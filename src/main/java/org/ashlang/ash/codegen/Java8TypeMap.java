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

import org.apache.commons.lang3.tuple.Triple;
import org.ashlang.ash.type.IntType;
import org.ashlang.ash.type.Type;
import org.ashlang.ash.type.Types;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

class Java8TypeMap {

    private final Map<Type, Triple<String, String, String>> typeMap;

    Java8TypeMap() {
        typeMap = Types.allSubTypes(IntType.class)
            .stream()
            .filter(IntType::isSigned)
            .collect(Collectors.toMap(
                Function.identity(),
                type -> Triple.of(jType(type), jFormat(type), "{{expr}}")
            ));
        Types.allSubTypes(IntType.class)
            .stream()
            .filter(IntType::isSigned)
            .forEach(type -> {

            });

        typeMap.put(Types.U8, Triple.of(jType((IntType) Types.U8), "%s", "Integer.toUnsignedString(({{expr}})&0xff, 10)"));
        typeMap.put(Types.U16, Triple.of(jType((IntType) Types.U16), "%s", "Integer.toUnsignedString(({{expr}})&0xffff, 10)"));
        typeMap.put(Types.U32, Triple.of(jType((IntType) Types.U32), "%s", "Integer.toUnsignedString({{expr}}, 10)"));
        typeMap.put(Types.U64, Triple.of(jType((IntType) Types.U64), "%s", "Long.toUnsignedString({{expr}}, 10)"));

        typeMap.put(Types.VOID, Triple.of(
            "void",
            "",
            "{{expr}}"
        ));
        typeMap.put(Types.BOOL, Triple.of(
            "boolean",
            "%s",
            "({{expr}} ? \"true\" : \"false\")"
        ));
    }

    String
    getType(Type type) {
        return typeMap.get(type).getLeft();
    }

    String
    getFormat(Type type) {
        return typeMap.get(type).getMiddle();
    }

    String
    formatExpression(Type type, String expression) {
        String fmt = typeMap.get(type).getRight();
        return fmt.replace("{{expr}}", expression);
    }

    private static String
    jType(IntType type) {
        switch (type.getBitSize()) {
            case 8:
                return "byte";
            case 16:
                return "short";
            case 32:
                return "int";
            case 64:
                return "long";
        }

        throw new IllegalStateException();
    }


    private static String
    jFormat(IntType type) {
        return "%d";
    }

}
