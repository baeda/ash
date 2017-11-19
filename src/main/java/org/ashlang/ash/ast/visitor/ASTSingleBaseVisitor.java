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

package org.ashlang.ash.ast.visitor;

import org.ashlang.ash.ast.*;

public class ASTSingleBaseVisitor<T> extends ASTSingleVisitor<T> {

    @Override
    protected T
    visitFileNode(FileNode node) {
        return visitChildren(node);
    }

    @Override
    protected T
    visitFuncDeclarationNode(FuncDeclarationNode node) {
        return visitChildren(node);
    }

    @Override
    protected T
    visitVarDeclarationNode(VarDeclarationNode node) {
        return visitChildren(node);
    }

    @Override
    protected T
    visitVarAssignNode(VarAssignNode node) {
        return visitChildren(node);
    }

    @Override
    protected T
    visitBlockNode(BlockNode node) {
        return visitChildren(node);
    }

    //region statement nodes

    @Override
    protected T
    visitVarDeclarationStatementNode(VarDeclarationStatementNode node) {
        return visitChildren(node);
    }

    @Override
    protected T
    visitVarAssignStatementNode(VarAssignStatementNode node) {
        return visitChildren(node);
    }

    @Override
    protected T
    visitBlockStatementNode(BlockStatementNode node) {
        return visitChildren(node);
    }

    @Override
    protected T
    visitExpressionStatementNode(ExpressionStatementNode node) {
        return visitChildren(node);
    }

    @Override
    protected T
    visitDumpStatementNode(DumpStatementNode node) {
        return visitChildren(node);
    }

    @Override
    protected T
    visitReturnStatementNode(ReturnStatementNode node) {
        return visitChildren(node);
    }

    //endregion statement nodes

    //region expression nodes

    @Override
    protected T
    visitParenExpressionNode(ParenExpressionNode node) {
        return visitChildren(node);
    }

    @Override
    protected T
    visitAddExpressionNode(AddExpressionNode node) {
        return visitChildren(node);
    }

    @Override
    protected T
    visitSubExpressionNode(SubExpressionNode node) {
        return visitChildren(node);
    }

    @Override
    protected T
    visitMulExpressionNode(MulExpressionNode node) {
        return visitChildren(node);
    }

    @Override
    protected T
    visitDivExpressionNode(DivExpressionNode node) {
        return visitChildren(node);
    }

    @Override
    protected T
    visitModExpressionNode(ModExpressionNode node) {
        return visitChildren(node);
    }

    @Override
    protected T
    visitIdExpressionNode(IdExpressionNode node) {
        return visitChildren(node);
    }

    @Override
    protected T
    visitIntExpressionNode(IntExpressionNode node) {
        return visitChildren(node);
    }

    //region expression nodes

}
