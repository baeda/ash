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

public class NotEqualsExpressionNode extends BinaryExpressionNode {

    public NotEqualsExpressionNode(ExpressionNode lhs, ExpressionNode rhs, Token op,
                                   SourceProvider sourceProvider) {
        super(lhs, rhs, op, sourceProvider);
    }

    @Override
    public <T, A> T accept(ASTVisitor<T, A> visitor, A argument) {
        return visitor.visitNotEqualsExpressionNode(this, argument);
    }

}
