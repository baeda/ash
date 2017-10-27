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

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.ashlang.ash.ast.ASTNode;
import org.ashlang.ash.ast.ASTPrinter;
import org.ashlang.ash.codegen.CodeGenerators;
import org.ashlang.ash.err.ErrorHandler;
import org.ashlang.ash.pass.CompilerPassChain;
import org.ashlang.ash.pass.CompilerPasses;
import org.ashlang.gen.AshLexer;
import org.ashlang.gen.AshParser;
import org.ashlang.gen.AshParser.FileContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class AshMain {

    private AshMain() { /**/ }

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Usage: ashc <file>");
            System.exit(1);
        }

        ASTNode rootNode = buildAST(CharStreams.fromPath(
            Paths.get(args[0]).normalize(), StandardCharsets.UTF_8));
        ASTPrinter.print(rootNode);

        String c11Src = translateToC11(rootNode);
        System.out.println("C11 source:\n==============");
        System.out.println(c11Src);
    }

    static ASTNode buildAST(String ashSrc) {
        return buildAST(CharStreams.fromString(ashSrc));
    }

    private static ASTNode buildAST(CharStream charStream) {
        AshLexer lexer = new AshLexer(charStream);
        AshParser parser = new AshParser(new CommonTokenStream(lexer));
        ErrorHandler errorHandler = new ErrorHandler();

        FileContext fileCtx = parser.file();

        ASTNode rootNode = ASTBuilder.buildAST(fileCtx);
        CompilerPassChain
            .withErrorHandler(errorHandler)
            .appendPass(CompilerPasses.TYPE_ASSIGN_PASS)
            .applyTo(rootNode);

        if (errorHandler.hasErrors()) {
            System.exit(1);
        }

        return rootNode;
    }

    static void compileToNative(ASTNode rootNode, Path outFile) {
        String c11Src = translateToC11(rootNode);
        compileToNative(c11Src, outFile);
    }

    private static String translateToC11(ASTNode rootNode) {
        return CodeGenerators.c11().generate(rootNode);
    }

    private static void compileToNative(String c11Src, Path outFile) {
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
