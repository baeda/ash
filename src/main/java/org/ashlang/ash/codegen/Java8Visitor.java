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

import org.ashlang.ash.ast.ASTVisitor;
import org.ashlang.ash.ast.FileNode;
import org.ashlang.ash.ast.IntExpressionNode;

class Java8Visitor implements ASTVisitor<String, Object> {

    private static Java8Visitor INSTANCE;

    static Java8Visitor getInstance() {
        if (INSTANCE == null) {
            synchronized (Java8Visitor.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Java8Visitor();
                }
            }
        }

        return INSTANCE;
    }

    private Java8Visitor() { /**/ }

    @Override
    public String visitFileNode(FileNode node, Object argument) {
        return String.join(
            "\n",
            "class _$Main {",
            "  public static void main(String[] args) {",
            "    System.out.print(" + visitChildren(node, argument) + ");",
            "  }",
            "}");
    }

    @Override
    public String visitIntExpressionNode(IntExpressionNode node, Object argument) {
        return node.getValue().getText();
    }

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
