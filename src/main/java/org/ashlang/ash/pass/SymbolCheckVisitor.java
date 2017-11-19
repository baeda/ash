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

import java.util.Collection;

class SymbolCheckVisitor extends ASTBaseVisitor<Void, Function> {

    private final ErrorHandler errorHandler;
    private final SymbolTable symbolTable;

    SymbolCheckVisitor(ErrorHandler errorHandler, SymbolTable symbolTable) {
        this.errorHandler = errorHandler;
        this.symbolTable = symbolTable;
    }

    @Override
    public Void
    visitFileNode(FileNode node, Function func) {
        visitChildren(node, func);

        checkSymbolUsage(symbolTable.getDeclaredSymbols());
        assertMainFunctionPresent(node.getStopToken(), symbolTable.getDeclaredFunctions());
        return null;
    }

    @Override
    public Void
    visitFuncDeclarationNode(FuncDeclarationNode node, Function func) {
        Function function = symbolTable.getDeclaredFunction(node);
        if (function != null) {
            errorHandler.emitFunctionAlreadyDeclared(
                node.getIdentifierToken(),
                function.getDeclSite().getIdentifierToken());
        } else {
            function = symbolTable.declareFunction(node);
        }

        node.setFunction(function);

        visitChildren(node, function);

        return null;
    }

    @Override
    public Void
    visitVarDeclarationNode(VarDeclarationNode node, Function func) {
        Symbol symbol = symbolTable.getDeclaredSymbol(node);
        if (symbol != null) {
            errorHandler.emitSymbolAlreadyDeclared(
                node.getIdentifierToken(),
                symbol.getDeclSite().getIdentifierToken());
        } else {
            symbol = symbolTable.declareSymbol(node);
        }

        node.setSymbol(symbol);
        return null;
    }

    @Override
    public Void
    visitVarAssignNode(VarAssignNode node, Function func) {
        String identifier = node.getIdentifierToken().getText();
        Symbol symbol = symbolTable.getDeclaredSymbol(identifier);
        if (symbol == null) {
            errorHandler.emitSymbolNotDeclared(node.getIdentifierToken());
        } else {
            symbol.initialize();
        }

        node.setSymbol(symbol);
        return null;
    }

    @Override
    public Void
    visitBlockNode(BlockNode node, Function func) {
        symbolTable.pushScope();

        visitChildren(node, func);

        checkSymbolUsage(symbolTable.getDeclaredSymbolsInCurrentScope());

        symbolTable.popScope();

        return null;
    }

    //region statement nodes


    @Override
    public Void
    visitExpressionStatementNode(ExpressionStatementNode node, Function func) {
        visitChildren(node, func);

        // for now, all expression statements are illegal.
        errorHandler.emitIllegalStatement(node);

        return null;
    }

    @Override
    public Void
    visitReturnStatementNode(ReturnStatementNode node, Function func) {
        node.setFunction(func);

        visitChildren(node, func);

        return null;
    }


    //endregion statement nodes

    //region expression nodes

    @Override
    public Void
    visitIdExpressionNode(IdExpressionNode node, Function func) {
        String identifier = node.getValue().getText();
        Symbol symbol = symbolTable.getDeclaredSymbol(identifier);

        if (symbol == null) {
            errorHandler.emitSymbolNotDeclared(node.getValue());
            return null;
        }

        symbol.use();

        if (!symbol.isInitialized()) {
            errorHandler.emitSymbolNotInitialized(
                node,
                symbol.getDeclSite().getIdentifierToken()
            );
        }

        node.setSymbol(symbol);
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
