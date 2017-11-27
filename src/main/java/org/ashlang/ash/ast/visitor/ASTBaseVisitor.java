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

public class ASTBaseVisitor<T, A> implements ASTVisitor<T, A> {

    @Override
    public T
    visitFileNode(FileNode node, A argument) {
        return visitChildren(node, argument);
    }

    @Override
    public T
    visitFuncDeclarationNode(FuncDeclarationNode node, A argument) {
        return visitChildren(node, argument);
    }

    @Override
    public T
    visitParamDeclarationNode(ParamDeclarationNode node, A argument) {
        return visitChildren(node, argument);
    }

    @Override
    public T
    visitVarDeclarationNode(VarDeclarationNode node, A argument) {
        return visitChildren(node, argument);
    }

    @Override
    public T
    visitVarAssignNode(VarAssignNode node, A argument) {
        return visitChildren(node, argument);
    }

    @Override
    public T
    visitBlockNode(BlockNode node, A argument) {
        return visitChildren(node, argument);
    }

    @Override
    public T
    visitFuncCallNode(FuncCallNode node, A argument) {
        return visitChildren(node, argument);
    }

    @Override
    public T
    visitArgumentNode(ArgumentNode node, A argument) {
        return visitChildren(node, argument);
    }

    @Override
    public T
    visitBranchNode(BranchNode node, A argument) {
        return visitChildren(node, argument);
    }

    //region statement nodes

    @Override
    public T
    visitVarDeclarationStatementNode(VarDeclarationStatementNode node, A argument) {
        return visitChildren(node, argument);
    }

    @Override
    public T
    visitVarAssignStatementNode(VarAssignStatementNode node, A argument) {
        return visitChildren(node, argument);
    }

    @Override
    public T
    visitBlockStatementNode(BlockStatementNode node, A argument) {
        return visitChildren(node, argument);
    }

    @Override
    public T
    visitBranchStatementNode(BranchStatementNode node, A argument) {
        return visitChildren(node, argument);
    }

    @Override
    public T
    visitExpressionStatementNode(ExpressionStatementNode node, A argument) {
        return visitChildren(node, argument);
    }

    @Override
    public T
    visitDumpStatementNode(DumpStatementNode node, A argument) {
        return visitChildren(node, argument);
    }

    @Override
    public T
    visitReturnStatementNode(ReturnStatementNode node, A argument) {
        return visitChildren(node, argument);
    }

    //endregion statement nodes

    //region expression nodes

    @Override
    public T
    visitParenExpressionNode(ParenExpressionNode node, A argument) {
        return visitChildren(node, argument);
    }

    @Override
    public T
    visitAddExpressionNode(AddExpressionNode node, A argument) {
        return visitChildren(node, argument);
    }

    @Override
    public T
    visitSubExpressionNode(SubExpressionNode node, A argument) {
        return visitChildren(node, argument);
    }

    @Override
    public T
    visitMulExpressionNode(MulExpressionNode node, A argument) {
        return visitChildren(node, argument);
    }

    @Override
    public T
    visitDivExpressionNode(DivExpressionNode node, A argument) {
        return visitChildren(node, argument);
    }

    @Override
    public T
    visitModExpressionNode(ModExpressionNode node, A argument) {
        return visitChildren(node, argument);
    }

    @Override
    public T
    visitEqualsExpressionNode(EqualsExpressionNode node, A argument) {
        return visitChildren(node, argument);
    }

    @Override
    public T
    visitFuncCallExpressionNode(FuncCallExpressionNode node, A argument) {
        return visitChildren(node, argument);
    }

    @Override
    public T
    visitBoolLiteralExpressionNode(BoolLiteralExpressionNode node, A argument) {
        return visitChildren(node, argument);
    }

    @Override
    public T
    visitIdExpressionNode(IdExpressionNode node, A argument) {
        return visitChildren(node, argument);
    }

    @Override
    public T
    visitIntExpressionNode(IntExpressionNode node, A argument) {
        return visitChildren(node, argument);
    }

    //endregion expression nodes

}
