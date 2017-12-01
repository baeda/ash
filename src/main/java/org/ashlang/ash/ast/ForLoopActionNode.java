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

package org.ashlang.ash.ast;

import org.ashlang.ash.ast.visitor.ASTVisitor;

import java.util.function.Function;

public class ForLoopActionNode extends ASTNode {

    private final VarAssignNode varAssign;
    private final ExpressionNode expression;

    public ForLoopActionNode(
        VarAssignNode varAssign,
        ExpressionNode expression,
        SourceProvider sourceProvider
    ) {
        super(
            determineToken(varAssign, expression, ASTNode::getStartToken),
            determineToken(varAssign, expression, ASTNode::getStopToken),
            sourceProvider
        );
        this.varAssign = varAssign;
        this.expression = expression;
    }

    public VarAssignNode getVarAssign() {
        return varAssign;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public <T, A> T accept(ASTVisitor<T, A> visitor, A argument) {
        return visitor.visitForLoopActionNode(this, argument);
    }

    private static Token
    determineToken(
        ASTNode left,
        ASTNode right,
        Function<ASTNode, Token> tokenExtractor
    ) {
        if (left != null) {
            return tokenExtractor.apply(left);
        }
        if (right != null) {
            return tokenExtractor.apply(right);
        }

        throw new IllegalStateException();
    }
}
