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
import org.ashlang.ash.ast.visitor.ASTVoidBaseVisitor;
import org.ashlang.ash.err.ErrorHandler;
import org.ashlang.ash.symbol.Symbol;

class StatementCheckPass extends ASTVoidBaseVisitor {

    private final ErrorHandler errorHandler;

    public StatementCheckPass(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override
    protected void visitBranchNode(BranchNode node) {
        visitChildren(node);

        StatementNode onTrue = node.getOnTrue();
        if (onTrue instanceof VarDeclarationStatementNode) {
            markSymbolAsValidAndEmitError(onTrue);
        }

        StatementNode onFalse = node.getOnFalse();
        if (onFalse instanceof VarDeclarationStatementNode) {
            markSymbolAsValidAndEmitError(onFalse);
        }
    }

    @Override
    protected void
    visitWhileLoopNode(WhileLoopNode node) {
        visitChildren(node);

        StatementNode body = node.getBody();
        if (body instanceof VarDeclarationStatementNode) {
            markSymbolAsValidAndEmitError(body);
        }
    }

    private void
    markSymbolAsValidAndEmitError(StatementNode onTrue) {
        VarDeclarationStatementNode varDeclStmt =
            (VarDeclarationStatementNode) onTrue;

        Symbol symbol = varDeclStmt.getVarDeclaration().getSymbol();
        // To avoid later errors like 'symbol never used'
        // or 'symbol not initialized', we set the corresponding flags here
        symbol.use();
        symbol.initialize();
        errorHandler.emitDeclarationNotAllowed(varDeclStmt);
    }

    @Override
    protected void
    visitExpressionStatementNode(ExpressionStatementNode node) {
        visitChildren(node);

        ExpressionNode expression = node.getExpression();
        if (!(expression instanceof FuncCallExpressionNode)) {
            errorHandler.emitIllegalStatement(node);
        }
    }

}
