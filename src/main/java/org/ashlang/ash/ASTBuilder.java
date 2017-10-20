package org.ashlang.ash;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ashlang.ash.ast.*;
import org.ashlang.gen.AshBaseVisitor;
import org.ashlang.gen.AshParser.ArithmeticExpressionContext;
import org.ashlang.gen.AshParser.FileContext;
import org.ashlang.gen.AshParser.IntExpressionContext;

import java.nio.file.Path;
import java.util.Objects;

import static org.ashlang.gen.AshLexer.*;

public class ASTBuilder extends AshBaseVisitor<ASTNode> {

    private final Path file;

    public ASTBuilder(Path file) {
        this.file = file;
    }

    @Override
    public FileNode visitFile(FileContext ctx) {
        ExpressionNode expression = (ExpressionNode) visit(ctx.expression());
        return new FileNode(expression);
    }

    //region Expression nodes

    @Override
    public ExpressionNode visitArithmeticExpression(ArithmeticExpressionContext ctx) {
        ExpressionNode lhs = (ExpressionNode) visit(ctx.lhs);
        ExpressionNode rhs = (ExpressionNode) visit(ctx.rhs);
        switch (ctx.op.getType()) {
            case Plus:
                return new AddExpressionNode(lhs, rhs);
            case Minus:
                return new SubExpressionNode(lhs, rhs);
            case Asterisk:
                return new MulExpressionNode(lhs, rhs);
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
