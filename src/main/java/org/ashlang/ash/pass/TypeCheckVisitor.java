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

class TypeCheckVisitor extends ASTBaseVisitor<Void, Void> {

    private final ErrorHandler errorHandler;
    private final OperatorMap operatorMap;

    TypeCheckVisitor(ErrorHandler errorHandler, OperatorMap operatorMap) {
        this.errorHandler = errorHandler;
        this.operatorMap = operatorMap;
    }

    @Override
    public Void
    visitVarDeclarationNode(VarDeclarationNode node, Void argument) {
        if (INVALID.equals(node.getType())) {
            errorHandler.emitInvalidType(node.getTypeToken());
        }
        return null;
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

    //endregion Expression nodes

    private void
    setResultTypeOfOperation(BinaryExpressionNode node, Operator op) {
        visitChildren(node, null);
        Type lhs = node.getLhs().getType();
        Type rhs = node.getRhs().getType();
        Type res = operatorMap.getResultOf(lhs, op, rhs);
        if (allValid(lhs, rhs) && INVALID.equals(res)) {
            errorHandler.emitInvalidOperator(node.getOp(), lhs, rhs);
        }
        node.setType(res);
    }

}
