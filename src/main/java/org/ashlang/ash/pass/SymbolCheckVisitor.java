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

import org.ashlang.ash.ast.ASTBaseVisitor;
import org.ashlang.ash.ast.IdExpressionNode;
import org.ashlang.ash.ast.VarAssignNode;
import org.ashlang.ash.ast.VarDeclarationNode;
import org.ashlang.ash.err.ErrorHandler;
import org.ashlang.ash.symbol.Symbol;
import org.ashlang.ash.symbol.SymbolTable;

class SymbolCheckVisitor extends ASTBaseVisitor<Void, Void> {

    private final ErrorHandler errorHandler;
    private final SymbolTable symbolTable;

    SymbolCheckVisitor(ErrorHandler errorHandler, SymbolTable symbolTable) {
        this.errorHandler = errorHandler;
        this.symbolTable = symbolTable;
    }

    @Override
    public Void
    visitVarDeclarationNode(VarDeclarationNode node, Void argument) {
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
    public Void visitVarAssignNode(VarAssignNode node, Void argument) {
        String identifier = node.getIdentifierToken().getText();
        Symbol symbol = symbolTable.getDeclaredSymbol(identifier);
        if (symbol == null) {
            errorHandler.emitSymbolNotDeclared(node.getIdentifierToken());
        }

        node.setSymbol(symbol);
        return null;
    }

    //region Expression nodes

    @Override
    public Void visitIdExpressionNode(IdExpressionNode node, Void argument) {
        String identifier = node.getValue().getText();
        Symbol symbol = symbolTable.getDeclaredSymbol(identifier);
        node.setSymbol(symbol);
        return null;
    }

    //endregion Expression nodes

}
