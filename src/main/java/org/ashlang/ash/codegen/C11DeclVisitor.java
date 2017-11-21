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

import org.ashlang.ash.ast.FileNode;
import org.ashlang.ash.ast.FuncDeclarationNode;
import org.ashlang.ash.ast.visitor.ASTSingleBaseVisitor;

class C11DeclVisitor extends ASTSingleBaseVisitor<String> {

    static final String FUNC = "_a$h_";

    private final C11TypeMap typeMap;

    C11DeclVisitor(C11TypeMap typeMap) {
        this.typeMap = typeMap;
    }

    @Override
    protected String
    visitFileNode(FileNode node) {
        return String.join("\n",
            "#include <stdbool.h>",
            "#include <stdio.h>",
            "#include <stdint.h>",
            "#include <inttypes.h>",
            "",
            visitChildren(node),
            "");
    }

    @Override
    protected String
    visitFuncDeclarationNode(FuncDeclarationNode node) {
        String cType = typeMap.getType(node.getType());
        String identifier = node.getIdentifierToken().getText();

        if ("main".equals(identifier)) {
            // handled in impl visitor
            return defaultResult();
        }

        return cType + " " + FUNC + identifier + "();\n";
    }

    @Override
    public String
    aggregate(String aggregate, String next) {
        if (aggregate == null) {
            return next;
        }
        if (next == null) {
            return aggregate;
        }
        return aggregate + next;
    }

    @Override
    public String
    defaultResult() {
        return "";
    }

}
