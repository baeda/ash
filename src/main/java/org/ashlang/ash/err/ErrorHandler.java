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
import org.ashlang.ash.ast.TokenRange;
import org.ashlang.ash.type.Type;

public interface ErrorHandler {

    boolean hasErrors();
    void flush();

    void emitUnknownToken(Token pos);
    void emitMissingToken(Token pos, String expectedTokens);
    void emitInputMismatch(Token pos, String expectedTokens);
    void emitInputMismatch(TokenRange pos, String expectedTokens);
    void emitInvalidType(Token pos);
    void emitTypeMismatch(TokenRange pos, Type have, Type want);
    void emitInvalidOperator(Token pos, Type left, Type right);
    void emitSymbolAlreadyDeclared(Token pos, Token declSite);
    void emitSymbolNotDeclared(Token pos);
    void emitSymbolNotInitialized(TokenRange pos, Token declSite);
    void emitSymbolNotUsed(Token pos);
    void emitSymbolInitializedButNotUsed(Token pos);
    void emitFunctionAlreadyDeclared(Token pos, Token declSite);
    void emitFunctionNotDeclared(Token pos);
    void emitFunctionArgumentCountMismatch(TokenRange pos, int have, int want);

    void emitIllegalStatement(TokenRange pos);
    void emitMissingReturnStatement(Token pos);
    void emitUnreachableStatement(TokenRange pos);

    void emitDivisionByZero(TokenRange pos);
    void emitIntConstantOverflow(TokenRange pos, Type have);
    void emitIntConstantUnderflow(TokenRange pos, Type have);

    void emitNoEntryPoint(Token pos);

}
