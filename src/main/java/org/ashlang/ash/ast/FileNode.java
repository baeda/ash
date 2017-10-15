package org.ashlang.ash.ast;

public class FileNode extends ASTNode {

    private final ExpressionNode expression;

    public FileNode(ExpressionNode expression) {
        super(expression.getStartToken(), expression.getStopToken());

        this.expression = expression;
    }

}
