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

package org.ashlang.ash.codegen;

import org.ashlang.ash.ast.*;
import org.ashlang.ash.symbol.Symbol;
import org.ashlang.ash.type.Type;

class C11Visitor implements ASTVisitor<String, Object> {

    private final C11TypeMap typeMap;

    C11Visitor() {
        typeMap = new C11TypeMap();
    }

    @Override
    public String visitFileNode(FileNode node, Object argument) {
        return String.join(
            "\n",
            "#include <stdio.h>",
            "#include <stdint.h>",
            "int main(int argc, char **argv) {",
            visitChildren(node, argument),
            "  return 0;",
            "}");
    }

    @Override
    public String visitVarDeclarationNode(VarDeclarationNode node, Object argument) {
        Symbol symbol = node.getSymbol();
        String cType = typeMap.get(symbol.getType());
        return cType + " " + symbol.getIdentifier();
    }

    @Override
    public String visitVarAssignNode(VarAssignNode node, Object argument) {
        Symbol symbol = node.getSymbol();
        String expression = visit(node.getExpression(), argument);
        return symbol.getIdentifier() + " = " + expression;
    }

    //region Statement nodes

    @Override
    public String
    visitVarDeclarationStatementNode(VarDeclarationStatementNode node,
                                     Object argument) {
        return visitChildren(node, argument) + ";";
    }

    @Override
    public String
    visitVarAssignStatementNode(VarAssignStatementNode node, Object argument) {
        return visitChildren(node, argument) + ";";
    }

    @Override
    public String visitDumpStatementNode(DumpStatementNode node, Object argument) {
        String expression = visitChildren(node, argument);
        Type type = node.getExpression().getType();
        String cType = typeMap.get(type);
        return "printf(\"%d\", (" + cType + ") " + expression + ");";
    }

    //endregion Statement nodes

    //region Expression nodes

    @Override
    public String visitParenExpressionNode(ParenExpressionNode node, Object argument) {
        return "(" + visitChildren(node, argument) + ")";
    }

    @Override
    public String visitAddExpressionNode(AddExpressionNode node, Object argument) {
        String lhs = visit(node.getLhs(), argument);
        String rhs = visit(node.getRhs(), argument);
        return "(" + lhs + "+" + rhs + ")";
    }

    @Override
    public String visitSubExpressionNode(SubExpressionNode node, Object argument) {
        String lhs = visit(node.getLhs(), argument);
        String rhs = visit(node.getRhs(), argument);
        return "(" + lhs + "-" + rhs + ")";
    }

    @Override
    public String visitMulExpressionNode(MulExpressionNode node, Object argument) {
        String lhs = visit(node.getLhs(), argument);
        String rhs = visit(node.getRhs(), argument);
        return "(" + lhs + "*" + rhs + ")";
    }

    @Override
    public String visitDivExpressionNode(DivExpressionNode node, Object argument) {
        String lhs = visit(node.getLhs(), argument);
        String rhs = visit(node.getRhs(), argument);
        return "(" + lhs + "/" + rhs + ")";
    }

    @Override
    public String visitModExpressionNode(ModExpressionNode node, Object argument) {
        String lhs = visit(node.getLhs(), argument);
        String rhs = visit(node.getRhs(), argument);
        return "(" + lhs + "%" + rhs + ")";
    }

    @Override
    public String visitIntExpressionNode(IntExpressionNode node, Object argument) {
        return node.getValue().getText();
    }

    //endregion Expression nodes

    @Override
    public String aggregate(String aggregate, String next) {
        if (aggregate == null) {
            return next;
        }
        if (next == null) {
            return aggregate;
        }
        return aggregate + next;
    }

    @Override
    public String defaultResult() {
        return "";
    }

}
