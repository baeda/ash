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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.ashlang.ash.type.IntType;
import org.ashlang.ash.type.Operator;
import org.ashlang.ash.type.Type;
import org.ashlang.ash.type.Types;

import java.util.HashMap;
import java.util.Map;

import static org.ashlang.ash.type.Operator.*;
import static org.ashlang.ash.type.Types.*;

class Java8OperatorMap {

    private final Java8TypeMap typeMap;
    private final Map<Triple<Type, Operator, Type>, String> templateMap;

    public static void main(String[] args) {
        byte a = (byte) 200;
        byte b = 100;

        byte res = ((byte) (((short) (a & 0xff)) / ((short) (b & 0xff))));

        System.out.println(res & 0xff);

    }

    Java8OperatorMap(Java8TypeMap typeMap) {
        this.typeMap = typeMap;
        templateMap = new HashMap<>();

        Types.allSubTypes(IntType.class).stream()
            .filter(IntType::isSigned)
            .forEach(type -> {
                entry(type, ADD,        type, template(type, type, "+"));
                entry(type, SUB,        type, template(type, type, "-"));
                entry(type, MUL,        type, template(type, type, "*"));
                entry(type, DIV,        type, template(type, type, "/"));
                entry(type, MOD,        type, template(type, type, "%"));
                entry(type, EQUALS,     type, template(BOOL, type, "=="));
                entry(type, NOT_EQUALS, type, template(BOOL, type, "!="));
                entry(type, LT,         type, template(BOOL, type, "<"));
                entry(type, GT,         type, template(BOOL, type, ">"));
                entry(type, LT_EQ,      type, template(BOOL, type, "<="));
                entry(type, GT_EQ,      type, template(BOOL, type, ">="));
            });

        entry(U8, ADD,        U8, maskedTemplate(U8,   U32, "+"));
        entry(U8, SUB,        U8, maskedTemplate(U8,   U32, "-"));
        entry(U8, MUL,        U8, maskedTemplate(U8,   U32, "*"));
        entry(U8, DIV,        U8, maskedTemplate(U8,   U32, "/"));
        entry(U8, MOD,        U8, maskedTemplate(U8,   U32, "%"));
        entry(U8, EQUALS,     U8, maskedTemplate(BOOL, U32, "=="));
        entry(U8, NOT_EQUALS, U8, maskedTemplate(BOOL, U32, "!="));
        entry(U8, LT,         U8, maskedTemplate(BOOL, U32, "<"));
        entry(U8, GT,         U8, maskedTemplate(BOOL, U32, ">"));
        entry(U8, LT_EQ,      U8, maskedTemplate(BOOL, U32, "<="));
        entry(U8, GT_EQ,      U8, maskedTemplate(BOOL, U32, ">="));

        entry(U16, ADD,        U16, maskedTemplate(U16,  U32, "+"));
        entry(U16, SUB,        U16, maskedTemplate(U16,  U32, "-"));
        entry(U16, MUL,        U16, maskedTemplate(U16,  U32, "*"));
        entry(U16, DIV,        U16, maskedTemplate(U16,  U32, "/"));
        entry(U16, MOD,        U16, maskedTemplate(U16,  U32, "%"));
        entry(U16, EQUALS,     U16, maskedTemplate(BOOL, U32, "=="));
        entry(U16, NOT_EQUALS, U16, maskedTemplate(BOOL, U32, "!="));
        entry(U16, LT,         U16, maskedTemplate(BOOL, U32, "<"));
        entry(U16, GT,         U16, maskedTemplate(BOOL, U32, ">"));
        entry(U16, LT_EQ,      U16, maskedTemplate(BOOL, U32, "<="));
        entry(U16, GT_EQ,      U16, maskedTemplate(BOOL, U32, ">="));

        entry(U32, ADD,        U32, maskedTemplate(U32,  U64, "+"));
        entry(U32, SUB,        U32, maskedTemplate(U32,  U64, "-"));
        entry(U32, MUL,        U32, maskedTemplate(U32,  U64, "*"));
        entry(U32, DIV,        U32, maskedTemplate(U32,  U64, "/"));
        entry(U32, MOD,        U32, maskedTemplate(U32,  U64, "%"));
        entry(U32, EQUALS,     U32, maskedTemplate(BOOL, U64, "=="));
        entry(U32, NOT_EQUALS, U32, maskedTemplate(BOOL, U64, "!="));
        entry(U32, LT,         U32, maskedTemplate(BOOL, U64, "<"));
        entry(U32, GT,         U32, maskedTemplate(BOOL, U64, ">"));
        entry(U32, LT_EQ,      U32, maskedTemplate(BOOL, U64, "<="));
        entry(U32, GT_EQ,      U32, maskedTemplate(BOOL, U64, ">="));

        entry(U64, ADD,        U64, maskedTemplate(U64, U64, "+"));
        entry(U64, SUB,        U64, maskedTemplate(U64, U64, "-"));
        entry(U64, MUL,        U64, maskedTemplate(U64, U64, "*"));
        entry(U64, DIV,        U64, "Long.divideUnsigned   ({{lhs}}, {{rhs}})     ");
        entry(U64, MOD,        U64, "Long.remainderUnsigned({{lhs}}, {{rhs}})     ");
        entry(U64, EQUALS,     U64, "Long.compareUnsigned  ({{lhs}}, {{rhs}}) == 0");
        entry(U64, NOT_EQUALS, U64, "Long.compareUnsigned  ({{lhs}}, {{rhs}}) != 0");
        entry(U64, LT,         U64, "Long.compareUnsigned  ({{lhs}}, {{rhs}}) <  0");
        entry(U64, GT,         U64, "Long.compareUnsigned  ({{lhs}}, {{rhs}}) >  0");
        entry(U64, LT_EQ,      U64, "Long.compareUnsigned  ({{lhs}}, {{rhs}}) <= 0");
        entry(U64, GT_EQ,      U64, "Long.compareUnsigned  ({{lhs}}, {{rhs}}) >= 0");
    }

    private void entry(Type left, Operator op, Type right, String template) {
        templateMap.put(Triple.of(left, op, right), template);
    }

    private String maskedTemplate(Type type, Type widenTo, String op) {
        String rawMask = StringUtils.repeat('f', type.getBitSize() / 4);
        String mask;
        if (widenTo.getBitSize() == 64) {
            mask = String.format("0x%sL", rawMask);
        } else {
            mask = String.format("0x%s", rawMask);
        }

        String jType = typeMap.getType(type);
        String jWidenTo = typeMap.getType(widenTo);

        return String.format(
            "((%s) (((%s) ({{lhs}} & %s)) %s ((%s) ({{rhs}} & %s))))",
            jType,
            jWidenTo,
            mask,
            op,
            jWidenTo,
            mask
        );
    }

    private String template(Type type, Type widenTo, String op) {
        return String.format(
            "((%s) (((%s) ({{lhs}})) %s ((%s) ({{rhs}}))))",
            typeMap.getType(type),
            typeMap.getType(widenTo),
            op,
            typeMap.getType(widenTo)
        );
    }

    public String op(Type lhs, Operator op, Type rhs, String lhsVal, String rhsVal) {
        String template = templateMap.get(Triple.of(lhs, op, rhs));
        template = template.replace("{{lhs}}", lhsVal);
        template = template.replace("{{rhs}}", rhsVal);
        return template;
    }

}
