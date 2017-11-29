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
import org.ashlang.ash.type.Type;

import java.util.List;

public class FuncDeclarationNode extends ASTNode {

    private final Token identifierToken;
    private final Token typeToken;
    private final List<ParamDeclarationNode> params;
    private final BlockNode body;

    private Type type;
    private Function function;

    public FuncDeclarationNode(
        Token startToken,
        Token identifierToken,
        Token typeToken,
        List<ParamDeclarationNode> params, BlockNode body,
        SourceProvider sourceProvider
    ) {
        super(
            startToken,
            body.getStopToken(),
            sourceProvider
        );
        this.identifierToken = identifierToken;
        this.typeToken = typeToken;
        this.params = params;
        this.body = body;
    }

    public Token
    getIdentifierToken() {
        return identifierToken;
    }

    public Token
    getTypeToken() {
        return typeToken;
    }

    public List<ParamDeclarationNode>
    getParams() {
        return params;
    }

    public BlockNode
    getBody() {
        return body;
    }

    public Type
    getType() {
        return type;
    }

    public void
    setType(Type type) {
        this.type = type;
    }

    public Function
    getFunction() {
        return function;
    }

    public void
    setFunction(Function function) {
        this.function = function;
    }

    @Override
    public <T, A> T
    accept(ASTVisitor<T, A> visitor, A argument) {
        return visitor.visitFuncDeclarationNode(this, argument);
    }

}
