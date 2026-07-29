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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import ru.d_shap.lr1.lexer.PatternTokenRule;
import ru.d_shap.lr1.lexer.TokenRule;
import ru.d_shap.lr1.parser.Production;

/**
 * Универсальный токенайзер, полностью управляемый грамматикой.
 * Токенайзер просто создаёт правила для всех терминалов из грамматики,
 * без каких-либо "волшебных" правил (NUMBER, IDENTIFIER и т.д.).
 */
public class GrammarTokenizer extends Tokenizer.BaseTokenizer {

    private final List<TokenRule> tokenRules;

    /**
     * Создать токенайзер.
     */
    public GrammarTokenizer() {
        super();
        tokenRules = new ArrayList<>();
    }

    /**
     * Инициализировать токенайзер из грамматики.
     * Автоматически добавляет правила для всех терминалов из грамматики.
     *
     * @param grammarMap     карта нетерминалов к их продукциям
     * @param allProductions список всех продукций
     */
    public void initializeFromGrammar(final Map<String, List<Production>> grammarMap, final List<Production> allProductions) {
        // Собрать все терминалы из грамматики
        Set<String> terminals = extractTerminals(grammarMap, allProductions);

        // Добавить терминалы из грамматики в правильном порядке
        addTerminalRules(terminals);
    }

    private Set<String> extractTerminals(final Map<String, List<Production>> grammarMap, final List<Production> allProductions) {
        Set<String> terminals = new HashSet<>();
        Set<String> nonTerminals = grammarMap.keySet();

        for (Production production : allProductions) {
            for (String symbol : production.getRhs()) {
                if (!nonTerminals.contains(symbol)) {
                    terminals.add(symbol);
                }
            }
        }

        return terminals;
    }

    /**
     * Добавить правила для терминалов из грамматики.
     * Порядок приоритета:
     * 1. Многосимвольные операторы (по длине убывая)
     * 2. Ключевые слова (с проверкой границ слова)
     * 3. Одиночные символы
     *
     * @param terminals множество терминалов
     */
    private void addTerminalRules(final Set<String> terminals) {
        List<String> multiCharOperators = new ArrayList<>();
        List<String> singleCharOperators = new ArrayList<>();
        List<String> keywordTerminals = new ArrayList<>();
        List<String> specialTerminals = new ArrayList<>();

        // Классифицировать терминалы
        for (String terminal : terminals) {
            if ("$".equals(terminal)) {
                continue;
            }

            if (terminal.length() > 1) {
                if (isAlphaNumeric(terminal)) {
                    keywordTerminals.add(terminal);
                } else {
                    if (terminal.startsWith("?") && terminal.endsWith("?")) {
                        specialTerminals.add(terminal);
                    } else {
                        multiCharOperators.add(terminal);
                    }
                }
            } else {
                singleCharOperators.add(terminal);
            }
        }

        // Сортировать для консистентности
        Collections.sort(multiCharOperators);
        Collections.sort(singleCharOperators);
        Collections.sort(keywordTerminals);
        Collections.sort(specialTerminals);

        // 1. Многосимвольные операторы (по длине убывая)
        Collections.sort(multiCharOperators, new Comparator<String>() {

            @Override
            public int compare(final String a, final String b) {
                int lenCmp = Integer.compare(b.length(), a.length());
                if (lenCmp != 0) {
                    return lenCmp;
                }
                return a.compareTo(b);
            }
        });

        for (String op : multiCharOperators) {
            tokenRules.add(new PatternTokenRule(op, "^" + Pattern.quote(op)));
        }

        // 2. Ключевые слова (с проверкой границ слова)
        for (String keyword : keywordTerminals) {
            tokenRules.add(new PatternTokenRule(keyword, "^" + "\\b" + Pattern.quote(keyword) + "\\b"));
        }

        if (specialTerminals.contains("?digit?")) {
            tokenRules.add(new PatternTokenRule("?digit?", "^" + "[0-9]"));
        }

        // 3. Одиночные символы
        for (String op : singleCharOperators) {
            tokenRules.add(new PatternTokenRule(op, "^" + Pattern.quote(op)));
        }
    }

    private boolean isAlphaNumeric(final String str) {
        return str.matches("[a-zA-Z_][a-zA-Z0-9_]*");
    }

    @Override
    public List<Token> tokenize(final String text) throws TokenizerException {
        if (tokenRules.isEmpty()) {
            throw new TokenizerException("No token rules defined. Call addTokenRule() or initializeFromGrammar() first.", 1, 1);
        }

        List<Token> tokens = new ArrayList<>();
        int position = 0;
        int line = 1;
        int column = 1;

        while (position < text.length()) {
            boolean matched = false;

            for (TokenRule rule : tokenRules) {
                String remaining = text.substring(position);
                String matchedText = rule.match(remaining);

                if (matchedText != null) {
                    String tokenType = rule.getTokenType();

                    Token token = new Token(tokenType, matchedText, line, column, position);
                    tokens.add(token);

                    position += matchedText.length();
                    for (int i = 0; i < matchedText.length(); i++) {
                        if (matchedText.charAt(i) == '\n') {
                            line++;
                            column = 1;
                        } else {
                            column++;
                        }
                    }

                    matched = true;
                    break;
                }
            }

            if (!matched) {
                throw new TokenizerException(
                        "Unexpected character: '" + text.charAt(position) + "'",
                        line, column
                );
            }
        }

        tokens = addEOFToken(tokens);

        return tokens;
    }

}
