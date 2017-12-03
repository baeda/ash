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

package org.ashlang.ash.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public final class IOUtil {

    private IOUtil() { /**/ }

    public static Version
    gccVersion() {
        ExecResult gccVersion = IOUtil.exec("gcc", "-dumpversion");
        if (gccVersion.isExceptional()) {
            System.err.println(gccVersion.getException().getMessage());
            return null;
        }
        if (gccVersion.hasErrors()) {
            System.err.println(gccVersion.getErr());
            return null;
        }

        String[] versionStrings = gccVersion.getOut().split("\\.");

        return new Version(
            safeToInt(versionStrings, 0),
            safeToInt(versionStrings, 1),
            safeToInt(versionStrings, 2)
        );
    }

    public static Version
    javacVersion() {
        ExecResult javacVersion = IOUtil.exec("javac", "-version");
        if (javacVersion.isExceptional()) {
            System.err.println(javacVersion.getException().getMessage());
            return null;
        }
        String combinedOut = javacVersion.getOut() + javacVersion.getErr();
        String rawVersion = combinedOut.replaceAll("javac", "").trim();
        String[] versionStrings = rawVersion.split("\\.");

        return new Version(
            safeToInt(versionStrings, 0),
            safeToInt(versionStrings, 1),
            safeToInt(versionStrings, 2)
        );
    }

    private static int
    safeToInt(String[] tokens, int index) {
        if (tokens.length <= index) {
            return 0;
        }

        try {
            String token = tokens[index];
            if (token == null) {
                return 0;
            }
            return Integer.parseInt(token.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static ExecResult
    exec(Path executable, Object... args) {
        String command = executable.toAbsolutePath().toString();
        return exec(command, args);
    }

    public static ExecResult
    exec(Object command, Object... args) {
        return execInDir(null, command, args);
    }

    public static ExecResult
    execInDir(Path execDir, Object command, Object... args) {
        File dir = execDir == null
            ? null
            : execDir.toFile();

        String[] cmd = new String[1 + args.length];
        cmd[0] = command.toString();
        for (int i = 0; i < args.length; i++) {
            cmd[i + 1] = args[i].toString();
        }

        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd, null, dir);

            Future<String> fOut
                = IOUtil.asyncExhaustiveReadStreamUTF8(process.getInputStream());
            Future<String> fErr
                = IOUtil.asyncExhaustiveReadStreamUTF8(process.getErrorStream());

            int exitCode = process.waitFor();
            String out = fOut.get();
            String err = fErr.get();
            return new ExecResult(exitCode, out, err, null);
        } catch (ExecutionException | InterruptedException | IOException e) {
            return new ExecResult(0xFFFFFFFF, "", "", e);
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    public static <X extends Throwable> void
    executeInTempDir(ThrowingConsumer<Path, X> consumer) throws X {
        Path tmpDir = null;
        try {
            tmpDir = Files.createTempDirectory(IOUtil.class.getCanonicalName())
                .toAbsolutePath();
            consumer.accept(tmpDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (tmpDir != null) {
                deleteRecursive(tmpDir);
            }
        }
    }

    public static <V, X extends Throwable> V
    executeInTempDir(ThrowingFunction<Path, V, X> consumer) throws X {
        Path tmpDir = null;
        try {
            tmpDir = Files.createTempDirectory(IOUtil.class.getCanonicalName())
                .toAbsolutePath();
            return consumer.apply(tmpDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (tmpDir != null) {
                deleteRecursive(tmpDir);
            }
        }
    }

    public static String
    exhaustiveReadStreamUTF8(InputStream in) {
        Scanner scanner = new Scanner(in, StandardCharsets.UTF_8.name())
            .useDelimiter("\\A+");
        return scanner.hasNext()
            ? scanner.next()
            : "";
    }


    public static Future<String>
    asyncExhaustiveReadStreamUTF8(InputStream in) {
        CompletableFuture<String> f = new CompletableFuture<>();
        new Thread(() -> f.complete(exhaustiveReadStreamUTF8(in))).start();
        return f;
    }

    public static String
    readUTF8(Path file) {
        try {
            return new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void
    writeUTF8(Path path, String content) {
        try {
            Files.write(path, content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void
    deleteRecursive(Path dir) {
        try {
            Files.walkFileTree(dir, RecursiveDeletingVisitor.INSTANCE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String
    tryIndent(String c11Src) {
        return executeInTempDir(tmpDir -> {
            Path outFile = tmpDir.resolve("main.c");
            writeUTF8(outFile, c11Src);
            ExecResult indent = exec(
                "indent",
                "-linux",
                "-nut",
                "-i4",
                outFile.toAbsolutePath()
            );

            if (indent.isExceptional() || indent.hasErrors()) {
                return c11Src;
            }

            return readUTF8(outFile);
        });
    }

    private static class
    RecursiveDeletingVisitor extends SimpleFileVisitor<Path> {

        private static final SimpleFileVisitor<Path> INSTANCE
            = new RecursiveDeletingVisitor();

        @Override
        public FileVisitResult
        visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult
        postVisitDirectory(Path dir, IOException exc) throws IOException {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
        }

    }

}
