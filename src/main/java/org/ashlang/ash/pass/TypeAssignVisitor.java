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

import org.ashlang.ash.ast.IdExpressionNode;
import org.ashlang.ash.ast.IntExpressionNode;
import org.ashlang.ash.ast.VarDeclarationNode;
import org.ashlang.ash.ast.visitor.ASTVoidBaseVisitor;
import org.ashlang.ash.symbol.Symbol;
import org.ashlang.ash.symbol.SymbolTable;
import org.ashlang.ash.type.Type;
import org.ashlang.ash.type.TypeMap;
import org.ashlang.ash.type.UntypedInt;

import java.math.BigInteger;

class TypeAssignVisitor extends ASTVoidBaseVisitor {

    private final SymbolTable symbolTable;
    private final TypeMap typeMap;

    TypeAssignVisitor(SymbolTable symbolTable, TypeMap typeMap) {
        this.symbolTable = symbolTable;
        this.typeMap = typeMap;
    }

    @Override
    protected void visitVarDeclarationNode(VarDeclarationNode node) {
        String typeString = node.getTypeToken().getText();
        Type type = typeMap.resolve(typeString);
        node.setType(type);
    }

    //region Expression nodes

    @Override
    protected void visitIdExpressionNode(IdExpressionNode node) {
        String identifier = node.getValue().getText();
        Symbol symbol = symbolTable.getDeclaredSymbol(identifier);
        node.setType(symbol.getType());
    }

    @Override
    protected void visitIntExpressionNode(IntExpressionNode node) {
        BigInteger value = new BigInteger(node.getValueToken().getText());
        node.setType(new UntypedInt(value));
        node.setValue(value);
    }

    //endregion Expression nodes

}
