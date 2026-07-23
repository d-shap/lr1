/// ////////////////////////////////////////////////////////////////////////////////////////////////
// LR(1) parser implementation.
// Copyright (C) 2026 Dmitry Shapovalov.
//
// This file is part of LR(1) parser.
//
// LR(1) parser is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// LR(1) parser is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with this program. If not, see <http://www.gnu.org/licenses/>.
/// ////////////////////////////////////////////////////////////////////////////////////////////////
package ru.d_shap.lr1.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Узел для нетерминала (правила грамматики).
 * Содержит имя правила и список дочерних узлов.
 */
public class RuleNode implements ASTNode {

    private final String ruleName;

    private final List<ASTNode> children;

    /**
     * Создать узел правила.
     *
     * @param ruleName имя правила (например, "expression", "statement", "program")
     */
    public RuleNode(final String ruleName) {
        super();
        this.ruleName = Objects.requireNonNull(ruleName, "ruleName cannot be null");
        children = new ArrayList<>();
    }

    @Override
    public String getType() {
        return ruleName;
    }

    @Override
    public Object getValue() {
        return ruleName;
    }

    /**
     * Получить имя правила.
     */
    public String getRuleName() {
        return ruleName;
    }

    /**
     * Добавить дочерний узел.
     *
     * @param child дочерний узел
     */
    public void addChild(final ASTNode child) {
        children.add(Objects.requireNonNull(child, "child cannot be null"));
    }

    /**
     * Добавить несколько дочерних узлов.
     *
     * @param childNodes дочерние узлы
     */
    public void addChildren(final ASTNode... childNodes) {
        for (ASTNode child : childNodes) {
            addChild(child);
        }
    }

    /**
     * Получить неизменяемый список дочерних узлов.
     */
    public List<ASTNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visitRule(this);
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public ASTNode getChild(final int index) {
        return children.get(index);
    }

    @Override
    public String toString() {
        return String.format("RuleNode(%s)[%d children]", ruleName, children.size());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RuleNode ruleNode = (RuleNode) o;
        return Objects.equals(ruleName, ruleNode.ruleName) &&
                Objects.equals(children, ruleNode.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ruleName, children);
    }

}
