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

public class ASTVoidBaseVisitor extends ASTVoidVisitor {

    @Override
    protected void
    visitFileNode(FileNode node) {
        visitChildren(node);
    }

    @Override
    protected void
    visitFuncDeclarationNode(FuncDeclarationNode node) {
        visitChildren(node);
    }

    @Override
    protected void
    visitParamDeclarationNode(ParamDeclarationNode node) {
        visitChildren(node);
    }

    @Override
    protected void
    visitVarDeclarationNode(VarDeclarationNode node) {
        visitChildren(node);
    }

    @Override
    protected void
    visitVarAssignNode(VarAssignNode node) {
        visitChildren(node);
    }

    @Override
    protected void
    visitBlockNode(BlockNode node) {
        visitChildren(node);
    }

    @Override
    protected void
    visitFuncCallNode(FuncCallNode node) {
        visitChildren(node);
    }

    @Override
    protected void
    visitArgumentNode(ArgumentNode node) {
        visitChildren(node);
    }

    @Override
    protected void
    visitBranchNode(BranchNode node) {
        visitChildren(node);
    }

    //region statement nodes

    @Override
    protected void
    visitVarDeclarationStatementNode(VarDeclarationStatementNode node) {
        visitChildren(node);
    }

    @Override
    protected void
    visitVarAssignStatementNode(VarAssignStatementNode node) {
        visitChildren(node);
    }

    @Override
    protected void
    visitBlockStatementNode(BlockStatementNode node) {
        visitChildren(node);
    }

    @Override
    protected void
    visitBranchStatementNode(BranchStatementNode node) {
        visitChildren(node);
    }

    @Override
    protected void
    visitExpressionStatementNode(ExpressionStatementNode node) {
        visitChildren(node);
    }

    @Override
    protected void
    visitDumpStatementNode(DumpStatementNode node) {
        visitChildren(node);
    }

    @Override
    protected void
    visitReturnStatementNode(ReturnStatementNode node) {
        visitChildren(node);
    }

    //endregion statement nodes

    //region expression nodes

    @Override
    protected void
    visitParenExpressionNode(ParenExpressionNode node) {
        visitChildren(node);
    }

    @Override
    protected void
    visitAddExpressionNode(AddExpressionNode node) {
        visitChildren(node);
    }

    @Override
    protected void
    visitSubExpressionNode(SubExpressionNode node) {
        visitChildren(node);
    }

    @Override
    protected void
    visitMulExpressionNode(MulExpressionNode node) {
        visitChildren(node);
    }

    @Override
    protected void
    visitDivExpressionNode(DivExpressionNode node) {
        visitChildren(node);
    }

    @Override
    protected void
    visitModExpressionNode(ModExpressionNode node) {
        visitChildren(node);
    }

    @Override
    protected void
    visitFuncCallExpressionNode(FuncCallExpressionNode node) {
        visitChildren(node);
    }

    @Override
    protected void
    visitBoolLiteralExpressionNode(BoolLiteralExpressionNode node) {
        visitChildren(node);
    }

    @Override
    protected void
    visitIdExpressionNode(IdExpressionNode node) {
        visitChildren(node);
    }

    @Override
    protected void
    visitIntExpressionNode(IntExpressionNode node) {
        visitChildren(node);
    }

}
