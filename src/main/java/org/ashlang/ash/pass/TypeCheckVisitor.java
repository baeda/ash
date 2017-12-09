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
import org.ashlang.ash.symbol.Function;
import org.ashlang.ash.symbol.Symbol;
import org.ashlang.ash.type.Type;
import org.ashlang.ash.type.Types;

import java.util.List;

import static org.ashlang.ash.type.Types.*;

class TypeCheckVisitor extends ASTVoidBaseVisitor {

    private final ErrorHandler errorHandler;

    TypeCheckVisitor(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override
    protected void
    visitVarDeclarationNode(VarDeclarationNode node) {
        if (VOID.equals(node.getType())) {
            node.setType(INVALID);
        }
        if (INVALID.equals(node.getType())) {
            errorHandler.emitInvalidType(node.getTypeToken());
        }
    }

    @Override
    protected void
    visitVarAssignNode(VarAssignNode node) {
        visitChildren(node);

        if (node.getSymbol() == null) {
            return;
        }

        Type lhsType = node.getSymbol().getType();
        Type rhsType = node.getExpression().getType();

        if (INVALID.equals(rhsType) || INVALID.equals(lhsType)) {
            return;
        }

        if (!lhsType.equals(rhsType)) {
            errorHandler.emitTypeMismatch(node, rhsType, lhsType);
        }
    }

    @Override
    protected void
    visitFuncDeclarationNode(FuncDeclarationNode node) {
        visitChildren(node);

        Function func = node.getFunction();
        List<Symbol> params = func.getParameters();

        if ("main".equals(func.getIdentifier())) {
            if (!VOID.equals(func.getType())) {
                errorHandler.emitTypeMismatch(node, func.getType(), VOID);
            }
            if (!params.isEmpty()) {
                errorHandler.emitFunctionArgumentCountMismatch(
                    TokenRange.ofSymbols(params),
                    params.size(),
                    0
                );
            }
        }

        for (Symbol param : params) {
            if (INVALID.equals(param.getType())) {
                errorHandler.emitInvalidType(param.getDeclSite().getTypeToken());
            }
        }
    }

    @Override
    protected void
    visitFuncCallNode(FuncCallNode node) {
        visitChildren(node);

        Function func = node.getFunction();

        if (func == null) {
            // call to undeclared function
            return;
        }

        List<Symbol> params = func.getParameters();
        List<ArgumentNode> args = node.getArguments();

        if (args.size() != params.size()) {
            TokenRange pos = args.isEmpty()
                ? TokenRange.ofToken(node.getStopToken())
                : TokenRange.ofNodes(args);

            errorHandler.emitFunctionArgumentCountMismatch(
                pos,
                args.size(),
                params.size()
            );
        }

        int N = Math.min(params.size(), args.size());
        for (int i = 0; i < N; i++) {
            Symbol param = params.get(i);
            ExpressionNode expr = args.get(i).getExpression();

            Type want = param.getType();
            Type have = expr.getType();

            if (Types.anyInvalid(want, have)) {
                continue;
            }

            if (!have.equals(want)) {
                errorHandler.emitTypeMismatch(expr, have, want);
            }
        }
    }

    @Override
    protected void
    visitBranchNode(BranchNode node) {
        visitChildren(node);

        ExpressionNode expression = node.getExpression();
        Type type = expression.getType();
        if (Types.allValid(type) && !BOOL.equals(type)) {
            errorHandler.emitTypeMismatch(expression, type, BOOL);
        }
    }

    @Override
    protected void
    visitWhileLoopNode(WhileLoopNode node) {
        visitChildren(node);

        ExpressionNode expression = node.getExpression();
        Type type = expression.getType();
        if (Types.allValid(type) && !BOOL.equals(type)) {
            errorHandler.emitTypeMismatch(expression, type, BOOL);
        }
    }

    //region statement nodes

    @Override
    protected void
    visitReturnStatementNode(ReturnStatementNode node) {
        visitChildren(node);

        Type returnType = node.getFunction().getType();
        ExpressionNode expression = node.getExpression();

        if (VOID.equals(returnType) && expression == null) {
            // blank 'return;' from void function.
            // valid
            return;
        }

        Type exprType = expression.getType();

        if (INVALID.equals(exprType) || INVALID.equals(returnType)) {
            return;
        }

        if (!returnType.equals(exprType)) {
            errorHandler.emitTypeMismatch(
                node.getExpression(),
                exprType,
                returnType
            );
        }
    }

    //endregion statement nodes

}
