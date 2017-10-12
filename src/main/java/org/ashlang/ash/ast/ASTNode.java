package org.ashlang.ash.ast;

public abstract class ASTNode {

    private final Token startToken;
    private final Token stopToken;

    public ASTNode(Token startToken, Token stopToken) {
        this.startToken = startToken;
        this.stopToken = stopToken;
    }

    public final Token getStartToken() {
        return startToken;
    }

    public final Token getStopToken() {
        return stopToken;
    }

}
