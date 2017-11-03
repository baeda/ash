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

import org.ashlang.ash.ast.ASTNode;
import org.ashlang.ash.err.ErrorHandler;
import org.ashlang.ash.symbol.SymbolTable;

import java.util.function.BiConsumer;

public class CompilerPassChain {

    public static CompilerPassChain
    withErrorHandler(ErrorHandler errorHandler) {
        return new CompilerPassChain(errorHandler);
    }

    private final ErrorHandler errorHandler;
    private final SymbolTable symbolTable;

    private CompilerPass entryPass;

    private CompilerPassChain(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;

        symbolTable = new SymbolTable();
        entryPass = (eh, st, node) -> {};
    }

    public CompilerPassChain
    appendPass(CompilerPass pass) {
        entryPass = entryPass.andThen(pass);
        return this;
    }

    public void applyTo(ASTNode node) {
        entryPass.accept(errorHandler, symbolTable, node);
    }

}
