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
import org.ashlang.ash.ast.visitor.ASTVoidBaseVisitor;
import org.ashlang.ash.err.ErrorHandler;
import org.ashlang.ash.symbol.Symbol;
import org.ashlang.ash.symbol.SymbolTable;

import java.util.Collection;

class SymbolCheckVisitor extends ASTVoidBaseVisitor {

    private final ErrorHandler errorHandler;
    private final SymbolTable symbolTable;

    SymbolCheckVisitor(ErrorHandler errorHandler, SymbolTable symbolTable) {
        this.errorHandler = errorHandler;
        this.symbolTable = symbolTable;
    }

    @Override
    protected void visitFileNode(FileNode node) {
        visitChildren(node);

        checkSymbolUsage(symbolTable.getDeclaredSymbols());
    }

    @Override
    protected void
    visitVarDeclarationNode(VarDeclarationNode node) {
        Symbol symbol = symbolTable.getDeclaredSymbol(node);
        if (symbol != null) {
            errorHandler.emitSymbolAlreadyDeclared(
                node.getIdentifierToken(),
                symbol.getDeclSite().getIdentifierToken());
        } else {
            symbol = symbolTable.declareSymbol(node);
        }

        node.setSymbol(symbol);
    }

    @Override
    protected void visitVarAssignNode(VarAssignNode node) {
        String identifier = node.getIdentifierToken().getText();
        Symbol symbol = symbolTable.getDeclaredSymbol(identifier);
        if (symbol == null) {
            errorHandler.emitSymbolNotDeclared(node.getIdentifierToken());
        } else {
            symbol.initialize();
        }

        node.setSymbol(symbol);
    }

    @Override
    protected void visitBlockNode(BlockNode node) {
        symbolTable.pushScope();

        visitChildren(node);

        checkSymbolUsage(symbolTable.getDeclaredSymbolsInCurrentScope());

        symbolTable.popScope();
    }

    //region expression nodes

    @Override
    protected void visitIdExpressionNode(IdExpressionNode node) {
        String identifier = node.getValue().getText();
        Symbol symbol = symbolTable.getDeclaredSymbol(identifier);

        if (symbol == null) {
            errorHandler.emitSymbolNotDeclared(node.getValue());
            return;
        }

        symbol.use();

        if (!symbol.isInitialized()) {
            errorHandler.emitSymbolNotInitialized(
                node,
                symbol.getDeclSite().getIdentifierToken()
            );
        }

        node.setSymbol(symbol);
    }

    //endregion expression nodes

    private void checkSymbolUsage(Collection<Symbol> symbols) {
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

}
