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

import org.ashlang.ash.ast.ASTBaseVisitor;
import org.ashlang.ash.ast.IdExpressionNode;
import org.ashlang.ash.ast.IntExpressionNode;
import org.ashlang.ash.ast.VarDeclarationNode;
import org.ashlang.ash.symbol.Symbol;
import org.ashlang.ash.symbol.SymbolTable;
import org.ashlang.ash.type.Type;
import org.ashlang.ash.type.TypeMap;

import static org.ashlang.ash.type.Types.I32;

class TypeAssignVisitor extends ASTBaseVisitor<Void, Void> {

    private final SymbolTable symbolTable;
    private final TypeMap typeMap;

    TypeAssignVisitor(SymbolTable symbolTable, TypeMap typeMap) {
        this.symbolTable = symbolTable;
        this.typeMap = typeMap;
    }

    @Override
    public Void visitVarDeclarationNode(VarDeclarationNode node, Void argument) {
        String typeString = node.getTypeToken().getText();
        Type type = typeMap.resolve(typeString);
        node.setType(type);
        return null;
    }

    //region Expression nodes

    @Override
    public Void visitIdExpressionNode(IdExpressionNode node, Void argument) {
        String identifier = node.getValue().getText();
        Symbol symbol = symbolTable.getDeclaredSymbol(identifier);
        node.setType(symbol.getType());
        return null;
    }

    @Override
    public Void visitIntExpressionNode(IntExpressionNode node, Void argument) {
        node.setType(I32);
        return null;
    }

    //endregion Expression nodes

}
