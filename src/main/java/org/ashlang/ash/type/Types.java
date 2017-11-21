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

import org.ashlang.ash.util.ReflectionUtil;

import java.util.Collection;
import java.util.stream.Collectors;

public interface Types {

    Type INVALID = new InvalidType();
    Type VOID = new VoidType();
    Type BOOL = new BoolType();
    Type I8 = new IntType(8, true);
    Type I16 = new IntType(16, true);
    Type I32 = new IntType(32, true);
    Type I64 = new IntType(64, true);
    Type U8 = new IntType(8, false);
    Type U16 = new IntType(16, false);
    Type U32 = new IntType(32, false);
    Type U64 = new IntType(64, false);

    static boolean allValid(Type... types) {
        for (Type type : types) {
            if (INVALID.equals(type)) {
                return false;
            }
        }
        return true;
    }

    static Collection<Type> allTypes() {
        return allSubTypes(Type.class);
    }

    static <T extends Type>
    Collection<T> allSubTypes(Class<T> targetClass) {
        return ReflectionUtil.getAllStaticFields(Types.class, targetClass);
    }

    static <T extends Type>
    Collection<T> allExactTypes(Class<T> targetClass) {
        return allSubTypes(targetClass)
            .stream()
            .filter(t -> targetClass.equals(t.getClass()))
            .collect(Collectors.toList());
    }

}
