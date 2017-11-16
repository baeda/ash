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

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;

public final class IOUtil {

    private IOUtil() { /**/ }

    public static ExecResult exec(Path executable, Object... args) {
        String[] cmd = new String[1 + args.length];
        cmd[0] = executable.toAbsolutePath().toString();
        for (int i = 0; i < args.length; i++) {
            cmd[i + 1] = args[i].toString();
        }

        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
            int exitCode = process.waitFor();
            String out = IOUtil.exhaustiveReadStreamUTF8(process.getInputStream());
            String err = IOUtil.exhaustiveReadStreamUTF8(process.getErrorStream());
            return new ExecResult(exitCode, out, err);
        } catch (InterruptedException | IOException e) {
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            return new ExecResult(0xFFFFFFFF, "", writer.toString());
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    public static ExecResult exec(Object command, Object... args) {
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
            int exitCode = process.waitFor();
            String out = IOUtil.exhaustiveReadStreamUTF8(process.getInputStream());
            String err = IOUtil.exhaustiveReadStreamUTF8(process.getErrorStream());
            return new ExecResult(exitCode, out, err);
        } catch (InterruptedException | IOException e) {
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            return new ExecResult(0xFFFFFFFF, "", writer.toString());
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

    public static String exhaustiveReadStreamUTF8(InputStream in) {
        Scanner scanner = new Scanner(in, "UTF-8").useDelimiter("\\A+");
        return scanner.hasNext()
            ? scanner.next()
            : "";
    }

    public static void writeUTF8(Path path, String content) {
        try {
            Files.write(path, content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteRecursive(Path dir) {
        try {
            Files.walkFileTree(dir, RecursiveDeletingVisitor.INSTANCE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
