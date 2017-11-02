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

package org.ashlang.ash.err;

import org.ashlang.ash.ast.Token;
import org.ashlang.ash.type.Type;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class ConsoleErrorHandler implements ErrorHandler {

    private final PrintStream out;

    private int numLexicalErrors;
    private int numSyntacticErrors;
    private int numSemanticErrors;

    public ConsoleErrorHandler() {
        this(System.err);
    }

    public ConsoleErrorHandler(OutputStream outStream) {
        this(createPrintStream(outStream));
    }

    private static PrintStream createPrintStream(OutputStream outStream) {
        try {
            return new PrintStream(outStream, false, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public ConsoleErrorHandler(PrintStream out) {
        this.out = out;

        numLexicalErrors = 0;
        numSyntacticErrors = 0;
        numSemanticErrors = 0;
    }

    @Override
    public boolean hasErrors() {
        return numLexicalErrors != 0
            || numSyntacticErrors != 0
            || numSemanticErrors != 0;
    }

    @Override
    public void flush() {
        out.flush();
    }

    @Override
    public void emitUnknownToken(Token pos) {
        emit(pos, "unknown token '%s'", pos.getText());
        numLexicalErrors++;
    }

    @Override
    public void emitMissingToken(Token pos, String expectedTokens) {
        emit(pos, "missing %s at '%s'", expectedTokens, pos.getText());
        numSyntacticErrors++;
    }

    @Override
    public void emitInputMismatch(Token pos, String expectedTokens) {
        emit(pos, "mismatched input '%s' expecting %s",
            pos.getText(), expectedTokens);
        numSyntacticErrors++;
    }

    @Override
    public void emitInvalidType(Token pos) {
        emit(pos, "invalid type '%s'", pos.getText());
        numSemanticErrors++;
    }

    @Override
    public void
    emitInvalidOperator(Token pos, Type left, Type right) {
        emit(pos, "invalid operator %s [%s] %s", left, pos.getText(), right);
        numSemanticErrors++;
    }

    @Override
    public void emitSymbolAlreadyDeclared(Token pos, Token declSite) {
        emit(pos, "symbol '%s' already declared at %d:%d.",
            pos.getText(), declSite.getLine() + 1, declSite.getColumn() + 1);
        numSemanticErrors++;
    }

    @Override
    public void emitSymbolNotDeclared(Token pos) {
        emit(pos, "symbol '%s' is not declared.", pos.getText());
        numSemanticErrors++;
    }

    private void emit(Token pos, String format, Object... args) {
        String position = formatPosition(pos);
        String message = String.format(format, args);
        out.printf("%s: error: %s\n", position, message);
    }

    private static String formatPosition(Token pos) {
        return String.format("%s:%d:%d",
            pos.getSourceName(), pos.getLine() + 1, pos.getColumn() + 1);
    }

}
