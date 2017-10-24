package org.ashlang.ash.ast;

public class FileNode extends ASTNode {

    private final StatementNode statement;

    public FileNode(StatementNode statement) {
        super(statement.getStartToken(), statement.getStopToken());

        this.statement = statement;
    }

    @Override
    public <T, A> T accept(ASTVisitor<T, A> visitor, A argument) {
        return visitor.visitFileNode(this, argument);
    }

}
