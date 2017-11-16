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

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.ashlang.ash.ast.Token;
import org.ashlang.ash.err.ErrorHandler;

public class LexerErrorListener extends BaseErrorListener {

    private final ErrorHandler errorHandler;

    public LexerErrorListener(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override
    public void
    syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                int line, int column, String msg, RecognitionException e) {
        Token token = new Token(
            line - 1 /* ANTLR line indices start at 1 */,
            column,
            (String) offendingSymbol,
            recognizer.getInputStream().getSourceName(),
            -1,
            -1);

        errorHandler.emitUnknownToken(token);
    }

}
