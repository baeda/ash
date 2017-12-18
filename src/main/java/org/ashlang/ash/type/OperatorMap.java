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

import org.apache.commons.lang3.tuple.Triple;

import java.util.HashMap;
import java.util.Map;

import static org.ashlang.ash.type.Operator.*;
import static org.ashlang.ash.type.Types.BOOL;
import static org.ashlang.ash.type.Types.INVALID;

public class OperatorMap {

    private final Map<Triple<Type, Operator, Type>, Type> opMap;

    public OperatorMap() {
        opMap = new HashMap<>();

        Types.allExactTypes(IntType.class)
            .forEach(type -> {
                entry(type, ADD, type, type);
                entry(type, SUB, type, type);
                entry(type, MUL, type, type);
                entry(type, DIV, type, type);
                entry(type, MOD, type, type);
            });

        Types.allTypes().stream()
            .filter(Types::allValid)
            .forEach(type -> {
                entry(type, EQUALS, type, BOOL);
                entry(type, NOT_EQUALS, type, BOOL);
                entry(type, LT, type, BOOL);
                entry(type, GT, type, BOOL);
                entry(type, LT_EQ, type, BOOL);
                entry(type, GT_EQ, type, BOOL);
            });
    }

    private void entry(Type left, Operator op, Type right, Type result) {
        opMap.put(Triple.of(left, op, right), result);
    }

    public Type
    getResultOf(Type left, Operator op, Type right) {
        Triple<Type, Operator, Type> key = Triple.of(left, op, right);
        return opMap.getOrDefault(key, INVALID);
    }

}
