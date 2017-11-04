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

import org.ashlang.ash.type.Type;

import java.util.HashMap;
import java.util.Map;

import static org.ashlang.ash.type.Type.I32;

public class C11TypeMap {

    private final Map<Type, String> typeMap;

    public C11TypeMap() {
        typeMap = new HashMap<>();
        typeMap.put(I32, "int32_t");
    }

    public String get(Type type) {
        return typeMap.getOrDefault(type, "INVALID_TYPE");
    }

}
