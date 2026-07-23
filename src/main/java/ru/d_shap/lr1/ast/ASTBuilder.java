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

import java.util.Objects;
import java.util.Stack;

/**
 * Вспомогательный класс для построения AST во время парсинга.
 * Использует стек для управления узлами при построении дерева bottom-up.
 */
public class ASTBuilder {

    private final Stack<ASTNode> nodeStack;

    /**
     * Создать построитель AST.
     */
    public ASTBuilder() {
        super();
        nodeStack = new Stack<>();
    }

    /**
     * Создать узел терминала и добавить его в стек.
     *
     * @param tokenType  тип токена
     * @param tokenValue значение токена
     */
    public void createTerminal(final String tokenType, final String tokenValue) {
        nodeStack.push(new TerminalNode(tokenType, tokenValue));
    }

    /**
     * Создать узел терминала с информацией о позиции и добавить его в стек.
     *
     * @param tokenType  тип токена
     * @param tokenValue значение токена
     * @param line       номер строки
     * @param column     номер колонки
     */
    public void createTerminal(final String tokenType, final String tokenValue, final int line, final int column) {
        nodeStack.push(new TerminalNode(tokenType, tokenValue, line, column));
    }

    /**
     * Создать узел правила и добавить его в стек.
     *
     * @param ruleName имя правила
     */
    public void createRule(final String ruleName) {
        nodeStack.push(new RuleNode(ruleName));
    }

    /**
     * Создать узел правила, заполнить его дочерними узлами из стека и добавить обратно в стек.
     * <p>
     * Метод извлекает из стека N последних элементов и добавляет их как дочерние узлы новому узлу правила.
     *
     * @param ruleName   имя правила
     * @param childCount количество дочерних узлов, которые нужно извлечь из стека
     */

    public void createRule(final String ruleName, final int childCount) {

        RuleNode rule = new RuleNode(ruleName);

        ASTNode[] children = new ASTNode[childCount];

        for (int i = childCount - 1; i >= 0; i--) {

            children[i] = nodeStack.pop();

        }

        for (ASTNode child : children) {

            rule.addChild(child);

        }

        nodeStack.push(rule);

    }

    /**
     * Добавить узел в текущий узел правила (верхний узел в стеке).
     * Верхний узел должен быть RuleNode.
     *
     * @param child дочерний узел для добавления
     */
    public void addChild(final ASTNode child) {
        if (nodeStack.isEmpty()) {
            throw new IllegalStateException("Node stack is empty");
        }
        ASTNode top = nodeStack.peek();
        if (!(top instanceof RuleNode)) {
            throw new IllegalStateException("Top of stack is not a RuleNode");
        }
        ((RuleNode) top).addChild(Objects.requireNonNull(child, "child cannot be null"));
    }

    /**
     * Получить текущий узел (верхний в стеке) без удаления.
     *
     * @return текущий узел
     */
    public ASTNode getCurrentNode() {
        if (nodeStack.isEmpty()) {
            throw new IllegalStateException("Node stack is empty");
        }
        return nodeStack.peek();
    }

    /**
     * Получить и удалить узел из вершины стека.
     *
     * @return узел с вершины стека
     */
    public ASTNode pop() {
        if (nodeStack.isEmpty()) {
            throw new IllegalStateException("Node stack is empty");
        }
        return nodeStack.pop();
    }

    /**
     * Получить построенное AST (узел, остающийся в стеке).
     * После построения дерева в стеке должен остаться ровно один узел - корень дерева.
     *
     * @return корневой узел AST
     */
    public ASTNode getAST() {
        if (nodeStack.isEmpty()) {
            throw new IllegalStateException("AST was not built: node stack is empty");
        }
        if (nodeStack.size() != 1) {
            throw new IllegalStateException("Invalid state: node stack contains " + nodeStack.size() + " elements, expected 1");
        }
        return nodeStack.peek();
    }

    /**
     * Получить размер стека (для отладки и проверок).
     *
     * @return количество элементов в стеке
     */
    public int getStackSize() {
        return nodeStack.size();
    }

    /**
     * Очистить стек (для переиспользования построителя).
     */
    public void clear() {
        nodeStack.clear();
    }

}
