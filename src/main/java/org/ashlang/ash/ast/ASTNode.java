package org.ashlang.ash.ast;

import java.util.List;

public abstract class ASTNode implements TokenRange {

    private final Token startToken;
    private final Token stopToken;
    private final SourceProvider sourceProvider;

    public ASTNode(Token startToken, Token stopToken,
                   SourceProvider sourceProvider) {
        this.startToken = startToken;
        this.stopToken = stopToken;
        this.sourceProvider = sourceProvider;
    }

    @Override
    public final Token getStartToken() {
        return startToken;
    }

    @Override
    public final Token getStopToken() {
        return stopToken;
    }

    @Override
    public String getText() {
        return sourceProvider.apply(startToken, stopToken);
    }

    public SourceProvider getSourceProvider() {
        return sourceProvider;
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
