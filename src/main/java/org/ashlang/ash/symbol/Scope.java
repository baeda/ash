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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.ashlang.ash.ast.DeclarationNode;
import org.ashlang.ash.ast.FuncDeclarationNode;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class Scope {

    private final Map<String, Symbol> symbolTable;
    private final Map<String, Function> functionTable;

    Scope() {
        symbolTable = new HashMap<>();
        functionTable = new HashMap<>();
    }

    private Scope(Scope other) {
        this.symbolTable = new HashMap<>(other.symbolTable);
        this.functionTable = new HashMap<>(other.functionTable);
    }

    Symbol
    declareSymbol(DeclarationNode declSite) {
        String identifier = declSite.getIdentifierToken().getText();
        Symbol symbol = symbolTable.get(identifier);
        if (symbol != null) {
            throw new IllegalStateException("Symbol " + symbol + " already declared.");
        }

        symbol = new Symbol(declSite);
        symbolTable.put(identifier, symbol);
        return symbol;
    }

    Symbol
    getDeclaredSymbol(DeclarationNode declSite) {
        String identifier = declSite.getIdentifierToken().getText();
        return getDeclaredSymbol(identifier);
    }

    Symbol
    getDeclaredSymbol(String identifier) {
        return symbolTable.get(identifier);
    }

    Collection<Symbol>
    getDeclaredSymbols() {
        return symbolTable.values();
    }

    Function
    declareFunction(FuncDeclarationNode declSite) {
        String identifier = declSite.getIdentifierToken().getText();
        Function function = functionTable.get(identifier);
        if (function != null) {
            throw new IllegalStateException("Function " + function + " already declared.");
        }

        function = new Function(declSite);
        functionTable.put(identifier, function);
        return function;
    }

    Function
    getDeclaredFunction(FuncDeclarationNode declSite) {
        String identifier = declSite.getIdentifierToken().getText();
        return getDeclaredFunction(identifier);
    }

    Function
    getDeclaredFunction(String identifier) {
        return functionTable.get(identifier);
    }

    Collection<Function>
    getDeclaredFunctions() {
        return functionTable.values();
    }

    Scope
    copy() {
        return new Scope(this);
    }

    @Override
    public String
    toString() {
        return new ToStringBuilder(this)
            .append("symbolTable", symbolTable)
            .append("functionTable", functionTable)
            .toString();
    }
}
