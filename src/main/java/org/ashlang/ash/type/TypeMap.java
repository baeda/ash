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

import java.util.HashMap;
import java.util.Map;

import static org.ashlang.ash.type.Types.I32;
import static org.ashlang.ash.type.Types.INVALID;

public class TypeMap {

    private final Map<String, Type> typeMap;

    public TypeMap() {
        typeMap = new HashMap<>();
        typeMap.put("i32", I32);
    }

    public Type resolve(String typeString) {
        return typeMap.getOrDefault(typeString, INVALID);
    }

}
