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

@FunctionalInterface
public interface ThrowingFunction<T, R, X extends Throwable> {

    R apply(T t) throws X;

    default <V, Y extends Throwable> ThrowingFunction<V, R, Throwable>
    compose(ThrowingFunction<? super V, ? extends T, Y> before) {
        return (V v) -> apply(before.apply(v));
    }

    default <V, Y extends Throwable> ThrowingFunction<T, V, Throwable>
    andThen(ThrowingFunction<? super R, ? extends V, Y> after) {
        return (T t) -> after.apply(apply(t));
    }

}
