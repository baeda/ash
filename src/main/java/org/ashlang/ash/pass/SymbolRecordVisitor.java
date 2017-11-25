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
import org.ashlang.ash.util.Defer;

class SymbolRecordVisitor extends ASTBaseVisitor<Void, Function> {

    private final ErrorHandler errorHandler;
    private final SymbolTable symbolTable;
    private final Defer defer;

    SymbolRecordVisitor(ErrorHandler errorHandler, SymbolTable symbolTable) {
        this.errorHandler = errorHandler;
        this.symbolTable = symbolTable;

        defer = new Defer();
    }

    @Override
    public Void
    visitFileNode(FileNode node, Function func) {
        symbolTable.pushScope();

        visitChildren(node, func);

        defer.runAll();

        symbolTable.popScope(node);
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

        symbolTable.pushScope();

        visitChildren(node, function);

        symbolTable.popScope(node);

        return null;
    }

    @Override
    public Void
    visitParamDeclarationNode(ParamDeclarationNode node, Function func) {
        Symbol symbol = symbolTable.getDeclaredSymbol(node);
        if (symbol != null) {
            errorHandler.emitSymbolAlreadyDeclared(
                node.getIdentifierToken(),
                symbol.getDeclSite().getIdentifierToken());
        } else {
            symbol = symbolTable.declareSymbol(node);
            symbol.initialize();
        }

        func.getParameters().add(symbol);
        node.setSymbol(symbol);
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
        visitChildren(node, func);

        String identifier = node.getIdentifierToken().getText();
        Symbol symbol = symbolTable.getDeclaredSymbol(identifier);
        if (symbol == null) {
            errorHandler.emitSymbolNotDeclared(node.getIdentifierToken());
        }

        node.setSymbol(symbol);
        return null;
    }

    @Override
    public Void
    visitBlockNode(BlockNode node, Function func) {
        symbolTable.pushScope();

        visitChildren(node, func);

        symbolTable.popScope(node);

        return null;
    }

    @Override
    public Void
    visitFuncCallNode(FuncCallNode node, Function func) {
        String identifier = node.getIdentifierToken().getText();

        visitChildren(node, func);

        // check function calls last, when we have
        // discovered all visible function signatures
        defer.record(() -> {
            Function declaredFunction = symbolTable.getDeclaredFunction(identifier);
            if (declaredFunction == null) {
                errorHandler.emitFunctionNotDeclared(node.getIdentifierToken());
            }

            node.setFunction(declaredFunction);
        });

        return null;
    }

    //region statement nodes

    @Override
    public Void
    visitExpressionStatementNode(ExpressionStatementNode node, Function func) {
        visitChildren(node, func);

        ExpressionNode expression = node.getExpression();
        if (!(expression instanceof FuncCallExpressionNode)) {
            errorHandler.emitIllegalStatement(node);
        }

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
        Token valueToken = node.getValueToken();
        String identifier = valueToken.getText();
        Symbol symbol = symbolTable.getDeclaredSymbol(identifier);

        if (symbol == null) {
            errorHandler.emitSymbolNotDeclared(valueToken);
            return null;
        }

        node.setSymbol(symbol);
        return null;
    }

    //endregion expression nodes

}
