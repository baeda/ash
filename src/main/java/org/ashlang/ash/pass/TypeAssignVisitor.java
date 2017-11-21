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
import org.ashlang.ash.symbol.Function;
import org.ashlang.ash.symbol.Symbol;
import org.ashlang.ash.type.Type;
import org.ashlang.ash.type.TypeMap;
import org.ashlang.ash.type.Types;
import org.ashlang.ash.type.UntypedInt;
import org.ashlang.ash.util.Defer;

import java.math.BigInteger;

class TypeAssignVisitor extends ASTVoidBaseVisitor {

    private final TypeMap typeMap;
    private final Defer defer;

    TypeAssignVisitor(TypeMap typeMap) {
        this.typeMap = typeMap;

        defer = new Defer();
    }

    @Override
    protected void
    visitFileNode(FileNode node) {
        visitChildren(node);

        defer.runAll();
    }

    @Override
    protected void
    visitFuncDeclarationNode(FuncDeclarationNode node) {
        String typeString = node.getTypeToken().getText();
        Type type = typeMap.resolve(typeString);

        node.setType(type);
        // we first want to record and type-assign all function
        // signatures and then resolve their implementations
        defer.record(() -> visitChildren(node));
    }

    @Override
    protected void
    visitVarDeclarationNode(VarDeclarationNode node) {
        String typeString = node.getTypeToken().getText();
        Type type = typeMap.resolve(typeString);
        node.setType(type);
    }

    //region expression nodes


    @Override
    protected void
    visitFuncCallExpressionNode(FuncCallExpressionNode node) {
        visitChildren(node);

        Function func = node.getFuncCall().getFunction();
        if (func == null) {
            return;
        }

        node.setType(func.getType());
    }

    @Override
    protected void
    visitBoolLiteralExpressionNode(BoolLiteralExpressionNode node) {
        boolean value = Boolean.parseBoolean(node.getValueToken().getText());
        node.setType(Types.BOOL);
        node.setValue(value);
    }

    @Override
    protected void
    visitIdExpressionNode(IdExpressionNode node) {
        Symbol symbol = node.getSymbol();

        if (symbol == null) {
            return;
        }

        node.setType(symbol.getType());
    }

    @Override
    protected void
    visitIntExpressionNode(IntExpressionNode node) {
        BigInteger value = new BigInteger(node.getValueToken().getText());
        node.setType(new UntypedInt(value));
        node.setValue(value);
    }

    //endregion expression nodes

}
