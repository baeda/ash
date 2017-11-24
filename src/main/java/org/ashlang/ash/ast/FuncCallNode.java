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
import org.ashlang.ash.symbol.Function;

import java.util.List;

public class FuncCallNode extends ASTNode {

    private final List<ArgumentNode> arguments;

    private Function function;

    public FuncCallNode(
        Token startToken, Token stopToken,
        List<ArgumentNode> arguments,
        SourceProvider sourceProvider
    ) {
        super(startToken, stopToken, sourceProvider);

        this.arguments = arguments;
    }

    public Token getIdentifierToken() {
        return getStartToken();
    }

    public List<ArgumentNode> getArguments() {
        return arguments;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    @Override
    public <T, A> T accept(ASTVisitor<T, A> visitor, A argument) {
        return visitor.visitFuncCallNode(this, argument);
    }

}
