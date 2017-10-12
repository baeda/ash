package org.ashlang.ash;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ashlang.ash.ast.ASTNode;
import org.ashlang.ash.ast.FileNode;
import org.ashlang.ash.ast.Token;

import java.nio.file.Path;
import java.util.Objects;

import static org.ashlang.ash.AshParser.FileContext;

public class ASTBuilder extends AshBaseVisitor<ASTNode> {

    private final Path file;

    public ASTBuilder(Path file) {
        this.file = file;
    }

    @Override
    public FileNode visitFile(FileContext ctx) {
        return new FileNode(createToken(ctx.start), createToken(ctx.stop));
    }

    private Token createToken(org.antlr.v4.runtime.Token token) {
        return new Token(
            file,
            token.getLine() - 1 /* ANTLR line indices start at 1 */,
            token.getCharPositionInLine(),
            token.getText());
    }

    //region ANTLR visitor default overrides

    @Override
    protected ASTNode aggregateResult(ASTNode aggregate, ASTNode next) {
        if (aggregate == null) {
            return next;
        }
        if (next == null) {
            return aggregate;
        }

        throw new IllegalStateException(String.format(
            "Cannot aggregate AST nodes %s, %s", aggregate, next));
    }

    @Override
    protected ASTNode defaultResult() {
        throw new IllegalStateException("Cannot provide default AST node");
    }

    @Override
    public ASTNode visit(ParseTree tree) {
        return Objects.requireNonNull(tree, "ParseTree").accept(this);
    }

    //endregion ANTLR visitor default overrides

}
