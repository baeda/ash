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

public abstract class ASTSingleVisitor<T> implements ASTVisitor<T, Void> {

    protected abstract T visitFileNode(FileNode node);

    protected abstract T visitFuncDeclarationNode(FuncDeclarationNode node);
    protected abstract T visitParamDeclarationNode(ParamDeclarationNode node);
    protected abstract T visitVarDeclarationNode(VarDeclarationNode node);
    protected abstract T visitVarAssignNode(VarAssignNode node);
    protected abstract T visitBlockNode(BlockNode node);
    protected abstract T visitFuncCallNode(FuncCallNode node);
    protected abstract T visitArgumentNode(ArgumentNode node);
    protected abstract T visitBranchNode(BranchNode node);
    protected abstract T visitWhileLoopNode(WhileLoopNode node);

    protected abstract T visitVarDeclarationStatementNode(VarDeclarationStatementNode node);
    protected abstract T visitVarAssignStatementNode(VarAssignStatementNode node);
    protected abstract T visitBlockStatementNode(BlockStatementNode node);
    protected abstract T visitBranchStatementNode(BranchStatementNode node);
    protected abstract T visitWhileLoopStatementNode(WhileLoopStatementNode node);
    protected abstract T visitExpressionStatementNode(ExpressionStatementNode node);
    protected abstract T visitDumpStatementNode(DumpStatementNode node);
    protected abstract T visitReturnStatementNode(ReturnStatementNode node);

    protected abstract T visitParenExpressionNode(ParenExpressionNode node);
    protected abstract T visitAddExpressionNode(AddExpressionNode node);
    protected abstract T visitSubExpressionNode(SubExpressionNode node);
    protected abstract T visitMulExpressionNode(MulExpressionNode node);
    protected abstract T visitDivExpressionNode(DivExpressionNode node);
    protected abstract T visitModExpressionNode(ModExpressionNode node);
    protected abstract T visitEqualsExpressionNode(EqualsExpressionNode node);
    protected abstract T visitNotEqualsExpressionNode(NotEqualsExpressionNode node);
    protected abstract T visitLtExpressionNode(LtExpressionNode node);
    protected abstract T visitGtExpressionNode(GtExpressionNode node);
    protected abstract T visitLtEqExpressionNode(LtEqExpressionNode node);
    protected abstract T visitGtEqExpressionNode(GtEqExpressionNode node);
    protected abstract T visitFuncCallExpressionNode(FuncCallExpressionNode node);
    protected abstract T visitBoolLiteralExpressionNode(BoolLiteralExpressionNode node);
    protected abstract T visitIdExpressionNode(IdExpressionNode node);
    protected abstract T visitIntExpressionNode(IntExpressionNode node);

    protected T
    visitChildren(ASTNode node) {
        return visitChildren(node, null);
    }

    protected T
    visit(ASTNode node) {
        return visit(node, null);
    }

    //region adapter

    @Override
    public final T
    visitFileNode(FileNode node, Void argument) {
        return visitFileNode(node);
    }

    @Override
    public final T
    visitFuncDeclarationNode(FuncDeclarationNode node, Void argument) {
        return visitFuncDeclarationNode(node);
    }

    @Override
    public final T
    visitParamDeclarationNode(ParamDeclarationNode node, Void argument) {
        return visitParamDeclarationNode(node);
    }

    @Override
    public final T
    visitVarDeclarationNode(VarDeclarationNode node, Void argument) {
        return visitVarDeclarationNode(node);
    }

    @Override
    public final T
    visitVarAssignNode(VarAssignNode node, Void argument) {
        return visitVarAssignNode(node);
    }

    @Override
    public final T
    visitBlockNode(BlockNode node, Void argument) {
        return visitBlockNode(node);
    }

    @Override
    public final T
    visitFuncCallNode(FuncCallNode node, Void argument) {
        return visitFuncCallNode(node);
    }

    @Override
    public final T
    visitArgumentNode(ArgumentNode node, Void argument) {
        return visitArgumentNode(node);
    }

