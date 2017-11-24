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

import org.ashlang.ash.symbol.Symbol;

import java.util.List;
import java.util.stream.Collectors;

public interface TokenRange {

    Token getStartToken();
    Token getStopToken();
    String getText();

    static TokenRange
    ofToken(Token token) {
        return new TokenRange() {
            @Override
            public Token getStartToken() {
                return token;
            }

            @Override
            public Token getStopToken() {
                return token;
            }

            @Override
            public String getText() {
                return token.getText();
            }
        };
    }

    static TokenRange
    ofSymbols(List<? extends Symbol> symbols) {
        return ofNodes(
            symbols.stream()
                .map(Symbol::getDeclSite)
                .collect(Collectors.toList())
        );
    }

    static TokenRange
    ofNodes(List<? extends ASTNode> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            throw new IllegalArgumentException();
        }

        ASTNode startNode = nodes.get(0);
        TokenRange stopNode = nodes.get(nodes.size() - 1);

        SourceProvider sourceProvider = startNode.getSourceProvider();
        Token startToken = startNode.getStartToken();
        Token stopToken = stopNode.getStartToken();
        return new TokenRange() {
            @Override
            public Token getStartToken() {
                return startToken;
            }

            @Override
            public Token getStopToken() {
                return stopToken;
            }

            @Override
            public String getText() {
                return sourceProvider.apply(startToken, stopToken);
            }
        };
    }

}
