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

package org.ashlang.ash.lang;

import org.antlr.v4.runtime.*;
import org.ashlang.ash.ast.Token;
import org.ashlang.ash.err.ErrorHandler;

public class ParserErrorListener extends BaseErrorListener {

    private final ErrorHandler errorHandler;

    public ParserErrorListener(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override
    public void
    syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                int line, int column, String msg, RecognitionException e) {
        Token position = new Token((org.antlr.v4.runtime.Token) offendingSymbol);
        Parser parser = (Parser) recognizer;
        Vocabulary vocabulary = parser.getVocabulary();
        String expectedTokens = parser.getExpectedTokens().toString(vocabulary);

        if (e instanceof InputMismatchException) {
            errorHandler.emitInputMismatch(position, expectedTokens);
        } else if (e instanceof NoViableAltException) {
            new ConsoleErrorListener()
                .syntaxError(recognizer, offendingSymbol, line, column, msg, e);
        } else if (e instanceof FailedPredicateException) {
            new ConsoleErrorListener()
                .syntaxError(recognizer, offendingSymbol, line, column, msg, e);
        } else {
            errorHandler.emitMissingToken(position, expectedTokens);
        }
    }

}
