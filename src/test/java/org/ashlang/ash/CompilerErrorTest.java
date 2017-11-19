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
        assertThat(
            "func main() : void",
            "{",
            "    dump§ 12;",
            "}")
            .hasError(UNKNOWN_TOKEN).at(3, 9)
            .hasNoMoreErrors();
    }

    @Test
    public void missingToken() {
        assertThat(
            "func main() : void",
            "{",
            "    dump 12",
            "}")
            .hasError(MISSING_TOKEN).at(4, 1)
            .hasNoMoreErrors();
    }

    @Test
    public void inputMismatch() {
        assertThat(
            "func main() : void",
            "{",
            "    dump dump;",
            "}")
            .hasError(INPUT_MISMATCH).at(3, 10)
            .hasNoMoreErrors();
    }

    @Test
    public void invalidType() {
        assertThat(
            "func main() : void",
            "{",
            "    a : int;",
            "}")
            .hasError(INVALID_TYPE).at(3, 9)
            .hasError(SYMBOL_NOT_USED).at(3, 5)
            .hasNoMoreErrors();
    }

    @Test
    public void typeMismatch() {
        assertThat(
            "func main() : void",
            "{",
            "    a : i32;",
            "    b : u32;",
            "    a = 1;",
            "    b = a;",
            "}")
            .hasError(TYPE_MISMATCH).at(6, 5)
            .hasError(SYMBOL_INITIALIZED_BUT_NOT_USED).at(3, 5)
            .hasError(SYMBOL_INITIALIZED_BUT_NOT_USED).at(4, 5)
            .hasNoMoreErrors();
    }

    @Test
    public void typeMismatch_returnType() {
        assertThat(
            "func main() : void",
            "{",
            "    return 0;",
            "}")
            .hasError(TYPE_MISMATCH).at(3, 5)
            .hasNoMoreErrors();
    }

    @Test
    public void typeMismatch_malformedEntryPoint() {
        assertThat("func main() : i32 {}")
            .hasError(TYPE_MISMATCH).at(1, 6)
            .hasNoMoreErrors();
    }

    @Test
    public void invalidOperator() {
        assertThat(
            "func main() : void",
            "{",
            "    a : i32;",
            "    b : u32;",
            "    a = 1;",
            "    b = 1;",
            "    dump a + b;",
            "}")
            .hasError(INVALID_OPERATOR).at(7, 12)
            .hasNoMoreErrors();
    }

    @Test
    public void symbolAlreadyDeclared() {
        assertThat(
            "func main() : void",
            "{",
            "    a : i32;",
            "    a : i32;",
            "}")
            .hasError(SYMBOL_ALREADY_DECLARED).at(4, 5)
            .hasError(SYMBOL_NOT_USED).at(3, 5)
            .hasNoMoreErrors();
    }

    @Test
    public void symbolAlreadyDeclared_inNewScope() {
        assertThat(
            "func main() : void",
            "{",
            "    a : i32;",
            "    {",
            "        a : i32;",
            "    }",
            "}")
            .hasError(SYMBOL_ALREADY_DECLARED).at(5, 9)
            .hasError(SYMBOL_NOT_USED).at(3, 5)
            .hasNoMoreErrors();
    }

    @Test
    public void symbolNotDeclared() {
        assertThat(
            "func main() : void",
            "{",
            "    a : i32;",
            "    b = 12;",
            "}")
            .hasError(SYMBOL_NOT_DECLARED).at(4, 5)
            .hasError(SYMBOL_NOT_USED).at(3, 5)
            .hasNoMoreErrors();
    }

    @Test
    public void symbolNotDeclared_outsideScope() {
        assertThat(
            "func main() : void",
            "{",
            "    {",
            "        a : i32;",
            "    }",
            "    a = 12;",
            "}")
            .hasError(SYMBOL_NOT_DECLARED).at(6, 5)
            .hasError(SYMBOL_NOT_USED).at(4, 9)
            .hasNoMoreErrors();
    }

    @Test
    public void symbolNotInitialized() {
        assertThat(
            "func main() : void",
            "{",
            "    a : i32;",
            "    dump a;",
            "}")
            .hasError(SYMBOL_NOT_INITIALIZED).at(4, 10)
            .hasNoMoreErrors();
    }

    @Test
    public void symbolNotInitialized_emittedOnScopeEnd() {
        assertThat(
            "func main() : void",
            "{",
            "    {",
            "        a : i32;",
            "        dump a;",
            "    }",
            "}")
            .hasError(SYMBOL_NOT_INITIALIZED).at(5, 14)
            .hasNoMoreErrors();
    }

    @Test
    public void symbolNotUsed() {
        assertThat(
            "func main() : void",
            "{",
            "    a : i32;",
            "}")
            .hasError(SYMBOL_NOT_USED).at(3, 5)
            .hasNoMoreErrors();
    }

    @Test
    public void symbolNotUsed_emittedOnScopeEnd() {
        assertThat(
            "func main() : void",
            "{",
            "    {",
            "        a : i32;",
            "    }",
            "}")
            .hasError(SYMBOL_NOT_USED).at(4, 9)
            .hasNoMoreErrors();
    }

    @Test
    public void symbolInitializedButNotUsed() {
        assertThat(
            "func main() : void",
            "{",
            "    a : i32;",
            "    a = 42;",
            "}")
            .hasError(SYMBOL_INITIALIZED_BUT_NOT_USED).at(3, 5)
            .hasNoMoreErrors();
    }

    @Test
    public void functionAlreadyDeclared() {
        assertThat(
            "func main() : void {}",
            "func main() : void {}")
            .hasError(FUNCTION_ALREADY_DECLARED).at(2, 6)
            .hasNoMoreErrors();
    }

    @Test
    public void divisionByZero() {
        assertThat(
            "func main() : void",
            "{",
            "    dump (6+1)/0;",
            "}")
            .hasError(DIV_BY_ZERO).at(3, 10)
            .hasNoMoreErrors();
    }

    @Test
    public void intConstOverflow_i32Constant() {
        assertThat(
            "func main() : void",
            "{",
            "    a : i32;",
            "    a = 2147483648;",
            "}")
            .hasError(INT_CONST_OVERFLOW).at(4, 9)
            .hasError(SYMBOL_INITIALIZED_BUT_NOT_USED).at(3, 5)
            .hasNoMoreErrors();
    }

    @Test
    public void intConstOverflow_u32Constant() {
        assertThat(
            "func main() : void",
            "{",
            "    a : u32;",
            "    a = 4294967296;",
            "}")
            .hasError(INT_CONST_OVERFLOW).at(4, 9)
            .hasError(SYMBOL_INITIALIZED_BUT_NOT_USED).at(3, 5)
            .hasNoMoreErrors();
    }

    @Test
    public void intConstOverflow_i32Arithmetic() {
        assertThat(
            "func main() : void",
            "{",
            "    a : i32;",
            "    a = 2147483647 + 1;",
            "}")
            .hasError(INT_CONST_OVERFLOW).at(4, 9)
            .hasError(SYMBOL_INITIALIZED_BUT_NOT_USED).at(3, 5)
            .hasNoMoreErrors();
    }

    @Test
    public void intConstOverflow_u32Arithmetic() {
        assertThat(
            "func main() : void",
            "{",
            "    a : u32;",
            "    a = 4294967298 + 1;",
            "}")
            .hasError(INT_CONST_OVERFLOW).at(4, 9)
            .hasError(SYMBOL_INITIALIZED_BUT_NOT_USED).at(3, 5)
            .hasNoMoreErrors();
    }

    @Test(enabled = false) /* negative integer constants not yet implemented */
    public void intConstUnderflow_i32Constant() {
        assertThat(
            "func main() : void",
            "{",
            "    a : i32;",
            "    a = -2147483649;",
            "}")
            .hasError(INT_CONST_UNDERFLOW).at(4, 9)
            .hasError(SYMBOL_INITIALIZED_BUT_NOT_USED).at(3, 5)
            .hasNoMoreErrors();
    }


    @Test(enabled = false) /* negative integer constants not yet implemented */
    public void intConstUnderflow_u32Constant() {
        assertThat(
            "func main() : void",
            "{",
            "    a : u32;",
            "    a = -1;",
            "}")
            .hasError(INT_CONST_UNDERFLOW).at(4, 9)
            .hasError(SYMBOL_INITIALIZED_BUT_NOT_USED).at(3, 5)
            .hasNoMoreErrors();
    }

    @Test
    public void intConstUnderflow_i32Arithmetic() {
        assertThat(
            "func main() : void",
            "{",
            "    a : i32;",
            "    a = 0 - 2147483649;",
            "}")
            .hasError(INT_CONST_UNDERFLOW).at(4, 9)
            .hasError(SYMBOL_INITIALIZED_BUT_NOT_USED).at(3, 5)
            .hasNoMoreErrors();
    }

    @Test
    public void intConstUnderflow_u32Arithmetic() {
        assertThat(
            "func main() : void",
            "{",
            "    a : u32;",
            "    a = 0 - 1;",
            "}")
            .hasError(INT_CONST_UNDERFLOW).at(4, 9)
            .hasError(SYMBOL_INITIALIZED_BUT_NOT_USED).at(3, 5)
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
