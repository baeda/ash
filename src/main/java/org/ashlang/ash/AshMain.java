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
import org.ashlang.ash.ast.ASTNode;
import org.ashlang.ash.ast.ASTPrinter;
import org.ashlang.ash.codegen.CodeGenerators;
import org.ashlang.gen.AshLexer;
import org.ashlang.gen.AshParser;
import org.ashlang.gen.AshParser.FileContext;

import java.nio.file.Path;

public final class AshMain {

    private AshMain() { /**/ }

    public static void main(String[] args) {
        ASTNode rootNode = buildAST("dump 1+2;");
        ASTPrinter.print(rootNode);
    }

    static ASTNode buildAST(String ashSrc) {
        AshLexer lexer = new AshLexer(CharStreams.fromString(ashSrc));
        AshParser parser = new AshParser(new CommonTokenStream(lexer));

        FileContext file = parser.file();

        return new ASTBuilder(null).visit(file);
    }

    static void compileToNative(ASTNode rootNode, Path outFile) {
        String c11Src = CodeGenerators.c11().generate(rootNode);
        Path outDir = outFile.getParent();
        if (outDir == null) {
            throw new IllegalArgumentException(String.format(
                "Could not get parent directory of file %s",
                outFile.toAbsolutePath()));
        }
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
