package org.ashlang.ash;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ashlang.ash.ast.*;
import org.ashlang.gen.AshBaseVisitor;
import org.ashlang.gen.AshParser.*;

import java.nio.file.Path;
import java.util.Objects;

import static org.ashlang.gen.AshLexer.ASTERISK;
import static org.ashlang.gen.AshLexer.MINUS;
import static org.ashlang.gen.AshLexer.PERCENT;
import static org.ashlang.gen.AshLexer.PLUS;
import static org.ashlang.gen.AshLexer.SLASH;

public class ASTBuilder extends AshBaseVisitor<ASTNode> {

    private final Path file;

    public ASTBuilder(Path file) {
        this.file = file;
    }

    @Override
    public FileNode visitFile(FileContext ctx) {
        StatementNode statement = (StatementNode) visit(ctx.statement());
        return new FileNode(statement);
    }

    //region Statement nodes

    @Override
    public ASTNode visitDumpStatement(DumpStatementContext ctx) {
        ExpressionNode expression = (ExpressionNode) visit(ctx.expr);
        return new DumpStatementNode(
            createToken(ctx.start),
            createToken(ctx.stop),
            expression);
    }

    //endregion Statement nodes

    //region Expression nodes

    @Override
    public ASTNode visitParenExpression(ParenExpressionContext ctx) {
        ExpressionNode expression = (ExpressionNode) visit(ctx.expr);
        return new ParenExpressionNode(
            createToken(ctx.start),
            createToken(ctx.stop),
            expression);
    }

    @Override
    public ExpressionNode visitArithmeticExpression(ArithmeticExpressionContext ctx) {
        ExpressionNode lhs = (ExpressionNode) visit(ctx.lhs);
        ExpressionNode rhs = (ExpressionNode) visit(ctx.rhs);
        switch (ctx.op.getType()) {
            case PLUS:
                return new AddExpressionNode(lhs, rhs);
            case MINUS:
                return new SubExpressionNode(lhs, rhs);
            case ASTERISK:
                return new MulExpressionNode(lhs, rhs);
            case SLASH:
                return new DivExpressionNode(lhs, rhs);
            case PERCENT:
                return new ModExpressionNode(lhs, rhs);
        }

        throw new IllegalStateException();
    }

    @Override
    public IntExpressionNode visitIntExpression(IntExpressionContext ctx) {
        return new IntExpressionNode(createToken(ctx.value));
    }

    //endregion Expression nodes

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
