package org.ashlang.ash;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ashlang.ash.ast.*;
import org.ashlang.gen.AshBaseVisitor;
import org.ashlang.gen.AshParser.*;

import java.util.Objects;

import static org.ashlang.gen.AshLexer.ASTERISK;
import static org.ashlang.gen.AshLexer.MINUS;
import static org.ashlang.gen.AshLexer.PERCENT;
import static org.ashlang.gen.AshLexer.PLUS;
import static org.ashlang.gen.AshLexer.SLASH;

public class ASTBuilder extends AshBaseVisitor<ASTNode> {

    private static final ASTBuilder INSTANCE = new ASTBuilder();

    public static ASTNode buildAST(ParseTree tree) {
        return INSTANCE.visit(tree);
    }

    private ASTBuilder() { /**/ }

    @Override
    public FileNode visitFile(FileContext ctx) {
        StatementNode statement = (StatementNode) visit(ctx.statement());
        return new FileNode(statement);
    }

    @Override
    public VarDeclarationNode visitVarDeclaration(VarDeclarationContext ctx) {
        return new VarDeclarationNode(
            new Token(ctx.id),
            new Token(ctx.type));
    }

    //region Statement nodes

    @Override
    public VarDeclarationStatementNode
    visitVarDeclarationStatement(VarDeclarationStatementContext ctx) {
        return new VarDeclarationStatementNode(
            visitVarDeclaration(ctx.ref),
            new Token(ctx.stop));
    }

    @Override
    public ASTNode visitDumpStatement(DumpStatementContext ctx) {
        ExpressionNode expression = (ExpressionNode) visit(ctx.expr);
        return new DumpStatementNode(
            new Token(ctx.start),
            new Token(ctx.stop),
            expression);
    }

    //endregion Statement nodes

    //region Expression nodes

    @Override
    public ASTNode visitParenExpression(ParenExpressionContext ctx) {
        ExpressionNode expression = (ExpressionNode) visit(ctx.expr);
        return new ParenExpressionNode(
            new Token(ctx.start),
            new Token(ctx.stop),
            expression);
    }

    @Override
    public ExpressionNode visitArithmeticExpression(ArithmeticExpressionContext ctx) {
        ExpressionNode lhs = (ExpressionNode) visit(ctx.lhs);
        ExpressionNode rhs = (ExpressionNode) visit(ctx.rhs);
        switch (ctx.op.getType()) {
            case PLUS:
                return new AddExpressionNode(lhs, rhs, new Token(ctx.op));
            case MINUS:
                return new SubExpressionNode(lhs, rhs, new Token(ctx.op));
            case ASTERISK:
                return new MulExpressionNode(lhs, rhs, new Token(ctx.op));
            case SLASH:
                return new DivExpressionNode(lhs, rhs, new Token(ctx.op));
            case PERCENT:
                return new ModExpressionNode(lhs, rhs, new Token(ctx.op));
        }

        throw new IllegalStateException();
    }

    @Override
    public IntExpressionNode visitIntExpression(IntExpressionContext ctx) {
        return new IntExpressionNode(new Token(ctx.value));
    }

    //endregion Expression nodes

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
        return null;
    }

    @Override
    public ASTNode visit(ParseTree tree) {
        return Objects.requireNonNull(tree, "ParseTree").accept(this);
    }

    //endregion ANTLR visitor default overrides

}
