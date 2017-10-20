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

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.ashlang.ash.AshParser.FileContext;
import org.ashlang.ash.ast.ASTNode;
import org.ashlang.ash.ast.ASTPrinter;
import org.ashlang.ash.codegen.CodeGenerators;

import java.nio.file.Path;

public final class AshMain {

    private AshMain() { /**/ }

    public static void main(String[] args) {
        ASTNode rootNode = buildAST("1+2");
        ASTPrinter.print(rootNode);
    }

    static ASTNode buildAST(String ashSrc) {
        AshLexer lexer = new AshLexer(CharStreams.fromString(ashSrc));
        AshParser parser = new AshParser(new CommonTokenStream(lexer));

        FileContext file = parser.file();

        return new ASTBuilder(null).visit(file);
    }

    static String compileToJVM(ASTNode rootNode, Path outDir) {
        String mainClassName = "_$Main";
        String java8Src = CodeGenerators.java8().generate(rootNode);

        Path javaFile = outDir.resolve(mainClassName + ".java");
        IOUtil.writeUTF8(javaFile, java8Src);

        ExecResult javac = IOUtil.exec("javac", javaFile.toAbsolutePath());

        if (javac.getExitCode() != 0) {
            throw new IllegalStateException(
                "ASH -> JVM(Java8) Compilation failed!\n" + javac.getErr());
        }

        return mainClassName;
    }

    static void compileToNative(ASTNode rootNode, Path outFile) {
        String c11Src = CodeGenerators.c11().generate(rootNode);
        Path outDir = outFile.getParent();
        Path tmpFile = outDir.resolve("main.c");
        IOUtil.writeUTF8(tmpFile, c11Src);

        ExecResult gcc = IOUtil.exec(
            "gcc",
            "-std=c11",
            "-Wall",
            "-Wextra",
            "-pedantic",
            "-o",
            outFile.toAbsolutePath(),
            tmpFile.toAbsolutePath()
        );

        if (gcc.getExitCode() != 0) {
            throw new IllegalStateException(
                "ASH -> Native(C11) Compilation failed!\n" + gcc.getErr());
        }
    }

}
