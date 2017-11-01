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

public class ConsoleErrorHandler implements ErrorHandler {

    private int numLexicalErrors;
    private int numSyntacticErrors;
    private int numSemanticErrors;

    public ConsoleErrorHandler() {
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
    public void flush() { /* no-op */ }

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

    private static void emit(Token pos, String format, Object... args) {
        String position = formatPosition(pos);
        String message = String.format(format, args);
        System.err.printf("%s: error: %s\n", position, message);
    }

    private static String formatPosition(Token pos) {
        return String.format("%s:%d:%d",
            pos.getSourceName(), pos.getLine() + 1, pos.getColumn() + 1);
    }

}
