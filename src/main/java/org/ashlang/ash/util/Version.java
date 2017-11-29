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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Version {

    private final int major;
    private final int minor;
    private final int patch;

    public Version(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public int
    getMajor() {
        return major;
    }

    public int
    getMinor() {
        return minor;
    }

    public int
    getPatch() {
        return patch;
    }

    public boolean
    greaterThan(int major, int minor, int patch) {
        if (this.major < major) {
            return false;
        }
        if (this.major > major) {
            return true;
        }

        // matching major version
        if (this.minor < minor) {
            return false;
        }
        if (this.minor > minor) {
            return true;
        }

        // matching minor version
        return this.patch >= patch;
    }

    public boolean
    lessThan(int major, int minor, int patch) {
        return !greaterThan(major, minor, patch);
    }

    @Override
    public String
    toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
            .append("major", major)
            .append("minor", minor)
            .append("patch", patch)
            .toString();
    }

    @Override
    public boolean
    equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Version version = (Version) obj;

        return new EqualsBuilder()
            .append(major, version.major)
            .append(minor, version.minor)
            .append(patch, version.patch)
            .isEquals();
    }

    @Override
    public int
    hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(major)
            .append(minor)
            .append(patch)
            .toHashCode();
    }

}

