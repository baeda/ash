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

package org.ashlang.ash.ast;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import static org.ashlang.ash.ast.ASTWalkUtil.getFieldValue;
import static org.ashlang.ash.ast.ASTWalkUtil.recordHierarchy;

public interface ASTVisitor<T, A> {

    T visitFileNode(FileNode node, A argument);

    T visitVarDeclarationNode(VarDeclarationNode node, A argument);
    T visitVarAssignNode(VarAssignNode node, A argument);

    T visitVarDeclarationStatementNode(VarDeclarationStatementNode node, A argument);
    T visitVarAssignStatementNode(VarAssignStatementNode node, A argument);
    T visitDumpStatementNode(DumpStatementNode node, A argument);

    T visitParenExpressionNode(ParenExpressionNode node, A argument);
    T visitAddExpressionNode(AddExpressionNode node, A argument);
    T visitSubExpressionNode(SubExpressionNode node, A argument);
    T visitMulExpressionNode(MulExpressionNode node, A argument);
    T visitDivExpressionNode(DivExpressionNode node, A argument);
    T visitModExpressionNode(ModExpressionNode node, A argument);
    T visitIdExpressionNode(IdExpressionNode node, A argument);
    T visitIntExpressionNode(IntExpressionNode node, A argument);

    default T visit(ASTNode node, A argument) {
        return node.accept(this, argument);
    }

    default T visitChildren(ASTNode node, A argument) {
        List<Class<?>> hierarchy = recordHierarchy(node.getClass());
        T aggregate = defaultResult();
        for (Class<?> clazz : hierarchy) {
            for (Field field : clazz.getDeclaredFields()) {
                Object value = getFieldValue(field, node);
                if (value instanceof ASTNode) {
                    T next = visit((ASTNode) value, argument);
                    aggregate = aggregate(aggregate, next);
                } else if (value instanceof Collection<?>) {
                    Collection<?> collection = (Collection<?>) value;
                    for (Object obj : collection) {
                        if (obj instanceof ASTNode) {
                            T next = visit((ASTNode) obj, argument);
                            aggregate = aggregate(aggregate, next);
                        }
                    }
                }
            }
        }
        return aggregate;
    }

    default T aggregate(T aggregate, T next) {
        return next;
    }

    default T defaultResult() {
        return null;
    }

}
