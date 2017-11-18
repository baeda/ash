package org.ashlang.ash.ast;

import org.ashlang.ash.ast.visitor.ASTVisitor;

import java.util.List;

public class FileNode extends ASTNode {

    private final List<FuncDeclarationNode> functions;

    public FileNode(List<FuncDeclarationNode> functions,
                    SourceProvider sourceProvider) {
        super(
            getFirstStartToken(functions),
            getLastStopToken(functions),
            sourceProvider
        );

        this.functions = functions;
    }

    public List<FuncDeclarationNode> getFunctions() {
        return functions;
    }

    @Override
    public <T, A> T accept(ASTVisitor<T, A> visitor, A argument) {
        return visitor.visitFileNode(this, argument);
    }

}
