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

public abstract class ASTVoidVisitor implements ASTVisitor<Void, Void> {

    protected abstract void visitFileNode(FileNode node);

    protected abstract void visitFuncDeclarationNode(FuncDeclarationNode node);
    protected abstract void visitParamDeclarationNode(ParamDeclarationNode node);
    protected abstract void visitVarDeclarationNode(VarDeclarationNode node);
    protected abstract void visitVarAssignNode(VarAssignNode node);
    protected abstract void visitVarDeclAssignNode(VarDeclAssignNode node);
    protected abstract void visitBlockNode(BlockNode node);
    protected abstract void visitFuncCallNode(FuncCallNode node);
    protected abstract void visitArgumentNode(ArgumentNode node);
    protected abstract void visitBranchNode(BranchNode node);
    protected abstract void visitWhileLoopNode(WhileLoopNode node);
    protected abstract void visitForLoopNode(ForLoopNode node);
    protected abstract void visitForLoopActionNode(ForLoopActionNode node);

    protected abstract void visitVarDeclarationStatementNode(VarDeclarationStatementNode node);
    protected abstract void visitVarAssignStatementNode(VarAssignStatementNode node);
    protected abstract void visitVarDeclAssignStatementNode(VarDeclAssignStatementNode node);
    protected abstract void visitBlockStatementNode(BlockStatementNode node);
    protected abstract void visitBranchStatementNode(BranchStatementNode node);
    protected abstract void visitWhileLoopStatementNode(WhileLoopStatementNode node);
    protected abstract void visitForLoopStatementNode(ForLoopStatementNode node);
    protected abstract void visitExpressionStatementNode(ExpressionStatementNode node);
    protected abstract void visitDumpStatementNode(DumpStatementNode node);
    protected abstract void visitReturnStatementNode(ReturnStatementNode node);

    protected abstract void visitParenExpressionNode(ParenExpressionNode node);
    protected abstract void visitAddExpressionNode(AddExpressionNode node);
    protected abstract void visitSubExpressionNode(SubExpressionNode node);
    protected abstract void visitMulExpressionNode(MulExpressionNode node);
    protected abstract void visitDivExpressionNode(DivExpressionNode node);
    protected abstract void visitModExpressionNode(ModExpressionNode node);
    protected abstract void visitEqualsExpressionNode(EqualsExpressionNode node);
    protected abstract void visitNotEqualsExpressionNode(NotEqualsExpressionNode node);
    protected abstract void visitLtExpressionNode(LtExpressionNode node);
    protected abstract void visitGtExpressionNode(GtExpressionNode node);
    protected abstract void visitLtEqExpressionNode(LtEqExpressionNode node);
    protected abstract void visitGtEqExpressionNode(GtEqExpressionNode node);
    protected abstract void visitFuncCallExpressionNode(FuncCallExpressionNode node);
    protected abstract void visitBoolLiteralExpressionNode(BoolLiteralExpressionNode node);
    protected abstract void visitIdExpressionNode(IdExpressionNode node);
    protected abstract void visitIntExpressionNode(IntExpressionNode node);

    protected void
    visitChildren(ASTNode node) {
        visitChildren(node, null);
    }

    public void
    visit(ASTNode node) {
        visit(node, null);
    }

    //region adapter

    @Override
    public final Void
    visitFileNode(FileNode node, Void argument) {
        visitFileNode(node);
        return null;
    }

    @Override
    public final Void
    visitFuncDeclarationNode(FuncDeclarationNode node, Void argument) {
        visitFuncDeclarationNode(node);
        return null;
    }

    @Override
    public final Void
    visitParamDeclarationNode(ParamDeclarationNode node, Void argument) {
        visitParamDeclarationNode(node);
        return null;
    }

    @Override
    public final Void
    visitVarDeclarationNode(VarDeclarationNode node, Void argument) {
        visitVarDeclarationNode(node);
        return null;
    }

    @Override
    public final Void
    visitVarAssignNode(VarAssignNode node, Void argument) {
        visitVarAssignNode(node);
        return null;
    }

    @Override
    public final Void
    visitVarDeclAssignNode(VarDeclAssignNode node, Void argument) {
        visitVarDeclAssignNode(node);
        return null;
    }

    @Override
    public final Void
    visitBlockNode(BlockNode node, Void argument) {
        visitBlockNode(node);
        return null;
    }

    @Override
    public final Void
    visitFuncCallNode(FuncCallNode node, Void argument) {
        visitFuncCallNode(node);
        return null;
    }

    @Override
    public final Void
    visitArgumentNode(ArgumentNode node, Void argument) {
        visitArgumentNode(node);
        return null;
    }

    @Override
    public final Void
    visitBranchNode(BranchNode node, Void argument) {
        visitBranchNode(node);
        return null;
    }

    @Override
    public final Void
    visitWhileLoopNode(WhileLoopNode node, Void argument) {
        visitWhileLoopNode(node);
        return null;
    }

