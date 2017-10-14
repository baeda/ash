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

package org.ashlang.ash.ast;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.List;

import static org.ashlang.ash.ast.ASTWalkUtil.*;

public final class ASTPrinter {

    private ASTPrinter() { /**/ }

    public static void print(ASTNode node) {
        print(node, 0);
    }

    private static void print(ASTNode node, int level) {
        List<Class<?>> hierarchy = recordHierarchy(node.getClass());
        int longestFieldName = findLongestFieldName(hierarchy);

        printNode(node, level);

        forAllFieldsIn(hierarchy, field -> {
            printField(field, node, level, longestFieldName);
        });
        forAllFieldsIn(hierarchy, field -> {
            printFieldRec(field, node, level);
        });
    }

    private static void printNode(ASTNode node, int level) {
        String nodeInfo = String.format(
            "%s [%s] -> [%s]",
            node.getClass().getSimpleName(),
            node.getStartToken(),
            node.getStopToken());
        outln(nodeInfo, level);
    }

    private static void
    printField(Field field, ASTNode node, int level, int longestFieldName) {
        Object value = getFieldValue(field, node);
        if (value instanceof ASTNode) {
            return;
        }

        String fieldName = field.getName();
        if (fieldName.equals("startToken") || fieldName.equals("stopToken")) {
            return;
        }

        String padded = String.format("%" + longestFieldName + "s", fieldName);
        outln(padded + " :: " + value, level + 1);
    }

    private static void printFieldRec(Field field, ASTNode node, int level) {
        Object value = getFieldValue(field, node);
        if (value instanceof ASTNode) {
            print((ASTNode) value, level + 1);
        }
    }

    private static String indent(int level) {
        return StringUtils.repeat("    ", level);
    }

    private static void outln(Object str, int level) {
        System.out.println(indent(level) + str);
    }

}
