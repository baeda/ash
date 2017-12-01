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

public class ForLoopNode extends ASTNode {

    private final DeclarationNode declaration;
    private final ExpressionNode condition;
    private final ForLoopActionNode action;
    private final StatementNode body;

    public ForLoopNode(
        Token startToken,
        DeclarationNode declaration,
        ExpressionNode condition,
        ForLoopActionNode action,
        StatementNode body,
        SourceProvider sourceProvider
    ) {
        super(startToken, body.getStopToken(), sourceProvider);
        this.declaration = declaration;
        this.condition = condition;
        this.action = action;
        this.body = body;
    }

    public DeclarationNode getDeclaration() {
        return declaration;
    }

    public ExpressionNode getCondition() {
        return condition;
    }

    public ForLoopActionNode getAction() {
        return action;
    }

    public StatementNode getBody() {
        return body;
    }

    @Override
    public <T, A> T accept(ASTVisitor<T, A> visitor, A argument) {
        return visitor.visitForLoopNode(this, argument);
    }

}
