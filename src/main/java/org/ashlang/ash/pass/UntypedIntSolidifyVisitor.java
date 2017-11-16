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
import org.ashlang.ash.type.IntType;
import org.ashlang.ash.type.Type;
import org.ashlang.ash.type.Types;
import org.ashlang.ash.type.UntypedInt;

class UntypedIntSolidifyVisitor extends ASTBaseVisitor<Void, Type> {

    private final ErrorHandler errorHandler;

    UntypedIntSolidifyVisitor(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override
    public Void visitVarAssignNode(VarAssignNode node, Type argument) {
        visitChildren(node, null);

        if (node.getSymbol() == null) {
            return null;
        }

        solidifyUntypedInt(
            node.getSymbol().getType(),
            node.getExpression()
        );

        return null;
    }

    @Override
    public Void visitDumpStatementNode(DumpStatementNode node, Type argument) {
        visitChildren(node, null);

        // resolve parenthesized integer constants, i.e. dump (12);
        ExpressionNode expression = node.getExpression();
        while (expression instanceof ParenExpressionNode) {
            expression = ((ParenExpressionNode) expression).getExpression();
        }

        Type type = expression.getType();
        if (!(type instanceof UntypedInt)) {
            return null;
        }

        UntypedInt uti = (UntypedInt) type;
        Type result = Types.INVALID;

        // we resolve direct int expressions to 'dump' to either be
        // * i64 for [-9223372036854775808; -1]
        // * u64 for [0; 18446744073709551615]
        if (uti.isNegative()) {
            // -42
            if (uti.getBitSize() < 64) {
                result = Types.I64;
            } else {
                errorHandler.emitIntConstantUnderflow(
                    node.getExpression(),
                    Types.I64
                );
            }
        } else {
            // 42
            if (uti.getBitSize() <= 64) {
                result = Types.U64;
            } else {
                errorHandler.emitIntConstantOverflow(
                    node.getExpression(),
                    Types.U64
                );
            }
        }

        expression.setType(result);

        return null;
    }

    //region Expression nodes


    @Override
    public Void
    visitParenExpressionNode(ParenExpressionNode node, Type argument) {
        visitChildren(node, null);

        Type type = node.getExpression().getType();
        node.setType(type);

        return null;
    }

    @Override
    public Void visitAddExpressionNode(AddExpressionNode node, Type argument) {
        visitChildren(node, null);

        solidifyUntypedInt(
            node.getLhs(),
            node.getRhs()
        );

        return null;
    }

    @Override
    public Void visitSubExpressionNode(SubExpressionNode node, Type argument) {
        visitChildren(node, null);

        solidifyUntypedInt(
            node.getLhs(),
            node.getRhs()
        );

        return null;
    }

    @Override
    public Void visitMulExpressionNode(MulExpressionNode node, Type argument) {
        visitChildren(node, null);

        solidifyUntypedInt(
            node.getLhs(),
            node.getRhs()
        );

        return null;
    }

    @Override
    public Void visitDivExpressionNode(DivExpressionNode node, Type argument) {
        visitChildren(node, null);

        solidifyUntypedInt(
            node.getLhs(),
            node.getRhs()
        );

        return null;
    }

    @Override
    public Void visitModExpressionNode(ModExpressionNode node, Type argument) {
        visitChildren(node, null);

        solidifyUntypedInt(
            node.getLhs(),
            node.getRhs()
        );

        return null;
    }

    //endregion Expression nodes

    private void
    solidifyUntypedInt(Type lhs, ExpressionNode rhsNode) {
        solidifyUntypedIntRight(lhs, rhsNode);
    }

    private void
    solidifyUntypedInt(ExpressionNode lhsNode, ExpressionNode rhsNode) {
        Type lhs = lhsNode.getType();
        Type rhs = rhsNode.getType();

        solidifyUntypedIntLeft(rhs, lhsNode);
        solidifyUntypedIntRight(lhs, rhsNode);
    }

    private void
    solidifyUntypedIntRight(Type lhs, ExpressionNode rhsNode) {
        Type rhs = rhsNode.getType();

        if (!(lhs instanceof IntType)) {
            return;
        }
        if (!(rhs instanceof UntypedInt)) {
            return;
        }

        solidify(rhsNode, (UntypedInt) rhs, (IntType) lhs);
    }

    private void
    solidifyUntypedIntLeft(Type rhs, ExpressionNode lhsNode) {
        Type lhs = lhsNode.getType();

        if (!(lhs instanceof UntypedInt)) {
            return;
        }
        if (!(rhs instanceof IntType)) {
            return;
        }

        solidify(lhsNode, (UntypedInt) lhs, (IntType) rhs);
    }

    private void solidify(ExpressionNode node, UntypedInt source, IntType target) {
        if (source.isNegative()) {
            if (target.isSigned()) {
                // a : i32 = -45;
                // possible
                if (source.getBitSize() >= target.getBitSize()) {
                    // a : i32 = -2147483649;
                    // underflow
                    errorHandler.emitIntConstantUnderflow(node, target);
                }
            } else {
                // a : u32 = -45;
                // underflow
                errorHandler.emitIntConstantUnderflow(node, target);
            }
        } else {
            if (target.isSigned()) {
                // a : i32 = 45;
                // possible
                if (source.getBitSize() >= target.getBitSize()) {
                    // a : i32 = 2147483648;
                    // overflow
                    errorHandler.emitIntConstantOverflow(node, target);
                }
            } else {
                // a : u32 = 45;
                // possible
                if (source.getBitSize() > target.getBitSize()) {
                    // a : u32 = 4294967296;
                    // overflow
                    errorHandler.emitIntConstantOverflow(node, target);
                }
            }
        }

        // always assign a valid type, to avoid continuous errors
        node.setType(target);
    }

}
