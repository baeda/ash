package org.ashlang.ash.ast;

import java.util.List;

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


    static Token getFirstStartToken(List<? extends ASTNode> nodes) {
        if (nodes.isEmpty()) {
            throw new IllegalStateException(
                "Node list not expected to be empty");
        }

        return nodes.get(0).getStartToken();
    }

    static Token getLastStopToken(List<? extends ASTNode> nodes) {
        if (nodes.isEmpty()) {
            throw new IllegalStateException(
                "Node list not expected to be empty");
        }

        return nodes.get(nodes.size() - 1).getStopToken();
    }

}
