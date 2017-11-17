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

    protected void visitFileNode(FileNode node) {
        visitChildren(node);
    }

    protected void visitVarDeclarationNode(VarDeclarationNode node) {
        visitChildren(node);
    }

    protected void visitVarAssignNode(VarAssignNode node) {
        visitChildren(node);
    }

    @Override
    protected void visitBlockNode(BlockNode node) {
        visitChildren(node);
    }

    //region statement nodes

    protected void visitVarDeclarationStatementNode(VarDeclarationStatementNode node) {
        visitChildren(node);
    }

    protected void visitVarAssignStatementNode(VarAssignStatementNode node) {
        visitChildren(node);
    }

    @Override
    protected void visitBlockStatementNode(BlockStatementNode node) {
        visitChildren(node);
    }

    protected void visitDumpStatementNode(DumpStatementNode node) {
        visitChildren(node);
    }

    //endregion statement nodes

    //region expression nodes

    protected void visitParenExpressionNode(ParenExpressionNode node) {
        visitChildren(node);
    }

    protected void visitAddExpressionNode(AddExpressionNode node) {
        visitChildren(node);
    }

    protected void visitSubExpressionNode(SubExpressionNode node) {
        visitChildren(node);
    }

    protected void visitMulExpressionNode(MulExpressionNode node) {
        visitChildren(node);
    }

    protected void visitDivExpressionNode(DivExpressionNode node) {
        visitChildren(node);
    }

    protected void visitModExpressionNode(ModExpressionNode node) {
        visitChildren(node);
    }

    protected void visitIdExpressionNode(IdExpressionNode node) {
        visitChildren(node);
    }

    protected void visitIntExpressionNode(IntExpressionNode node) {
        visitChildren(node);
    }

}
