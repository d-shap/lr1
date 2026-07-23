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
import java.util.List;

/**
 * Утилита для вывода AST в текстовом формате.
 * Поддерживает несколько форматов вывода для визуализации дерева.
 */
public final class ASTTreePrinter {

    private static final String VERTICAL_LINE = "│";

    private static final String BRANCH = "├──";

    private static final String LAST_BRANCH = "└──";

    private static final String INDENT = "   ";

    private ASTTreePrinter() {
        super();
    }

    /**
     * Вывести AST в стандартном формате с отступами.
     *
     * @param root корневой узел дерева
     *
     * @return строковое представление дерева
     */
    public static String print(final ASTNode root) {
        StringBuilder sb = new StringBuilder();
        printNode(root, "", true, sb);
        return sb.toString();
    }

    /**
     * Вывести AST в компактном формате (одна строка на узел).
     *
     * @param root корневой узел дерева
     *
     * @return строковое представление дерева
     */
    public static String printCompact(final ASTNode root) {
        StringBuilder sb = new StringBuilder();
        printNodeCompact(root, 0, sb);
        return sb.toString();
    }

    /**
     * Вывести AST в формате с номерами строк (для выравнивания).
     *
     * @param root корневой узел дерева
     *
     * @return строковое представление дерева
     */
    public static String printWithLineNumbers(final ASTNode root) {
        StringBuilder sb = new StringBuilder();
        List<Integer> lines = new ArrayList<>();
        printNodeWithLineNumbers(root, "", true, sb, lines);
        return sb.toString();
    }

    private static void printNode(final ASTNode node, final String prefix, final boolean isLast, final StringBuilder sb) {
        // Вывести текущий узел
        sb.append(prefix);
        if (isLast) {
            sb.append(LAST_BRANCH);
        } else {
            sb.append(BRANCH);
        }

        if (node instanceof TerminalNode) {
            TerminalNode terminal = (TerminalNode) node;
            sb.append(String.format("'%s' = \"%s\"", terminal.getType(), terminal.getValue()));
        } else {
            RuleNode rule = (RuleNode) node;
            sb.append(rule.getRuleName());
        }
        sb.append("\n");

        // Вывести дочерние узлы
        if (node instanceof RuleNode) {
            RuleNode rule = (RuleNode) node;
            List<ASTNode> children = rule.getChildren();

            for (int i = 0; i < children.size(); i++) {
                boolean isLastChild = i == children.size() - 1;
                String newPrefix;
                if (isLast) {
                    newPrefix = prefix + INDENT;
                } else {
                    newPrefix = prefix + VERTICAL_LINE + " ";
                }
                printNode(children.get(i), newPrefix, isLastChild, sb);
            }
        }
    }

    private static void printNodeCompact(final ASTNode node, final int depth, final StringBuilder sb) {
        // Отступ
        for (int i = 0; i < depth; i++) {
            sb.append("  ");
        }

        // Информация об узле
        if (node instanceof TerminalNode) {
            TerminalNode terminal = (TerminalNode) node;
            sb.append(String.format("[TERM] %s = \"%s\"\n", terminal.getType(), terminal.getValue()));
        } else {
            RuleNode rule = (RuleNode) node;
            sb.append(String.format("[RULE] %s\n", rule.getRuleName()));

            for (ASTNode child : rule.getChildren()) {
                printNodeCompact(child, depth + 1, sb);
            }
        }
    }

    private static void printNodeWithLineNumbers(final ASTNode node, final String prefix, final boolean isLast, final StringBuilder sb, final List<Integer> lines) {
        // Вывести текущий узел
        sb.append(prefix);
        if (isLast) {
            sb.append(LAST_BRANCH);
        } else {
            sb.append(BRANCH);
        }

        if (node instanceof TerminalNode) {
            TerminalNode terminal = (TerminalNode) node;
            String lineInfo;
            if (terminal.getLine() >= 0) {
                lineInfo = String.format(" [%d:%d]", terminal.getLine(), terminal.getColumn());
            } else {
                lineInfo = "";
            }
            sb.append(String.format("'%s' = \"%s\"%s", terminal.getType(), terminal.getValue(), lineInfo));
        } else {
            RuleNode rule = (RuleNode) node;
            sb.append(rule.getRuleName());
        }
        sb.append("\n");

        // Вывести дочерние узлы
        if (node instanceof RuleNode) {
            RuleNode rule = (RuleNode) node;
            List<ASTNode> children = rule.getChildren();

            for (int i = 0; i < children.size(); i++) {
                boolean isLastChild = i == children.size() - 1;
                String newPrefix;
                if (isLast) {
                    newPrefix = prefix + INDENT;
                } else {
                    newPrefix = prefix + VERTICAL_LINE + " ";
                }
                printNodeWithLineNumbers(children.get(i), newPrefix, isLastChild, sb, lines);
            }
        }
    }

    /**
     * Получить высоту дерева.
     *
     * @param node корневой узел
     *
     * @return высота дерева (листья имеют высоту 0)
     */
    public static int getHeight(final ASTNode node) {
        if (node instanceof TerminalNode) {
            return 0;
        }
        RuleNode rule = (RuleNode) node;
        if (rule.getChildCount() == 0) {
            return 0;
        }
        int maxHeight = 0;
        for (ASTNode child : rule.getChildren()) {
            maxHeight = Math.max(maxHeight, getHeight(child));
        }
        return maxHeight + 1;
    }

    /**
     * Получить количество узлов в дереве.
     *
     * @param node корневой узел
     *
     * @return количество узлов (включая корень)
     */
    public static int getNodeCount(final ASTNode node) {
        int count = 1;
        if (node instanceof RuleNode) {
            RuleNode rule = (RuleNode) node;
            for (ASTNode child : rule.getChildren()) {
                count += getNodeCount(child);
            }
        }
        return count;
    }

    /**
     * Получить статистику дерева.
     *
     * @param node корневой узел
     *
     * @return строка со статистикой
     */
    public static String getStatistics(final ASTNode node) {
        int height = getHeight(node);
        int nodeCount = getNodeCount(node);
        int ruleCount = countRules(node);
        int terminalCount = nodeCount - ruleCount;

        return String.format(
                "AST Statistics:\n" +
                        "  Total nodes: %d\n" +
                        "  Rule nodes: %d\n" +
                        "  Terminal nodes: %d\n" +
                        "  Height: %d",
                nodeCount, ruleCount, terminalCount, height
        );
    }

    private static int countRules(final ASTNode node) {
        int count = 0;
        if (node instanceof RuleNode) {
            count = 1;
            RuleNode rule = (RuleNode) node;
            for (ASTNode child : rule.getChildren()) {
                count += countRules(child);
            }
        }
        return count;
    }

}
