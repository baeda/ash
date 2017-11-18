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
import org.antlr.v4.runtime.misc.Interval;
import org.ashlang.ash.ast.Token;
import org.ashlang.ash.ast.TokenRange;
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
        if (!(recognizer instanceof Parser)) {
            throw new IllegalStateException(
                "This error handler may only be used with " + Parser.class);
        }

        Token position = new Token((org.antlr.v4.runtime.Token) offendingSymbol);
        Parser parser = (Parser) recognizer;
        Vocabulary vocabulary = parser.getVocabulary();
        String expectedTokens = parser.getExpectedTokens().toString(vocabulary);

        if (e instanceof InputMismatchException) {
            errorHandler.emitInputMismatch(position, expectedTokens);
        } else if (e instanceof NoViableAltException) {
            NoViableAltException nva = (NoViableAltException) e;

            TokenRange range = createRange(
                parser,
                nva.getStartToken(),
                nva.getOffendingToken()
            );

            errorHandler.emitInputMismatch(range, expectedTokens);
        } else if (e instanceof FailedPredicateException) {
            throw e;
        } else {
            errorHandler.emitMissingToken(position, expectedTokens);
        }
    }

    private TokenRange createRange(
        Parser parser,
        org.antlr.v4.runtime.Token start,
        org.antlr.v4.runtime.Token stop
    ) {
        return new TokenRange() {
            @Override
            public Token getStartToken() {
                return new Token(start);
            }

            @Override
            public Token getStopToken() {
                return new Token(stop);
            }

            @Override
            public String getText() {
                return parser
                    .getInputStream()
                    .getTokenSource()
                    .getInputStream()
                    .getText(Interval.of(
                        start.getStartIndex(),
                        stop.getStopIndex()
                    ));
            }
        };
    }

}
