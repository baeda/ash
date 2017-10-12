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

package org.ashlang.ash;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.ashlang.ash.AshParser.FileContext;
import org.ashlang.ash.ast.ASTNode;

public final class AshMain {

    private AshMain() { /**/ }

    public static void main(String[] args) {
        AshLexer lexer = new AshLexer(CharStreams.fromString("1+2"));
        AshParser parser = new AshParser(new CommonTokenStream(lexer));

        FileContext file = parser.file();

        System.out.println(file.toString(parser));

        ASTNode rootNode = new ASTBuilder(null).visit(file);

        System.out.println(rootNode);
    }

}
