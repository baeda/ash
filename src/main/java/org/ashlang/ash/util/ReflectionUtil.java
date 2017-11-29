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

package org.ashlang.ash.util;

import org.ashlang.ash.ast.ASTNode;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class ReflectionUtil {

    private ReflectionUtil() { /**/ }

    public static List<Class<?>>
    recordHierarchy(Class<?> startClass) {
        List<Class<?>> result = new ArrayList<>();

        Class<?> clazz = startClass;
        while (ASTNode.class.isAssignableFrom(clazz)) {
            result.add(clazz);
            clazz = clazz.getSuperclass();
        }

        return result;
    }

    public static int
    findLongestFieldName(List<Class<?>> classes) {
        return classes.stream()
            .flatMap(clazz -> Stream.of(clazz.getDeclaredFields()))
            .map(Field::getName)
            .mapToInt(String::length)
            .max()
            .orElse(0);
    }

    public static void
    forAllFieldsIn(List<Class<?>> classes, Consumer<Field> action) {
        for (Class<?> clazz : classes) {
            for (Field field : clazz.getDeclaredFields()) {
                action.accept(field);
            }
        }
    }

    public static Object
    getFieldValue(Field field, Object obj) {
        try {
            AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
                field.setAccessible(true);
                return null;
            });
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T
    getFieldValue(Field field, Object obj, Class<T> targetClass) {
        try {
            AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
                field.setAccessible(true);
                return null;
            });
            return targetClass.cast(field.get(obj));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (ClassCastException e) {
            return null;
        }
    }

    public static void
    setFieldValue(Field field, Object obj, Object value) {
        try {
            AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
                field.setAccessible(true);
                return null;
            });
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T>
    getAllStaticFields(Class<?> sourceClass, Class<T> fieldClass) {
        List<T> result = new ArrayList<>();
        for (Field field : sourceClass.getDeclaredFields()) {
            T value = getFieldValue(field, sourceClass, fieldClass);
            if (value != null) {
                result.add(value);
            }
        }
        return result;
    }

}
