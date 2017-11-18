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
    protected abstract void visitVarDeclarationNode(VarDeclarationNode node);
    protected abstract void visitVarAssignNode(VarAssignNode node);
    protected abstract void visitBlockNode(BlockNode node);

    //region statement nodes

    protected abstract void visitVarDeclarationStatementNode(VarDeclarationStatementNode node);
    protected abstract void visitVarAssignStatementNode(VarAssignStatementNode node);
    protected abstract void visitBlockStatementNode(BlockStatementNode node);
    protected abstract void visitDumpStatementNode(DumpStatementNode node);

    //endregion statement nodes

    //region expression nodes

    protected abstract void visitParenExpressionNode(ParenExpressionNode node);
    protected abstract void visitAddExpressionNode(AddExpressionNode node);
    protected abstract void visitSubExpressionNode(SubExpressionNode node);
    protected abstract void visitMulExpressionNode(MulExpressionNode node);
    protected abstract void visitDivExpressionNode(DivExpressionNode node);
    protected abstract void visitModExpressionNode(ModExpressionNode node);
    protected abstract void visitIdExpressionNode(IdExpressionNode node);
    protected abstract void visitIntExpressionNode(IntExpressionNode node);

    //endregion expression nodes

    protected void visitChildren(ASTNode node) {
        visitChildren(node, null);
    }

    public void visit(ASTNode node) {
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
    public final Void visitBlockNode(BlockNode node, Void argument) {
        visitBlockNode(node);
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
    visitBlockStatementNode(BlockStatementNode node, Void argument) {
        visitBlockStatementNode(node);
        return null;
    }

    @Override
    public final Void
    visitDumpStatementNode(DumpStatementNode node, Void argument) {
        visitDumpStatementNode(node);
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
