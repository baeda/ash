/*
 * The Ash Project
 * Copyright (C) 2017  Peter Skrypalle
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 2 of the License only.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.ashlang.ash.lang;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;
import org.ashlang.ash.ast.*;
import org.ashlang.gen.AshBaseVisitor;
import org.ashlang.gen.AshParser.*;

import java.util.Collection;
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

        return setParent(
            new FileNode(
                statements,
                sourceProvider
            ),
            statements
        );
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
        return setParent(
            new VarAssignNode(
                new Token(ctx.id),
                expression,
                sourceProvider
            ),
            expression
        );
    }

    @Override
    public BlockNode visitBlock(BlockContext ctx) {
        List<StatementNode> statements = ctx.statements.stream()
            .map(stmtCtx -> (StatementNode) visit(stmtCtx))
            .collect(Collectors.toList());

        return setParent(
            new BlockNode(
                statements,
                sourceProvider
            ),
            statements
        );
    }

    //region statement nodes

    @Override
    public VarDeclarationStatementNode
    visitVarDeclarationStatement(VarDeclarationStatementContext ctx) {
        VarDeclarationNode varDeclarationNode = visitVarDeclaration(ctx.ref);
        return setParent(
            new VarDeclarationStatementNode(
                varDeclarationNode,
                new Token(ctx.stop),
                sourceProvider
            ),
            varDeclarationNode
        );
    }

    @Override
    public VarAssignStatementNode
    visitVarAssignStatement(VarAssignStatementContext ctx) {
        VarAssignNode varAssign = visitVarAssign(ctx.ref);
        return setParent(
            new VarAssignStatementNode(
                varAssign,
                new Token(ctx.stop),
                sourceProvider
            ),
            varAssign
        );
    }

    @Override
    public BlockStatementNode
    visitBlockStatement(BlockStatementContext ctx) {
        BlockNode block = visitBlock(ctx.ref);
        return setParent(
            new BlockStatementNode(
                block,
                new Token(ctx.stop),
                sourceProvider
            ),
            block
        );
    }

    @Override
    public ASTNode visitDumpStatement(DumpStatementContext ctx) {
        ExpressionNode expression = (ExpressionNode) visit(ctx.expr);
        return setParent(
            new DumpStatementNode(
                new Token(ctx.start),
                new Token(ctx.stop),
                expression,
                sourceProvider
            ),
            expression
        );
    }

    //endregion statement nodes

    //region expression nodes

    @Override
    public ASTNode visitParenExpression(ParenExpressionContext ctx) {
        ExpressionNode expression = (ExpressionNode) visit(ctx.expr);
        return setParent(
            new ParenExpressionNode(
                new Token(ctx.start),
                new Token(ctx.stop),
                expression,
                sourceProvider
            ),
            expression
        );
    }

    @Override
    public ExpressionNode visitArithmeticExpression(ArithmeticExpressionContext ctx) {
        ExpressionNode lhs = (ExpressionNode) visit(ctx.lhs);
        ExpressionNode rhs = (ExpressionNode) visit(ctx.rhs);
        ExpressionNode node;
        switch (ctx.op.getType()) {
            case PLUS:
                node = new AddExpressionNode(
                    lhs,
                    rhs,
                    new Token(ctx.op),
                    sourceProvider
                );
                break;
            case MINUS:
                node = new SubExpressionNode(
                    lhs,
                    rhs,
                    new Token(ctx.op),
                    sourceProvider
                );
                break;
            case ASTERISK:
                node = new MulExpressionNode(
                    lhs,
                    rhs,
                    new Token(ctx.op),
                    sourceProvider
                );
                break;
            case SLASH:
                node = new DivExpressionNode(
                    lhs,
                    rhs,
                    new Token(ctx.op),
                    sourceProvider
                );
                break;
            case PERCENT:
                node = new ModExpressionNode(
                    lhs,
                    rhs,
                    new Token(ctx.op),
                    sourceProvider
                );
                break;
            default:
                throw new IllegalStateException();
        }

        return setParent(
            node,
            lhs,
            rhs
        );
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

    //endregion expression nodes

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

    @SafeVarargs
    private static <T extends ASTNode, U extends ASTNode>
    T setParent(T parent, U firstChild, U... otherChildren) {
        firstChild.setParent(parent);
        for (U child : otherChildren) {
            child.setParent(parent);
        }
        return parent;
    }

    private static <T extends ASTNode, U extends ASTNode>
    T setParent(T parent, Collection<U> children) {
        children.forEach(child -> child.setParent(parent));
        return parent;
    }

}
