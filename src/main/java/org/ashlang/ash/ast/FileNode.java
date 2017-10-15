package org.ashlang.ash.ast;

public class FileNode extends ASTNode {

    private final ExpressionNode expression;

    public FileNode(ExpressionNode expression) {
        super(expression.getStartToken(), expression.getStopToken());

        this.expression = expression;
    }

    @Override
    public <T, A> T accept(ASTVisitor<T, A> visitor, A argument) {
        return visitor.visitFileNode(this, argument);
    }

}
