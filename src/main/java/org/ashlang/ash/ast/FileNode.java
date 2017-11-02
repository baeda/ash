package org.ashlang.ash.ast;

import java.util.List;

public class FileNode extends ASTNode {

    private final List<StatementNode> statements;

    public FileNode(List<StatementNode> statements) {
        super(getFirstStartToken(statements), getLastStopToken(statements));

        this.statements = statements;
    }

    public List<StatementNode> getStatements() {
        return statements;
    }

    @Override
    public <T, A> T accept(ASTVisitor<T, A> visitor, A argument) {
        return visitor.visitFileNode(this, argument);
    }

}
