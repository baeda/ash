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

import org.ashlang.ash.ast.ASTNode;
import org.ashlang.ash.err.ConsoleErrorHandler;
import org.ashlang.ash.err.ErrorHandler;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class CompilerSystemTest {

    @DataProvider(parallel = true)
    public Object[][] provideAshSourceAndExpectedResultString() {
        return new Object[][]{
            {"dump 0;", "0"},
            {"dump 1;", "1"},
            {"dump 2147483647;", "2147483647"},

            {"dump 0+0;", "0"},
            {"dump 0+1;", "1"},
            {"dump 1+0;", "1"},
            {"dump 1+1;", "2"},
            {"dump 2147483647+0;", "2147483647"},
            {"dump 0+2147483647;", "2147483647"},
            {"dump 1+2147483646;", "2147483647"},
            {"dump 2147483646+1;", "2147483647"},

            {"dump 0-0;", "0"},
            {"dump 0-1;", "-1"},
            {"dump 1-0;", "1"},
            {"dump 1-1;", "0"},
            {"dump 2147483647-0;", "2147483647"},
            {"dump 0-2147483647;", "-2147483647"},
            {"dump 1-2147483647;", "-2147483646"},
            {"dump 2147483647-1;", "2147483646"},
            {"dump 2147483647-2147483647;", "0"},

            {"dump 0*0;", "0"},
            {"dump 0*1;", "0"},
            {"dump 1*0;", "0"},
            {"dump 1*1;", "1"},
            {"dump 2*2;", "4"},
            {"dump 2*2*2;", "8"},
            {"dump 2*2*2*2;", "16"},
            {"dump 2*2*2*2*2;", "32"},
            {"dump 2*2*2*2*2*2;", "64"},

            {"dump 0/1;", "0"},
            {"dump 1/1;", "1"},
            {"dump 4/2;", "2"},
            {"dump 8/2/2;", "2"},
            {"dump 16/2/2/2;", "2"},
            {"dump 32/2/2/2/2;", "2"},
            {"dump 64/2/2/2/2/2;", "2"},

            {"dump 0%1;", "0"},
            {"dump 1%1;", "0"},
            {"dump 4%2;", "0"},
            {"dump 4%3;", "1"},

            {"dump 1+2-3+4-5+6-7+8;", "6"},
            {"dump 1-2+3-4+5-6+7-8;", "-4"},
            {"dump 1+2+3*42;", "129"},
            {"dump 1+2+3*42/4;", "34"},
            {"dump 1+2+3*42/5;", "28"},
            {"dump 3*3/2;", "4"},
            {"dump 4/3*7%2;", "1"},

            {"dump 1+2;", "3"},
            {"dump (1)+2;", "3"},
            {"dump 1+(2);", "3"},
            {"dump (1)+(2);", "3"},
            {"dump (1+2);", "3"},
            {"dump 1+2*3;", "7"},
            {"dump 1+(2*3);", "7"},
            {"dump (1+2)*3;", "9"},
        };
    }

    @Test(dataProvider = "provideAshSourceAndExpectedResultString")
    public void c11_target(String input, String expected) {
        IOUtil.executeInTempDir(tmpDir -> {
            // [ash compiler] Arrange
            ErrorHandler errorHandler = new ConsoleErrorHandler();

            // [ash compiler] Act
            ASTNode rootNode = AshMain.buildAST(input, errorHandler);

            // [ash compiler] Assert
            assertThat(errorHandler.hasErrors()).isFalse();

            // [c11 compiler] Arrange
            Path outFile = tmpDir.resolve("out");

            // [c11 compiler] Act
            AshMain.compileToNative(rootNode, outFile);

            // [c11 compiler] Assert
            // asserted by thrown exception on failure

            // [bin execution] Arrange
            // [bin execution] Act
            ExecResult run = IOUtil.exec(outFile);

            // [bin execution] Assert
            assertThat(run.getErr()).isEmpty();
            assertThat(run.getOut()).isEqualTo(expected);
            assertThat(run.getExitCode()).isZero();
        });
    }

}
