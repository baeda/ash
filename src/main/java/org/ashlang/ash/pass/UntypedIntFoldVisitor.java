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
import org.ashlang.ash.type.Type;
import org.ashlang.ash.type.UntypedInt;

import java.math.BigInteger;
import java.util.function.BinaryOperator;

class UntypedIntFoldVisitor extends ASTVoidBaseVisitor {

    private final ErrorHandler errorHandler;

    UntypedIntFoldVisitor(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    //region Expression nodes

    @Override
    protected void visitParenExpressionNode(ParenExpressionNode node) {
        visitChildren(node);

        Type type = node.getExpression().getType();
        if (!(type instanceof UntypedInt)) {
            return;
        }

        UntypedInt uti = (UntypedInt) type;
        replaceWithIntExpression(node, uti);
    }

    @Override
    protected void visitAddExpressionNode(AddExpressionNode node) {
        visitChildren(node);
        resolveAndFoldTree(node, BigInteger::add);
    }

    @Override
    protected void visitSubExpressionNode(SubExpressionNode node) {
        visitChildren(node);
        resolveAndFoldTree(node, BigInteger::subtract);
    }

    @Override
    protected void visitMulExpressionNode(MulExpressionNode node) {
        visitChildren(node);
        resolveAndFoldTree(node, BigInteger::multiply);
    }

    @Override
    protected void visitDivExpressionNode(DivExpressionNode node) {
        visitChildren(node);
        resolveAndFoldTree(node, (lhs, rhs) -> {
            if (rhs.equals(BigInteger.ZERO)) {
                errorHandler.emitDivisionByZero(node);
                // return valid value to avoid continuous errors
                return BigInteger.ONE;
            }
            return lhs.divide(rhs);
        });
    }

    @Override
    protected void visitModExpressionNode(ModExpressionNode node) {
        visitChildren(node);
        resolveAndFoldTree(node, BigInteger::remainder);
    }

    //endregion Expression nodes

    private void resolveAndFoldTree(BinaryExpressionNode node,
                                    BinaryOperator<BigInteger> operator) {
        Type lhsType = node.getLhs().getType();
        Type rhsType = node.getRhs().getType();

        if (!(lhsType instanceof UntypedInt)) {
            return;
        }
        if (!(rhsType instanceof UntypedInt)) {
            return;
        }

        BigInteger lhsValue = ((UntypedInt) lhsType).getValue();
        BigInteger rhsValue = ((UntypedInt) rhsType).getValue();
        BigInteger newValue = operator.apply(lhsValue, rhsValue);
        UntypedInt resolvedType = new UntypedInt(newValue);

        replaceWithIntExpression(node, resolvedType);
    }

    private void replaceWithIntExpression(ExpressionNode node, UntypedInt type) {
        IntExpressionNode replacementNode = new IntExpressionNode(
            node.getStartToken(),
            node.getStopToken(),
            node.getSourceProvider()
        );
        replacementNode.setType(type);
        replacementNode.setValue(type.getValue());

        // replace 'this' node on the parent with the replacement
        node.replaceWith(replacementNode);
    }

}
