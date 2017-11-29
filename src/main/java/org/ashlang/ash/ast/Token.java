package org.ashlang.ash.ast;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Token {

    private final int line;
    private final int column;
    private final String text;
    private final String sourceName;

    private final int startIndex;
    private final int stopIndex;

    public Token(org.antlr.v4.runtime.Token token) {
        this(
            token.getLine() - 1 /* ANTLR line indices start at 1 */,
            token.getCharPositionInLine(),
            token.getText(),
            token.getInputStream().getSourceName(),
            token.getStartIndex(),
            token.getStopIndex());
    }

    public Token(
        int line,
        int column,
        String text,
        String sourceName,
        int startIndex,
        int stopIndex
    ) {
        this.line = line;
        this.column = column;
        this.text = text;
        this.sourceName = sourceName;
        this.startIndex = startIndex;
        this.stopIndex = stopIndex;
    }

    public int
    getLine() {
        return line;
    }

    public int
    getColumn() {
        return column;
    }

    public String
    getText() {
        return text;
    }

    public String
    getSourceName() {
        return sourceName;
    }

    public int
    getStartIndex() {
        return startIndex;
    }

    public int
    getStopIndex() {
        return stopIndex;
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

        Token token = (Token) obj;

        return new EqualsBuilder()
            .append(line, token.line)
            .append(column, token.column)
            .append(startIndex, token.startIndex)
            .append(stopIndex, token.stopIndex)
            .append(text, token.text)
            .append(sourceName, token.sourceName)
            .isEquals();
    }

    @Override
    public int
    hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(line)
            .append(column)
            .append(text)
            .append(sourceName)
            .append(startIndex)
            .append(stopIndex)
            .toHashCode();
    }

    @Override
    public String
    toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
            .append("line", line)
            .append("column", column)
            .append("text", text)
            .append("sourceName", sourceName)
            .append("startIndex", startIndex)
            .append("stopIndex", stopIndex)
            .toString();
    }

}
