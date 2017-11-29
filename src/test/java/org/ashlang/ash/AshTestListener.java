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

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.util.Arrays;

public class AshTestListener extends TestListenerAdapter {

    @Override
    public void
    onTestFailure(ITestResult testResult) {
        log("FAILURE", testResult);
    }

    @Override
    public void
    onTestSuccess(ITestResult testResult) {
        log("SUCCESS", testResult);
    }

    private void
    log(String status, ITestResult tr) {
        long millis = tr.getEndMillis() - tr.getStartMillis();
        System.out.printf("  %s[%5dms]: %s%s\n",
            status, millis, tr.getName(), Arrays.toString(tr.getParameters()));
    }

}
