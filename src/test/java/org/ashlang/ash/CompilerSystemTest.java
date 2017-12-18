/*
 * The Ash Project
 * Copyright (C) 2017  Peter Skrypalle
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 2 of the License only.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.ashlang.ash;

import org.apache.commons.lang3.tuple.Pair;
import org.ashlang.ash.ast.ASTNode;
import org.ashlang.ash.err.ConsoleErrorHandler;
import org.ashlang.ash.err.ErrorHandler;
import org.ashlang.ash.util.ExecResult;
import org.ashlang.ash.util.IOUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class CompilerSystemTest {

    @DataProvider(parallel = true)
    public Object[][]
    provideAshResourceBasePath() throws Exception {
        Set<String> basePaths = findTestResourceBasePaths();
        Object[][] result = new Object[basePaths.size()][];

        int i = 0;
        for (String basePath : basePaths) {
            result[i++] = new Object[]{basePath};
        }

        return result;
    }

    @Test(dataProvider = "provideAshResourceBasePath")
    public void
    c11_target(String basePath) throws Exception {
        Path source = getResourcePath(basePath, ".ash");
        Path result = getResourcePath(basePath, ".ash.result");
        String sourceString = IOUtil.readUTF8(source);
        String resultString = IOUtil.readUTF8(result);

        IOUtil.executeInTempDir(tmpDir -> {
            // [ash compiler] Arrange
            ByteArrayOutputStream errStream = new ByteArrayOutputStream();
            ErrorHandler errorHandler = new ConsoleErrorHandler(errStream);

            // [ash compiler] Act
            ASTNode rootNode = AshMain.buildAST(sourceString, errorHandler);

            // [ash compiler] Assert
            assertThat(errorHandler.hasErrors())
                .as(errStream.toString("UTF-8").trim())
                .isFalse();

            // [c11 compiler] Arrange
            Path outFile = tmpDir.resolve("out");

            // [c11 compiler] Act
            AshMain.compileToNative(rootNode, outFile);

            // [c11 compiler] Assert
            // asserted by thrown exception on failure

            // [bin execution] Arrange
            // [bin execution] Act
            ExecResult run = IOUtil.exec(outFile);

            // [bin execution] Assert
            assertThat(run.getErr()).isEmpty();
            assertThat(run.getOut()).isEqualToIgnoringWhitespace(resultString);
            assertThat(run.getExitCode()).isZero();
        });
    }

    @Test(dataProvider = "provideAshResourceBasePath")
    public void
    java8_target(String basePath) throws Exception {
        Path source = getResourcePath(basePath, ".ash");
        Path result = getResourcePath(basePath, ".ash.result");
        String sourceString = IOUtil.readUTF8(source);
        String resultString = IOUtil.readUTF8(result);

        IOUtil.executeInTempDir(tmpDir -> {
            // [ash compiler] Arrange
            ByteArrayOutputStream errStream = new ByteArrayOutputStream();
            ErrorHandler errorHandler = new ConsoleErrorHandler(errStream);

            // [ash compiler] Act
            ASTNode rootNode = AshMain.buildAST(sourceString, errorHandler);

            // [ash compiler] Assert
            assertThat(errorHandler.hasErrors())
                .as(errStream.toString("UTF-8").trim())
                .isFalse();

            // [java8 compiler] Arrange
            Path outFile = tmpDir.resolve("Main.class");

            // [java8 compiler] Act
            AshMain.compileToJVM(rootNode, outFile);

            // [java8 compiler] Assert
            // asserted by thrown exception on failure

            // [bin execution] Arrange
            // [bin execution] Act
            ExecResult run = IOUtil.execInDir(tmpDir, "java", "Main");

            // [bin execution] Assert
            assertThat(run.getErr()).isEmpty();
            assertThat(run.getOut()).isEqualToIgnoringWhitespace(resultString);
            assertThat(run.getExitCode()).isZero();
        });
    }

    //region helpers

    private static Path
    getResourcePath(String basePath, String ext) throws Exception {
        String resource = "/" + basePath + ext;
        URI uri = CompilerSystemTest.class.getResource(resource).toURI();
        return Paths.get(uri);
    }

    private static Set<String>
    findTestResourceBasePaths() throws Exception {
        URL resource = CompilerSystemTest.class.getResource("/");
        Path root = Paths.get(resource.toURI());

        List<Path> sources = Files.walk(root)
            .filter(path -> !Files.isDirectory(path))
            .filter(path -> path.toString().endsWith(".ash"))
            .map(path -> root.relativize(path).normalize())
            .collect(Collectors.toList());
        List<Path> expectedResults = Files.walk(root)
            .filter(path -> !Files.isDirectory(path))
            .filter(path -> path.toString().endsWith(".ash.result"))
            .map(path -> root.relativize(path).normalize())
            .collect(Collectors.toList());

        Map<String, Pair<Path, Path>> resourceMap = new HashMap<>();
        sources.forEach(path -> {
            String key = path.toString().replace(".ash", "");
            Pair<Path, Path> pair = resourceMap.get(key);
            if (pair != null) {
                throw new IllegalStateException();
            }

            resourceMap.put(key, Pair.of(path, null));
        });
        expectedResults.forEach(path -> {
            String key = path.toString().replace(".ash.result", "");
            Pair<Path, Path> pair = resourceMap.get(key);
            if (pair == null) {
                throw new IllegalStateException();
            }

            resourceMap.put(key, Pair.of(pair.getLeft(), path));
        });

        List<String> failures = new ArrayList<>();

        resourceMap.forEach((name, pair) -> {
            if (pair.getLeft() == null || pair.getRight() == null) {
                String message;
                if (pair.getLeft() == null) {
                    message = "missing source file " + name + ".ash";
                } else {
                    message = "missing result file " + name + ".ash.result";
                }
                failures.add(message);
            }
        });

        if (!failures.isEmpty()) {
            failures.sort(String.CASE_INSENSITIVE_ORDER);
            String s = "Error discovering test resources\n";
            fail(s + String.join("\n", failures));
        }

        return resourceMap.keySet();
    }

    //endregion

}
