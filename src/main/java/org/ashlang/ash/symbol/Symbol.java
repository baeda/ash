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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.ashlang.ash.ast.VarDeclarationNode;
import org.ashlang.ash.type.Type;

public class Symbol {

    private final VarDeclarationNode declSite;

    Symbol(VarDeclarationNode declSite) {
        this.declSite = declSite;
    }

    public VarDeclarationNode getDeclSite() {
        return declSite;
    }

    public String getIdentifier() {
        return declSite.getIdentifierToken().getText();
    }

    public Type getType() {
        return declSite.getType();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Symbol symbol = (Symbol) obj;
        return new EqualsBuilder()
            .append(getIdentifier(), symbol.getIdentifier())
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(getIdentifier())
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
            .append("identifier", getIdentifier())
            .append("type", getType())
            .build();
    }

}