    @Override
    public final Void
    visitForLoopNode(ForLoopNode node, Void argument) {
        visitForLoopNode(node);
        return null;
    }

    @Override
    public final Void
    visitForLoopActionNode(ForLoopActionNode node, Void argument) {
        visitForLoopActionNode(node);
        return null;
    }

    //region statement nodes

    @Override
    public final Void
    visitVarDeclarationStatementNode(VarDeclarationStatementNode node, Void argument) {
        visitVarDeclarationStatementNode(node);
        return null;
    }

    @Override
    public final Void
    visitVarAssignStatementNode(VarAssignStatementNode node, Void argument) {
        visitVarAssignStatementNode(node);
        return null;
    }

    @Override
    public final Void
    visitVarDeclAssignStatementNode(VarDeclAssignStatementNode node, Void argument) {
        visitVarDeclAssignStatementNode(node);
        return null;
    }

    @Override
    public final Void
    visitBlockStatementNode(BlockStatementNode node, Void argument) {
        visitBlockStatementNode(node);
        return null;
    }

    @Override
    public final Void
    visitBranchStatementNode(BranchStatementNode node, Void argument) {
        visitBranchStatementNode(node);
        return null;
    }

    @Override
    public final Void
    visitWhileLoopStatementNode(WhileLoopStatementNode node, Void argument) {
        visitWhileLoopStatementNode(node);
        return null;
    }

    @Override
    public final Void
    visitForLoopStatementNode(ForLoopStatementNode node, Void argument) {
        visitForLoopStatementNode(node);
        return null;
    }

    @Override
    public final Void
    visitExpressionStatementNode(ExpressionStatementNode node, Void argument) {
        visitExpressionStatementNode(node);
        return null;
    }

    @Override
    public final Void
    visitDumpStatementNode(DumpStatementNode node, Void argument) {
        visitDumpStatementNode(node);
        return null;
    }

    @Override
    public final Void
    visitReturnStatementNode(ReturnStatementNode node, Void argument) {
        visitReturnStatementNode(node);
        return null;
    }

    //endregion statement nodes

    //region expression nodes

    @Override
    public final Void
    visitParenExpressionNode(ParenExpressionNode node, Void argument) {
        visitParenExpressionNode(node);
        return null;
    }

    @Override
    public final Void
    visitAddExpressionNode(AddExpressionNode node, Void argument) {
        visitAddExpressionNode(node);
        return null;
    }

    @Override
    public final Void
    visitSubExpressionNode(SubExpressionNode node, Void argument) {
        visitSubExpressionNode(node);
        return null;
    }

    @Override
    public final Void
    visitMulExpressionNode(MulExpressionNode node, Void argument) {
        visitMulExpressionNode(node);
        return null;
    }

    @Override
    public final Void
    visitDivExpressionNode(DivExpressionNode node, Void argument) {
        visitDivExpressionNode(node);
        return null;
    }

    @Override
    public final Void
    visitModExpressionNode(ModExpressionNode node, Void argument) {
        visitModExpressionNode(node);
        return null;
    }

    @Override
    public final Void
    visitEqualsExpressionNode(EqualsExpressionNode node, Void argument) {
        visitEqualsExpressionNode(node);
        return null;
    }

    @Override
    public final Void
    visitNotEqualsExpressionNode(NotEqualsExpressionNode node, Void argument) {
        visitNotEqualsExpressionNode(node);
        return null;
    }

    @Override
    public final Void
    visitLtExpressionNode(LtExpressionNode node, Void argument) {
        visitLtExpressionNode(node);
        return null;
    }

    @Override
    public final Void
    visitGtExpressionNode(GtExpressionNode node, Void argument) {
        visitGtExpressionNode(node);
        return null;
    }

    @Override
    public final Void
    visitLtEqExpressionNode(LtEqExpressionNode node, Void argument) {
        visitLtEqExpressionNode(node);
        return null;
    }

    @Override
    public final Void
    visitGtEqExpressionNode(GtEqExpressionNode node, Void argument) {
        visitGtEqExpressionNode(node);
        return null;
    }

    @Override
    public final Void
    visitFuncCallExpressionNode(FuncCallExpressionNode node, Void argument) {
        visitFuncCallExpressionNode(node);
        return null;
    }

    @Override
    public final Void
    visitBoolLiteralExpressionNode(BoolLiteralExpressionNode node, Void argument) {
        visitBoolLiteralExpressionNode(node);
        return null;
    }

    @Override
    public final Void
    visitIdExpressionNode(IdExpressionNode node, Void argument) {
        visitIdExpressionNode(node);
        return null;
    }

    @Override
    public final Void
    visitIntExpressionNode(IntExpressionNode node, Void argument) {
        visitIntExpressionNode(node);
        return null;
    }

    //endregion expression nodes

    //endregion adapter

}
