package org.ashlang.ash.ast;

public class FileNode extends ASTNode {

    private final Expression expression;

    public FileNode(Expression expression) {
        super(expression.getStartToken(), expression.getStopToken());

        this.expression = expression;
    }

}
