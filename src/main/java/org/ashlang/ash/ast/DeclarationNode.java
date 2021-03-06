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

package org.ashlang.ash.ast;

import org.ashlang.ash.symbol.Symbol;
import org.ashlang.ash.type.Type;

public abstract class DeclarationNode extends ASTNode {

    private Type type;
    private Symbol symbol;

    DeclarationNode(
        Token identifier,
        Token type,
        SourceProvider sourceProvider
    ) {
        super(identifier, type, sourceProvider);
    }

    public Token
    getIdentifierToken() {
        return getStartToken();
    }

    public Token
    getTypeToken() {
        return getStopToken();
    }

    public Type
    getType() {
        return type;
    }

    public void
    setType(Type type) {
        this.type = type;
    }

    public Symbol
    getSymbol() {
        return symbol;
    }

    public void
    setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

}
