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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ExecResult {

    private final int exitCode;
    private final String out;
    private final String err;
    private final Exception exception;

    public ExecResult(int exitCode, String out, String err, Exception exception) {
        this.exitCode = exitCode;
        this.out = out;
        this.err = err;
        this.exception = exception;
    }

    public int getExitCode() {
        return exitCode;
    }

    public String getOut() {
        return out;
    }

    public String getErr() {
        return err;
    }

    public Exception getException() {
        return exception;
    }

    public boolean hasErrors() {
        return exitCode != 0 || !err.isEmpty();
    }

    public boolean isExceptional() {
        return exception != null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("exitCode", exitCode)
            .append("out", out)
            .append("err", err)
            .append("exception", exception)
            .toString();
    }

}