    @Override
    public final T
    visitBranchNode(BranchNode node, Void argument) {
        return visitBranchNode(node);
    }

    @Override
    public final T
    visitWhileLoopNode(WhileLoopNode node, Void argument) {
        return visitWhileLoopNode(node);
    }

    //region statement nodes

    @Override
    public final T
    visitVarDeclarationStatementNode(VarDeclarationStatementNode node, Void argument) {
        return visitVarDeclarationStatementNode(node);
    }

    @Override
    public final T
    visitVarAssignStatementNode(VarAssignStatementNode node, Void argument) {
        return visitVarAssignStatementNode(node);
    }

    @Override
    public final T
    visitBlockStatementNode(BlockStatementNode node, Void argument) {
        return visitBlockStatementNode(node);
    }

    @Override
    public final T
    visitBranchStatementNode(BranchStatementNode node, Void argument) {
        return visitBranchStatementNode(node);
    }

    @Override
    public final T
    visitWhileLoopStatementNode(WhileLoopStatementNode node, Void argument) {
        return visitWhileLoopStatementNode(node);
    }

    @Override
    public final T
    visitExpressionStatementNode(ExpressionStatementNode node, Void argument) {
        return visitExpressionStatementNode(node);
    }

    @Override
    public final T
    visitDumpStatementNode(DumpStatementNode node, Void argument) {
        return visitDumpStatementNode(node);
    }

    @Override
    public final T
    visitReturnStatementNode(ReturnStatementNode node, Void argument) {
        return visitReturnStatementNode(node);
    }

    //endregion statement nodes

    //region expression nodes

    @Override
    public final T
    visitParenExpressionNode(ParenExpressionNode node, Void argument) {
        return visitParenExpressionNode(node);
    }

    @Override
    public final T
    visitAddExpressionNode(AddExpressionNode node, Void argument) {
        return visitAddExpressionNode(node);
    }

    @Override
    public final T
    visitSubExpressionNode(SubExpressionNode node, Void argument) {
        return visitSubExpressionNode(node);
    }

    @Override
    public final T
    visitMulExpressionNode(MulExpressionNode node, Void argument) {
        return visitMulExpressionNode(node);
    }

    @Override
    public final T
    visitDivExpressionNode(DivExpressionNode node, Void argument) {
        return visitDivExpressionNode(node);
    }

    @Override
    public final T
    visitModExpressionNode(ModExpressionNode node, Void argument) {
        return visitModExpressionNode(node);
    }

    @Override
    public final T
    visitEqualsExpressionNode(EqualsExpressionNode node, Void argument) {
        return visitEqualsExpressionNode(node);
    }

    @Override
    public final T
    visitNotEqualsExpressionNode(NotEqualsExpressionNode node, Void argument) {
        return visitNotEqualsExpressionNode(node);
    }

    @Override
    public final T
    visitLtExpressionNode(LtExpressionNode node, Void argument) {
        return visitLtExpressionNode(node);
    }

    @Override
    public final T
    visitGtExpressionNode(GtExpressionNode node, Void argument) {
        return visitGtExpressionNode(node);
    }

    @Override
    public final T
    visitLtEqExpressionNode(LtEqExpressionNode node, Void argument) {
        return visitLtEqExpressionNode(node);
    }

    @Override
    public final T
    visitGtEqExpressionNode(GtEqExpressionNode node, Void argument) {
        return visitGtEqExpressionNode(node);
    }

    @Override
    public final T
    visitFuncCallExpressionNode(FuncCallExpressionNode node, Void argument) {
        return visitFuncCallExpressionNode(node);
    }

    @Override
    public final T
    visitBoolLiteralExpressionNode(BoolLiteralExpressionNode node, Void argument) {
        return visitBoolLiteralExpressionNode(node);
    }

    @Override
    public final T
    visitIdExpressionNode(IdExpressionNode node, Void argument) {
        return visitIdExpressionNode(node);
    }

    @Override
    public final T
    visitIntExpressionNode(IntExpressionNode node, Void argument) {
        return visitIntExpressionNode(node);
    }

    //endregion expression nodes

    //endregion adapter

}
