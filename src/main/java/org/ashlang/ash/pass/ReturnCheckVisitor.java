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
import org.ashlang.ash.type.Types;

import java.util.HashSet;
import java.util.Set;

class ReturnCheckVisitor extends ASTBaseVisitor<Void, Function> {

    private final ErrorHandler errorHandler;
    private final Set<Function> unreachableErrors;

    ReturnCheckVisitor(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        unreachableErrors = new HashSet<>();
    }

    @Override
    public Void
    visitFuncDeclarationNode(FuncDeclarationNode node, Function unused) {
        Function func = node.getFunction();
        func.setReturns(false);
        visitChildren(node, func);

        if (!Types.VOID.equals(func.getType()) && !func.returns()) {
            errorHandler.emitMissingReturnStatement(node.getStopToken());
        }
        return null;
    }

    @Override
    public Void
    visitBranchNode(BranchNode node, Function func) {
        boolean returns = func.returns();

        visit(node.getOnTrue(), func);
        boolean returnsOnTrue = func.returns();

        func.setReturns(returns);

        visit(node.getOnFalse(), func);
        boolean returnsOnFalse = func.returns();

        func.setReturns(returnsOnTrue && returnsOnFalse);

        return null;
    }

    //region statement nodes

    @Override
    public Void
    visitVarDeclarationStatementNode(VarDeclarationStatementNode node, Function func) {
        return visitStatementNode(node, func);
    }

    @Override
    public Void
    visitVarAssignStatementNode(VarAssignStatementNode node, Function func) {
        return visitStatementNode(node, func);
    }

    @Override
    public Void
    visitBlockStatementNode(BlockStatementNode node, Function func) {
        return visitStatementNode(node, func);
    }

    @Override
    public Void
    visitBranchStatementNode(BranchStatementNode node, Function func) {
        return visitStatementNode(node, func);
    }

    @Override
    public Void
    visitExpressionStatementNode(ExpressionStatementNode node, Function func) {
        return visitStatementNode(node, func);
    }

    @Override
    public Void
    visitDumpStatementNode(DumpStatementNode node, Function func) {
        return visitStatementNode(node, func);
    }

    @Override
    public Void
    visitReturnStatementNode(ReturnStatementNode node, Function func) {
        visitStatementNode(node, func);
        func.setReturns(true);
        return null;
    }

    private Void
    visitStatementNode(StatementNode node, Function func) {
        if (func.returns() && !unreachableErrors.contains(func)) {
            errorHandler.emitUnreachableStatement(node);
            unreachableErrors.add(func);
        }
        return visitChildren(node, func);
    }

    //endregion statement nodes

}
