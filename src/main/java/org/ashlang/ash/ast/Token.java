package org.ashlang.ash.ast;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.nio.file.Path;

public class Token {

    private final Path   file;
    private final int    line;
    private final int    column;
    private final String text;

    public Token(Path file, int line, int column, String text) {
        this.file = file;
        this.line = line;
        this.column = column;
        this.text = text;
    }

    public Path getFile() {
        return file;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Token token = (Token) obj;

        return new EqualsBuilder()
            .append(line, token.line)
            .append(column, token.column)
            .append(file, token.file)
            .append(text, token.text)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(file)
            .append(line)
            .append(column)
            .append(text)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("file", file)
            .append("line", line)
            .append("column", column)
            .append("text", text)
            .toString();
    }

}
