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

class SymbolCheckVisitor extends ASTBaseVisitor<Void, Void> {

    private final ErrorHandler errorHandler;
    private final SymbolTable symbolTable;

    SymbolCheckVisitor(ErrorHandler errorHandler, SymbolTable symbolTable) {
        this.errorHandler = errorHandler;
        this.symbolTable = symbolTable;
    }

    @Override
    public Void
    visitFileNode(FileNode node, Void argument) {
        visitChildren(node, argument);

        SymbolTableSnapshot snapshot = symbolTable.recall(node);
        checkSymbolUsage(snapshot.getDeclaredSymbols());
        assertMainFunctionPresent(node.getStopToken(), snapshot.getDeclaredFunctions());

        return null;
    }

    @Override
    public Void
    visitFuncDeclarationNode(FuncDeclarationNode node, Void argument) {
        visitChildren(node, argument);

        SymbolTableSnapshot snapshot = symbolTable.recall(node);
        checkSymbolUsage(snapshot.getDeclaredSymbolsInCurrentScope());

        return null;
    }

    @Override
    public Void
    visitVarAssignNode(VarAssignNode node, Void argument) {
        visitChildren(node, argument);

        Symbol symbol = node.getSymbol();
        if (symbol == null) {
            // Symbol not declared.
            return null;
        }

        symbol.initialize();

        return null;
    }

    @Override
    public Void
    visitBlockNode(BlockNode node, Void argument) {
        visitChildren(node, argument);

        SymbolTableSnapshot snapshot = symbolTable.recall(node);
        checkSymbolUsage(snapshot.getDeclaredSymbolsInCurrentScope());

        return null;
    }

    //region expression nodes

    @Override
    public Void
    visitIdExpressionNode(IdExpressionNode node, Void argument) {
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
