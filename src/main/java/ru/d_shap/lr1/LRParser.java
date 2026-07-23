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
package ru.d_shap.lr1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

import ru.d_shap.lr1.ast.ASTBuilder;
import ru.d_shap.lr1.ast.ASTNode;
import ru.d_shap.lr1.ast.RuleNode;
import ru.d_shap.lr1.ast.TerminalNode;
import ru.d_shap.lr1.state.ActionGotoTable;
import ru.d_shap.lr1.state.Production;

/**
 * Основной движок LR(1) парсера.
 * Использует ACTION/GOTO таблицы для управления процессом парсинга.
 */
public class LRParser {

    private final ActionGotoTable actionGotoTable;

    private final List<Production> productions;

    private final String startSymbol;

    private final ASTBuilder astBuilder;

    /**
     * Создать LR парсер.
     *
     * @param actionGotoTable таблицы ACTION и GOTO для парсера
     * @param productions     список всех производственных правил
     * @param startSymbol     стартовый символ грамматики
     */
    public LRParser(final ActionGotoTable actionGotoTable, final List<Production> productions, final String startSymbol) {
        super();
        this.actionGotoTable = Objects.requireNonNull(actionGotoTable, "actionGotoTable cannot be null");
        this.productions = Objects.requireNonNull(productions, "productions cannot be null");
        this.startSymbol = Objects.requireNonNull(startSymbol, "startSymbol cannot be null");
        astBuilder = new ASTBuilder();
    }

    /**
     * Распарсить список токенов и вернуть результат.
     *
     * @param tokens список токенов для парсинга
     *
     * @return результат парсинга с AST или сообщение об ошибке
     */
    public ParseResult parse(final List<Token> tokens) {
        try {
            astBuilder.clear();

            // Стеки для парсера
            Stack<Integer> stateStack = new Stack<>();
            Stack<ASTNode> symbolStack = new Stack<>();

            // Инициализация
            stateStack.push(0); // Начальное состояние

            int tokenIndex = 0;
            Token currentToken = tokens.get(tokenIndex);

            while (true) {
                int state = stateStack.peek();

                // Получить действие из таблицы ACTION
                ActionGotoTable.Action action = actionGotoTable.getAction(state, currentToken.getType());

                if (action == null || action.getType() == ActionGotoTable.ActionType.ERROR) {
                    return createErrorResult("Unexpected token: " + currentToken.getType(),
                            currentToken.getLine(), currentToken.getColumn());
                }

                if (action.getType() == ActionGotoTable.ActionType.SHIFT) {
                    // SHIFT: добавить токен в стек символов и перейти в новое состояние
                    symbolStack.push(createTerminalNode(currentToken));
                    int nextState = action.getValue();
                    stateStack.push(nextState);
                    tokenIndex++;

                    if (tokenIndex < tokens.size()) {
                        currentToken = tokens.get(tokenIndex);
                    } else {
                        currentToken = new Token("EOF", "", -1, -1, -1);
                    }

                } else if (action.getType() == ActionGotoTable.ActionType.REDUCE) {
                    // REDUCE: применить производственное правило
                    int ruleNumber = action.getValue();
                    Production production = productions.get(ruleNumber);

                    // Извлечь символы из стека
                    List<ASTNode> rhsNodes = new ArrayList<>();
                    for (int i = 0; i < production.getRhs().size(); i++) {
                        if (!symbolStack.isEmpty()) {
                            rhsNodes.add(0, symbolStack.pop());
                        }
                        if (!stateStack.isEmpty()) {
                            stateStack.pop();
                        }
                    }

                    // Создать узел правила
                    RuleNode ruleNode = new RuleNode(production.getLhs());
                    for (ASTNode node : rhsNodes) {
                        ruleNode.addChild(node);
                    }
                    symbolStack.push(ruleNode);

                    // Получить следующее состояние из таблицы GOTO
                    int topState = stateStack.peek();
                    int gotoState = actionGotoTable.getGoto(topState, production.getLhs());
                    if (gotoState == -1) {
                        return createErrorResult("Invalid GOTO state for: " + production.getLhs(),
                                currentToken.getLine(), currentToken.getColumn());
                    }
                    stateStack.push(gotoState);

                } else if (action.getType() == ActionGotoTable.ActionType.ACCEPT) {
                    // ACCEPT: парсинг завершен успешно
                    if (symbolStack.isEmpty()) {
                        return createErrorResult("Symbol stack is empty at accept", -1, -1);
                    }
                    ASTNode root = symbolStack.pop();
                    return new ParseResult(root);

                } else {
                    return createErrorResult("Unknown action type",
                            currentToken.getLine(), currentToken.getColumn());
                }
            }
        } catch (Exception e) {
            return createErrorResult("Parser error: " + e.getMessage(), -1, -1);
        }
    }

    /**
     * Распарсить текст (с применением токенизатора).
     *
     * @param text      исходный текст
     * @param tokenizer токенизатор для преобразования текста в токены
     *
     * @return результат парсинга
     */
    public ParseResult parseText(final String text, final Tokenizer tokenizer) {
        try {
            List<Token> tokens = tokenizer.tokenize(text);
            return parse(tokens);
        } catch (Tokenizer.TokenizerException e) {
            return createErrorResult("Tokenizer error: " + e.getMessage(),
                    e.getLine(), e.getColumn());
        }
    }

    /**
     * Создать узел терминала из токена.
     */
    private TerminalNode createTerminalNode(final Token token) {
        return new TerminalNode(token.getType(), token.getValue(),
                token.getLine(), token.getColumn());
    }

    /**
     * Создать результат ошибки парсинга.
     */
    private ParseResult createErrorResult(final String message, final int line, final int column) {
        return new ParseResult(message, line, column);
    }

}
