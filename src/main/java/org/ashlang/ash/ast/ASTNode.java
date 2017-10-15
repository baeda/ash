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

    public abstract <T, A> T accept(ASTVisitor<T, A> visitor, A argument);

}
