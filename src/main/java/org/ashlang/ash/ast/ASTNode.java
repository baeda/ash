package org.ashlang.ash.ast;

import org.ashlang.ash.ast.visitor.ASTVisitor;

import java.lang.reflect.Field;
import java.util.List;

import static org.ashlang.ash.util.ReflectionUtil.*;

public abstract class ASTNode implements TokenRange {

    private final Token startToken;
    private final Token stopToken;
    private final SourceProvider sourceProvider;

    private ASTNode parent;

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

    public void setParent(ASTNode parent) {
        this.parent = parent;
    }

    public void replaceWith(ASTNode newNode) {
        parent.replace(this, newNode);
    }

    private void replace(ASTNode oldNode, ASTNode newNode) {
        List<Class<?>> hierarchy = recordHierarchy(this.getClass());
        for (Class<?> clazz : hierarchy) {
            for (Field field : clazz.getDeclaredFields()) {
                Object value = getFieldValue(field, this);
                if (value == oldNode) {
                    setFieldValue(field, this, newNode);
                }
            }
        }
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
