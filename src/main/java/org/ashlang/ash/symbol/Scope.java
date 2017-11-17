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

package org.ashlang.ash.symbol;

import org.ashlang.ash.ast.VarDeclarationNode;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class Scope {

    private final Map<String, Symbol> symbolTable;

    Scope() {
        symbolTable = new HashMap<>();
    }

    Symbol declareSymbol(VarDeclarationNode declSite) {
        String identifier = declSite.getIdentifierToken().getText();
        Symbol symbol = symbolTable.get(identifier);
        if (symbol != null) {
            throw new IllegalStateException("Symbol " + symbol + " already declared.");
        }

        symbol = new Symbol(declSite);
        symbolTable.put(identifier, symbol);
        return symbol;
    }

    Symbol getDeclaredSymbol(VarDeclarationNode declSite) {
        String identifier = declSite.getIdentifierToken().getText();
        return getDeclaredSymbol(identifier);
    }

    Symbol getDeclaredSymbol(String identifier) {
        return symbolTable.get(identifier);
    }

    Collection<Symbol> getDeclaredSymbols() {
        return symbolTable.values();
    }

}
