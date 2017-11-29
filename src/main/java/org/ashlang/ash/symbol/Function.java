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

package org.ashlang.ash.symbol;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.ashlang.ash.ast.FuncDeclarationNode;
import org.ashlang.ash.type.Type;

import java.util.ArrayList;
import java.util.List;

public class Function {

    private final FuncDeclarationNode declSite;
    private final List<Symbol> parameters;

    private boolean isUsed;

    Function(FuncDeclarationNode declSite) {
        this.declSite = declSite;

        parameters = new ArrayList<>();
    }

    public FuncDeclarationNode
    getDeclSite() {
        return declSite;
    }

    public List<Symbol>
    getParameters() {
        return parameters;
    }

    public String
    getIdentifier() {
        return declSite.getIdentifierToken().getText();
    }

    public Type
    getType() {
        return declSite.getType();
    }

    public void
    use() {
        isUsed = true;
    }

    public boolean
    isUsed() {
        return isUsed;
    }

    @Override
    public String
    toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
            .append("identifier", getIdentifier())
            .append("parameters", getParameters())
            .append("type", getType())
            .build();
    }

}
