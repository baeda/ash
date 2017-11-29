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

package org.ashlang.ash.type;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public abstract class Type {

    private final String id;
    private final int bitSize;

    public Type(String id, int bitSize) {
        this.id = id;
        this.bitSize = bitSize;
    }

    public String
    getId() {
        return id;
    }

    public int
    getBitSize() {
        return bitSize;
    }

    @Override
    public boolean
    equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Type type = (Type) obj;

        return new EqualsBuilder()
            .append(bitSize, type.bitSize)
            .isEquals();
    }

    @Override
    public int
    hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(bitSize)
            .toHashCode();
    }

    @Override
    public String
    toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("bitSize", bitSize)
            .toString();
    }

}
