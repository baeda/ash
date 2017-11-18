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

import org.apache.commons.lang3.tuple.Pair;
import org.ashlang.ash.type.IntType;
import org.ashlang.ash.type.Type;
import org.ashlang.ash.type.Types;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

class C11TypeMap {

    private final Map<Type, Pair<String, String>> typeMap;

    C11TypeMap() {
        typeMap = Types.allSubTypes(IntType.class)
            .stream()
            .collect(Collectors.toMap(
                Function.identity(),
                type -> Pair.of(cType(type), cFormat(type))
            ));

        typeMap.put(Types.VOID, Pair.of("void", ""));
    }

    String getType(Type type) {
        return typeMap.get(type).getLeft();
    }

    String getFormat(Type type) {
        return typeMap.get(type).getRight();
    }

    private static String cType(IntType type) {
        String prefix = type.isSigned()
            ? ""
            : "u";
        return String.format("%sint%d_t", prefix, type.getBitSize());
    }


    private static String cFormat(IntType type) {
        String classifier = type.isSigned()
            ? "d"
            : "u";
        return String.format("PRI%s%d", classifier, type.getBitSize());
    }

}
