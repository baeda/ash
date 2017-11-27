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

package org.ashlang.ash.pass;

import org.ashlang.ash.ast.*;
import org.ashlang.ash.ast.visitor.ASTBaseVisitor;
import org.ashlang.ash.err.ErrorHandler;
import org.ashlang.ash.symbol.Function;
import org.ashlang.ash.symbol.Symbol;
import org.ashlang.ash.symbol.SymbolTable;
import org.ashlang.ash.symbol.SymbolTableSnapshot;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class SymbolCheckVisitor extends ASTBaseVisitor<Void, Set<Symbol>> {

    private final ErrorHandler errorHandler;
    private final SymbolTable symbolTable;

    SymbolCheckVisitor(ErrorHandler errorHandler, SymbolTable symbolTable) {
        this.errorHandler = errorHandler;
        this.symbolTable = symbolTable;
    }

    @Override
    public Void
    visitFileNode(FileNode node, Set<Symbol> symbols) {
        visitChildren(node, new HashSet<>());

        SymbolTableSnapshot snapshot = symbolTable.recall(node);
        checkSymbolUsage(snapshot.getDeclaredSymbols());
        assertMainFunctionPresent(node.getStopToken(), snapshot.getDeclaredFunctions());

        return null;
    }

    @Override
    public Void
    visitFuncDeclarationNode(FuncDeclarationNode node, Set<Symbol> symbols) {
        visitChildren(node, symbols);

        SymbolTableSnapshot snapshot = symbolTable.recall(node);
        checkSymbolUsage(snapshot.getDeclaredSymbolsInCurrentScope());

        return null;
    }

    @Override
    public Void
    visitVarAssignNode(VarAssignNode node, Set<Symbol> symbols) {
        visitChildren(node, symbols);

        Symbol symbol = node.getSymbol();
        if (symbol == null) {
            // Symbol not declared.
            return null;
        }

        if (symbols != null) {
            symbols.add(symbol);
        }

        symbol.initialize();

        return null;
    }

    @Override
    public Void
    visitBlockNode(BlockNode node, Set<Symbol> symbols) {
        visitChildren(node, symbols);

        SymbolTableSnapshot snapshot = symbolTable.recall(node);
        checkSymbolUsage(snapshot.getDeclaredSymbolsInCurrentScope());

        return null;
    }

    @Override
    public Void
    visitBranchNode(BranchNode node, Set<Symbol> symbols) {
        // Algorithm overview:
        // ----------------------|--------
        // initialized on true:  | a,b,c
        // initialized on false: | a,  c,d
        // ----------------------|--------
        // to initialize:        | a,  c
        // to deinitialize:      |   b,  d

        visit(node.getExpression(), symbols);

        Set<Symbol> symbolsInitializedOnTrue = new HashSet<>();
        symbolsInitializedOnTrue.addAll(symbols);
        visit(node.getOnTrue(), symbolsInitializedOnTrue);

        Set<Symbol> symbolsInitializedOnFalse = new HashSet<>();
        symbolsInitializedOnFalse.addAll(symbols);
        visit(node.getOnFalse(), symbolsInitializedOnFalse);

        Set<Symbol> toInitialize = symbolsInitializedOnTrue.stream()
            .filter(symbolsInitializedOnFalse::contains)
            .collect(Collectors.toSet());

        Set<Symbol> toDeinitialize = Stream.concat(
            symbolsInitializedOnTrue.stream(),
            symbolsInitializedOnFalse.stream()
        )
            .filter(symbol -> !toInitialize.contains(symbol))
            .collect(Collectors.toSet());

        toInitialize.forEach(Symbol::initialize);
        toDeinitialize.forEach(Symbol::deinitialize);

        symbols.addAll(toInitialize);

        return null;
    }

    //region expression nodes

    @Override
    public Void
    visitIdExpressionNode(IdExpressionNode node, Set<Symbol> symbols) {
        Symbol symbol = node.getSymbol();

        if (symbol == null) {
            // Symbol not declared.
            return null;
        }

        symbol.use();

        if (!symbol.isInitialized()) {
            errorHandler.emitSymbolNotInitialized(
                node,
                symbol.getDeclSite().getIdentifierToken()
            );
        }

        return null;
    }

    //endregion expression nodes

    private void
    checkSymbolUsage(Collection<Symbol> symbols) {
        symbols.stream()
            .filter(sym -> !sym.isUsed())
            .forEach(sym -> {
                Token id = sym.getDeclSite().getIdentifierToken();
                if (!sym.isInitialized()) {
                    errorHandler.emitSymbolNotUsed(id);
                } else {
                    errorHandler.emitSymbolInitializedButNotUsed(id);
                }
            });
    }

    private void
    assertMainFunctionPresent(Token pos, Collection<Function> functions) {
        long numMainFunctions = functions.stream()
            .filter(func -> "main".equals(func.getIdentifier()))
            .count();

        if (numMainFunctions <= 0L) {
            errorHandler.emitNoEntryPoint(pos);
        }
    }

}
