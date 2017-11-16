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
import org.ashlang.ash.err.ErrorHandler;
import org.ashlang.ash.type.Operator;
import org.ashlang.ash.type.OperatorMap;
import org.ashlang.ash.type.Type;

import static org.ashlang.ash.type.Operator.*;
import static org.ashlang.ash.type.Types.INVALID;
import static org.ashlang.ash.type.Types.allValid;

class TypeCheckVisitor extends ASTVoidBaseVisitor {

    private final ErrorHandler errorHandler;
    private final OperatorMap operatorMap;

    TypeCheckVisitor(ErrorHandler errorHandler, OperatorMap operatorMap) {
        this.errorHandler = errorHandler;
        this.operatorMap = operatorMap;
    }

    @Override
    protected void
    visitVarDeclarationNode(VarDeclarationNode node) {
        if (INVALID.equals(node.getType())) {
            errorHandler.emitInvalidType(node.getTypeToken());
        }
    }

    @Override
    protected void visitVarAssignNode(VarAssignNode node) {
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

    //region Expression nodes

    @Override
    protected void visitParenExpressionNode(ParenExpressionNode node) {
        visitChildren(node);
        Type type = node.getExpression().getType();
        node.setType(type);
    }

    @Override
    protected void visitAddExpressionNode(AddExpressionNode node) {
        setResultTypeOfOperation(node, ADD);
    }

    @Override
    protected void visitSubExpressionNode(SubExpressionNode node) {
        setResultTypeOfOperation(node, SUB);
    }

    @Override
    protected void visitMulExpressionNode(MulExpressionNode node) {
        setResultTypeOfOperation(node, MUL);
    }

    @Override
    protected void visitDivExpressionNode(DivExpressionNode node) {
        setResultTypeOfOperation(node, DIV);
    }

    @Override
    protected void visitModExpressionNode(ModExpressionNode node) {
        setResultTypeOfOperation(node, MOD);
    }

    //endregion Expression nodes

    private void
    setResultTypeOfOperation(BinaryExpressionNode node, Operator op) {
        visitChildren(node);
        Type lhs = node.getLhs().getType();
        Type rhs = node.getRhs().getType();
        Type res = operatorMap.getResultOf(lhs, op, rhs);
        if (allValid(lhs, rhs) && INVALID.equals(res)) {
            errorHandler.emitInvalidOperator(node.getOp(), lhs, rhs);
        }
        node.setType(res);
    }

}
