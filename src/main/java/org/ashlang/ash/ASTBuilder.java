package org.ashlang.ash;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;
import org.ashlang.ash.ast.*;
import org.ashlang.gen.AshBaseVisitor;
import org.ashlang.gen.AshParser.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.ashlang.gen.AshLexer.ASTERISK;
import static org.ashlang.gen.AshLexer.MINUS;
import static org.ashlang.gen.AshLexer.PERCENT;
import static org.ashlang.gen.AshLexer.PLUS;
import static org.ashlang.gen.AshLexer.SLASH;

public class ASTBuilder extends AshBaseVisitor<ASTNode> {

    public static ASTNode buildAST(ParseTree tree, Parser parser) {
        return new ASTBuilder(parser).visit(tree);
    }

    private final SourceProvider sourceProvider;

    private ASTBuilder(Parser parser) {
        sourceProvider = (start, stop) -> parser
            .getInputStream()
            .getTokenSource()
            .getInputStream()
            .getText(Interval.of(start.getStartIndex(), stop.getStopIndex()));
    }

    @Override
    public FileNode visitFile(FileContext ctx) {
        List<StatementNode> statements = ctx.statement().stream()
            .map(stmtCtx -> (StatementNode) visit(stmtCtx))
            .collect(Collectors.toList());

        return new FileNode(statements, sourceProvider);
    }

    @Override
    public VarDeclarationNode visitVarDeclaration(VarDeclarationContext ctx) {
        return new VarDeclarationNode(
            new Token(ctx.id),
            new Token(ctx.type),
            sourceProvider
        );
    }

    @Override
    public VarAssignNode visitVarAssign(VarAssignContext ctx) {
        ExpressionNode expression = (ExpressionNode) visit(ctx.value);
        return new VarAssignNode(
            new Token(ctx.id),
            expression,
            sourceProvider
        );
    }

    //region Statement nodes

    @Override
    public VarDeclarationStatementNode
    visitVarDeclarationStatement(VarDeclarationStatementContext ctx) {
        VarDeclarationNode varDeclarationNode = visitVarDeclaration(ctx.ref);
        return new VarDeclarationStatementNode(
            varDeclarationNode,
            new Token(ctx.stop),
            sourceProvider
        );
    }

    @Override
    public VarAssignStatementNode
    visitVarAssignStatement(VarAssignStatementContext ctx) {
        VarAssignNode varAssign = visitVarAssign(ctx.ref);
        return new VarAssignStatementNode(
            varAssign,
            new Token(ctx.stop),
            sourceProvider
        );
    }

    @Override
    public ASTNode visitDumpStatement(DumpStatementContext ctx) {
        ExpressionNode expression = (ExpressionNode) visit(ctx.expr);
        return new DumpStatementNode(
            new Token(ctx.start),
            new Token(ctx.stop),
            expression,
            sourceProvider
        );
    }

    //endregion Statement nodes

    //region Expression nodes

    @Override
    public ASTNode visitParenExpression(ParenExpressionContext ctx) {
        ExpressionNode expression = (ExpressionNode) visit(ctx.expr);
        return new ParenExpressionNode(
            new Token(ctx.start),
            new Token(ctx.stop),
            expression,
            sourceProvider
        );
    }

    @Override
    public ExpressionNode visitArithmeticExpression(ArithmeticExpressionContext ctx) {
        ExpressionNode lhs = (ExpressionNode) visit(ctx.lhs);
        ExpressionNode rhs = (ExpressionNode) visit(ctx.rhs);
        switch (ctx.op.getType()) {
            case PLUS:
                return new AddExpressionNode(
                    lhs,
                    rhs,
                    new Token(ctx.op),
                    sourceProvider
                );
            case MINUS:
                return new SubExpressionNode(
                    lhs,
                    rhs,
                    new Token(ctx.op),
                    sourceProvider
                );
            case ASTERISK:
                return new MulExpressionNode(
                    lhs,
                    rhs,
                    new Token(ctx.op),
                    sourceProvider
                );
            case SLASH:
                return new DivExpressionNode(
                    lhs,
                    rhs,
                    new Token(ctx.op),
                    sourceProvider
                );
            case PERCENT:
                return new ModExpressionNode(
                    lhs,
                    rhs,
                    new Token(ctx.op),
                    sourceProvider
                );
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public IdExpressionNode visitIdExpression(IdExpressionContext ctx) {
        return new IdExpressionNode(
            new Token(ctx.value),
            sourceProvider
        );
    }

    @Override
    public IntExpressionNode visitIntExpression(IntExpressionContext ctx) {
        return new IntExpressionNode(
            new Token(ctx.value),
            sourceProvider
        );
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
