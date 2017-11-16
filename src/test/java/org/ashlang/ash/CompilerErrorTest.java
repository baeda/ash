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

package org.ashlang.ash;

import org.testng.annotations.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.ashlang.ash.ErrorType.*;

public class CompilerErrorTest {

    @Test
    public void unknownToken() {
        assertThat("dump§ 12;")
            .hasError(UNKNOWN_TOKEN).at(1, 5)
            .hasNoMoreErrors();
    }

    @Test
    public void missingToken() {
        assertThat("dump 12")
            .hasError(MISSING_TOKEN).at(1, 8)
            .hasNoMoreErrors();
    }

    @Test
    public void inputMismatch() {
        assertThat("dump dump;")
            .hasError(INPUT_MISMATCH).at(1, 6)
            .hasNoMoreErrors();
    }

    @Test
    public void invalidType() {
        assertThat("a : int;")
            .hasError(INVALID_TYPE).at(1, 5)
            .hasNoMoreErrors();
    }

    @Test
    public void typeMismatch() {
        assertThat(
            "a : i32;",
            "b : u32;",
            "a = 1;",
            "b = a;")
            .hasError(TYPE_MISMATCH).at(4, 1)
            .hasNoMoreErrors();
    }

    @Test
    public void invalidOperator() {
        assertThat(
            "a : i32;",
            "b : u32;",
            "a = 1;",
            "b = 1;",
            "dump a + b;")
            .hasError(INVALID_OPERATOR).at(5, 8)
            .hasNoMoreErrors();
    }

    @Test
    public void symbolAlreadyDeclared() {
        assertThat(
            "a : i32;",
            "a : i32;")
            .hasError(SYMBOL_ALREADY_DECLARED).at(2, 1)
            .hasNoMoreErrors();
    }

    @Test
    public void symbolNotDeclared() {
        assertThat(
            "a : i32;",
            "b = 12;")
            .hasError(SYMBOL_NOT_DECLARED).at(2, 1)
            .hasNoMoreErrors();
    }

    @Test
    public void symbolNotInitialized() {
        assertThat(
            "a : i32;",
            "dump a;")
            .hasError(SYMBOL_NOT_INITIALIZED).at(2, 6)
            .hasNoMoreErrors();
    }

    @Test
    public void divisionByZero() {
        assertThat("dump (6+1)/0;")
            .hasError(DIV_BY_ZERO).at(1, 6)
            .hasNoMoreErrors();
    }

    @Test
    public void intConstOverflow_i32Constant() {
        assertThat(
            "a : i32;",
            "a = 2147483648;")
            .hasError(INT_CONST_OVERFLOW).at(2, 5)
            .hasNoMoreErrors();
    }

    @Test
    public void intConstOverflow_u32Constant() {
        assertThat(
            "a : u32;",
            "a = 4294967296;")
            .hasError(INT_CONST_OVERFLOW).at(2, 5)
            .hasNoMoreErrors();
    }

    @Test
    public void intConstOverflow_i32Arithmetic() {
        assertThat(
            "a : i32;",
            "a = 2147483647 + 1;")
            .hasError(INT_CONST_OVERFLOW).at(2, 5)
            .hasNoMoreErrors();
    }

    @Test
    public void intConstOverflow_u32Arithmetic() {
        assertThat(
            "a : u32;",
            "a = 4294967298 + 1;")
            .hasError(INT_CONST_OVERFLOW).at(2, 5)
            .hasNoMoreErrors();
    }

    @Test(enabled = false) /* negative integer constants not yet implemented */
    public void intConstUnderflow_i32Constant() {
        assertThat(
            "a : i32;",
            "a = -2147483649;")
            .hasError(INT_CONST_UNDERFLOW).at(2, 5)
            .hasNoMoreErrors();
    }


    @Test(enabled = false) /* negative integer constants not yet implemented */
    public void intConstUnderflow_u32Constant() {
        assertThat(
            "a : u32;",
            "a = -1;")
            .hasError(INT_CONST_UNDERFLOW).at(2, 5)
            .hasNoMoreErrors();
    }

    @Test
    public void intConstUnderflow_i32Arithmetic() {
        assertThat(
            "a : i32;",
            "a = 0 - 2147483649;")
            .hasError(INT_CONST_UNDERFLOW).at(2, 5)
            .hasNoMoreErrors();
    }

    @Test
    public void intConstUnderflow_u32Arithmetic() {
        assertThat(
            "a : u32;",
            "a = 0 - 1;")
            .hasError(INT_CONST_UNDERFLOW).at(2, 5)
            .hasNoMoreErrors();
    }

    private static CompilerErrorAssertor
    assertThat(String... lines) {
        String ashSrc = Stream.of(lines)
            .collect(Collectors.joining("\n"));
        CompilerErrorAssertor errorAssertor = new CompilerErrorAssertor();
        AshMain.buildAST(ashSrc, errorAssertor.captureErrors());
        return errorAssertor;
    }

}
