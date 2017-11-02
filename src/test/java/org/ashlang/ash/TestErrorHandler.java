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
import org.ashlang.ash.ast.Token;
import org.ashlang.ash.err.ErrorHandler;
import org.ashlang.ash.type.Type;

import java.util.ArrayList;
import java.util.List;

import static org.ashlang.ash.ErrorType.*;

class TestErrorHandler implements ErrorHandler {

    private final List<Pair<ErrorType, Token>> errors;

    TestErrorHandler() {
        this.errors = new ArrayList<>();
    }

    List<Pair<ErrorType, Token>> getErrors() {
        return errors;
    }

    @Override
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    @Override
    public void flush() { /* no-op */ }

    @Override
    public void emitUnknownToken(Token pos) {
        addError(UNKNOWN_TOKEN, pos);
    }

    @Override
    public void emitMissingToken(Token pos, String expectedTokens) {
        addError(MISSING_TOKEN, pos);
    }

    @Override
    public void emitInputMismatch(Token pos, String expectedTokens) {
        addError(INPUT_MISMATCH, pos);
    }

    @Override
    public void emitInvalidType(Token pos) {
        addError(INVALID_TYPE, pos);
    }

    @Override
    public void emitInvalidOperator(Token pos, Type left, Type right) {
        addError(INVALID_OPERATOR, pos);
    }

    @Override
    public void emitSymbolAlreadyDeclared(Token pos, Token declSite) {
        addError(SYMBOL_ALREADY_DECLARED, pos);
    }

    @Override
    public void emitSymbolNotDeclared(Token pos) {
        addError(SYMBOL_NOT_DECLARED, pos);
    }

    private void addError(ErrorType errorType, Token pos) {
        errors.add(Pair.of(errorType, pos));
    }

}
