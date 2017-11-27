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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.ashlang.gen.AshLexer.ASTERISK;
import static org.ashlang.gen.AshLexer.DOUBLE_EQUALS;
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
    public FileNode
    visitFile(FileContext ctx) {
        List<FuncDeclarationNode> functions = ctx.functions.stream()
            .map(funcDeclCtx -> (FuncDeclarationNode) visit(funcDeclCtx))
            .collect(Collectors.toList());

        return setParent(
            new FileNode(
                functions,
                sourceProvider
            ),
            functions
        );
    }

    @Override
    public FuncDeclarationNode
    visitFuncDeclaration(FuncDeclarationContext ctx) {
        BlockNode body = visitBlock(ctx.body);

        List<ParamDeclarationNode> params = new ArrayList<>();
        if (ctx.params != null) {
            ctx.params.paramDeclaration().stream()
                .map(stmtCtx -> (ParamDeclarationNode) visit(stmtCtx))
                .forEach(params::add);
        }

        return setParent(
            new FuncDeclarationNode(
                new Token(ctx.start),
                new Token(ctx.id),
                new Token(ctx.type),
                params, body,
                sourceProvider
            ),
            body,
            params
        );
    }

    @Override
    public ParamDeclarationNode
    visitParamDeclaration(ParamDeclarationContext ctx) {
        return new ParamDeclarationNode(
            new Token(ctx.id),
            new Token(ctx.type),
            sourceProvider
        );
    }

    @Override
    public VarDeclarationNode
    visitVarDeclaration(VarDeclarationContext ctx) {
        return new VarDeclarationNode(
            new Token(ctx.id),
            new Token(ctx.type),
            sourceProvider
        );
    }

    @Override
    public VarAssignNode
    visitVarAssign(VarAssignContext ctx) {
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
    public BlockNode
    visitBlock(BlockContext ctx) {
        List<StatementNode> statements = ctx.statements.stream()
            .map(stmtCtx -> (StatementNode) visit(stmtCtx))
            .collect(Collectors.toList());

        return setParent(
            new BlockNode(
                new Token(ctx.start),
                new Token(ctx.stop),
                statements,
                sourceProvider
            ),
            statements
        );
    }

    @Override
    public FuncCallNode
    visitFuncCall(FuncCallContext ctx) {
        List<ArgumentNode> args = new ArrayList<>();
        if (ctx.args != null) {
            ctx.args.argument().stream()
                .map(stmtCtx -> (ArgumentNode) visit(stmtCtx))
                .forEach(args::add);
        }

        return setParent(
            new FuncCallNode(
                new Token(ctx.id),
                new Token(ctx.stop),
                args,
                sourceProvider
            ),
            args
        );
    }

    @Override
    public ArgumentNode
    visitArgument(ArgumentContext ctx) {
        ExpressionNode expression = (ExpressionNode) visit(ctx.expr);

        return setParent(
            new ArgumentNode(
                expression,
                sourceProvider
            ),
            expression
        );
    }

    @Override
    public BranchNode
    visitOneArmedBranch(OneArmedBranchContext ctx) {
        ExpressionNode expression = (ExpressionNode) visit(ctx.expr);
        StatementNode onTrue = (StatementNode) visit(ctx.onTrue);

        BlockNode blockNode = new BlockNode(
            new Token(ctx.stop),
            new Token(ctx.stop),
            new ArrayList<>(),
            sourceProvider
        );
        StatementNode onFalse = setParent(
            new BlockStatementNode(
                blockNode,
                new Token(ctx.stop),
                sourceProvider
            ),
            blockNode
        );

        return setParent(
            new BranchNode(
                new Token(ctx.start),
                expression,
                onTrue,
                onFalse,
                sourceProvider
            ),
            expression,
            onTrue,
            onFalse
        );
    }

    @Override
    public BranchNode
    visitTwoArmedBranch(TwoArmedBranchContext ctx) {
        ExpressionNode expression = (ExpressionNode) visit(ctx.expr);
        StatementNode onTrue = (StatementNode) visit(ctx.onTrue);
        StatementNode onFalse = (StatementNode) visit(ctx.onFalse);

        return setParent(
            new BranchNode(
                new Token(ctx.start),
                expression,
                onTrue,
                onFalse,
                sourceProvider
            ),
            expression,
            onTrue,
            onFalse
        );
    }

    //region statement nodes

    @Override
    public ExpressionStatementNode
    visitExpressionStatement(ExpressionStatementContext ctx) {
        ExpressionNode expression = (ExpressionNode) visit(ctx.expr);

        return setParent(
            new ExpressionStatementNode(
                expression,
                sourceProvider
            ),
            expression
        );
    }

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
    public BranchStatementNode
    visitBranchStatement(BranchStatementContext ctx) {
        BranchNode branch = (BranchNode) visit(ctx.ref);

        return setParent(
            new BranchStatementNode(
                branch,
                sourceProvider
            ),
            branch
        );
    }

    @Override
    public ReturnStatementNode
    visitReturnStatement(ReturnStatementContext ctx) {
        ExpressionNode expression = (ExpressionNode) visit(ctx.expr);
        return setParent(
            new ReturnStatementNode(
                new Token(ctx.start),
                new Token(ctx.stop),
                expression,
                sourceProvider
            ),
            expression
        );
    }

    @Override
    public DumpStatementNode
    visitDumpStatement(DumpStatementContext ctx) {
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
    public ParenExpressionNode
    visitParenExpression(ParenExpressionContext ctx) {
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
    public BinaryExpressionNode
    visitArithmeticExpression(ArithmeticExpressionContext ctx) {
        ExpressionNode lhs = (ExpressionNode) visit(ctx.lhs);
        ExpressionNode rhs = (ExpressionNode) visit(ctx.rhs);
        BinaryExpressionNode node;
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
    public BinaryExpressionNode
    visitBoolExpression(BoolExpressionContext ctx) {
        ExpressionNode lhs = (ExpressionNode) visit(ctx.lhs);
        ExpressionNode rhs = (ExpressionNode) visit(ctx.rhs);
        BinaryExpressionNode node;
        switch (ctx.op.getType()) {
            case DOUBLE_EQUALS:
                node = new EqualsExpressionNode(
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
    public FuncCallExpressionNode
    visitFuncCallExpression(FuncCallExpressionContext ctx) {
        FuncCallNode funcCall = (FuncCallNode) visit(ctx.call);

        return setParent(
            new FuncCallExpressionNode(
                funcCall,
                sourceProvider
            ),
            funcCall
        );
    }

    @Override
    public ASTNode
    visitBoolLiteralExpression(BoolLiteralExpressionContext ctx) {
        return new BoolLiteralExpressionNode(
            new Token(ctx.value),
            sourceProvider
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
        if (tree == null) {
            return null;
        }
        return tree.accept(this);
    }

    //endregion ANTLR visitor default overrides

    private static <T extends ASTNode>
    T setParent(T parent, Object firstChild, Object... otherChildren) {
        setParent(parent, firstChild);

        for (Object child : otherChildren) {
            setParent(parent, child);
        }

        return parent;
    }

    private static <T extends ASTNode> T
    setParent(T parent, Object child) {
        if (child instanceof Collection<?>) {
            for (Object obj : (Collection<?>) child) {
                setParent(parent, obj);
            }
            return parent;
        }

        if (!(child instanceof ASTNode)) {
            throw new IllegalStateException();
        }

        ((ASTNode) child).setParent(parent);

        return parent;
    }

}
