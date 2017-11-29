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
import org.ashlang.ash.type.IntType;
import org.ashlang.ash.type.Type;
import org.ashlang.ash.type.Types;
import org.ashlang.ash.type.UntypedInt;

import java.util.List;

class UntypedIntSolidifyVisitor extends ASTVoidBaseVisitor {

    private final ErrorHandler errorHandler;

    UntypedIntSolidifyVisitor(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override
    protected void
    visitVarAssignNode(VarAssignNode node) {
        visitChildren(node);

        if (node.getSymbol() == null) {
            return;
        }

        solidifyUntypedInt(
            node.getSymbol().getType(),
            node.getExpression()
        );
    }

    @Override
    protected void
    visitFuncCallNode(FuncCallNode node) {
        visitChildren(node);

        Function func = node.getFunction();
        if (func == null) {
            // call of undeclared function
            return;
        }

        List<Symbol> params = func.getParameters();
        List<ArgumentNode> args = node.getArguments();

        int N = Math.min(params.size(), args.size());
        for (int i = 0; i < N; i++) {
            Symbol param = params.get(i);
            ArgumentNode arg = args.get(i);

            solidifyUntypedIntRight(
                param.getType(),
                arg.getExpression()
            );
        }
    }

    //region statement nodes

    @Override
    protected void
    visitDumpStatementNode(DumpStatementNode node) {
        visitChildren(node);

        // resolve parenthesized integer constants, i.e. dump (12);
        ExpressionNode expression = node.getExpression();
        while (expression instanceof ParenExpressionNode) {
            expression = ((ParenExpressionNode) expression).getExpression();
        }

        Type type = expression.getType();
        if (!(type instanceof UntypedInt)) {
            return;
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
    }

    //endregion statement nodes

    //region expression nodes

    @Override
    protected void
    visitParenExpressionNode(ParenExpressionNode node) {
        visitChildren(node);

        Type type = node.getExpression().getType();
        node.setType(type);
    }

    @Override
    protected void
    visitAddExpressionNode(AddExpressionNode node) {
        visitChildren(node);
        solidifyUntypedInt(
            node.getLhs(),
            node.getRhs()
        );
    }

    @Override
    protected void
    visitSubExpressionNode(SubExpressionNode node) {
        visitChildren(node);
        solidifyUntypedInt(
            node.getLhs(),
            node.getRhs()
        );
    }

    @Override
    protected void
    visitMulExpressionNode(MulExpressionNode node) {
        visitChildren(node);
        solidifyUntypedInt(
            node.getLhs(),
            node.getRhs()
        );
    }

    @Override
    protected void
    visitDivExpressionNode(DivExpressionNode node) {
        visitChildren(node);
        solidifyUntypedInt(
            node.getLhs(),
            node.getRhs()
        );
    }

    @Override
    protected void
    visitModExpressionNode(ModExpressionNode node) {
        visitChildren(node);
        solidifyUntypedInt(
            node.getLhs(),
            node.getRhs()
        );
    }

    @Override
    protected void
    visitEqualsExpressionNode(EqualsExpressionNode node) {
        visitChildren(node);
        solidifyUntypedInt(
            node.getLhs(),
            node.getRhs()
        );
    }

    @Override
    protected void
    visitNotEqualsExpressionNode(NotEqualsExpressionNode node) {
        visitChildren(node);
        solidifyUntypedInt(
            node.getLhs(),
            node.getRhs()
        );
    }

    //endregion expression nodes

    //region statement nodes

    @Override
    protected void
    visitReturnStatementNode(ReturnStatementNode node) {
        visitChildren(node);

        ExpressionNode expression = node.getExpression();
        if (expression == null) {
            // empty return statement, nothing to do.
            return;
        }

        solidifyUntypedIntRight(
            node.getFunction().getType(),
            expression
        );
    }


    //endregion statement nodes

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

    private void
    solidify(ExpressionNode node, UntypedInt source, IntType target) {
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
