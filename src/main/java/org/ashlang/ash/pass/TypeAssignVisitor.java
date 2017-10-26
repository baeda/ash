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
import static org.ashlang.ash.type.Type.INT;
import static org.ashlang.ash.type.Type.INVALID;

class TypeAssignVisitor extends ASTBaseVisitor<Void, Void> {

    private final ErrorHandler errorHandler;
    private final OperatorMap operatorMap;

    TypeAssignVisitor(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;

        operatorMap = new OperatorMap();
    }

    //region Expression nodes

    @Override
    public Void visitParenExpressionNode(ParenExpressionNode node, Void argument) {
        visitChildren(node, null);
        Type type = node.getExpression().getType();
        node.setType(type);
        return null;
    }

    @Override
    public Void visitAddExpressionNode(AddExpressionNode node, Void argument) {
        setResultTypeOfOperation(node, ADD);
        return null;
    }

    @Override
    public Void visitSubExpressionNode(SubExpressionNode node, Void argument) {
        setResultTypeOfOperation(node, SUB);
        return null;
    }

    @Override
    public Void visitMulExpressionNode(MulExpressionNode node, Void argument) {
        setResultTypeOfOperation(node, MUL);
        return null;
    }

    @Override
    public Void visitDivExpressionNode(DivExpressionNode node, Void argument) {
        setResultTypeOfOperation(node, DIV);
        return null;
    }

    @Override
    public Void visitModExpressionNode(ModExpressionNode node, Void argument) {
        setResultTypeOfOperation(node, MOD);
        return null;
    }

    @Override
    public Void visitIntExpressionNode(IntExpressionNode node, Void argument) {
        node.setType(INT);
        return null;
    }

    //endregion Expression nodes

    private void
    setResultTypeOfOperation(BinaryExpressionNode node, Operator op) {
        visitChildren(node, null);
        Type lhs = node.getLhs().getType();
        Type rhs = node.getRhs().getType();
        Type res = operatorMap.getResultOf(lhs, op, rhs);
        if (Type.allValid(lhs, rhs) && res == INVALID) {
            errorHandler.emitInvalidOperator(node.getOp(), lhs, rhs);
        }
        node.setType(res);
    }

}
