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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.d_shap.lr1.state.Production;

/**
 * Универсальный токенайзер, полностью управляемый грамматикой.
 * Токенайзер просто создаёт правила для всех терминалов из грамматики,
 * без каких-либо "волшебных" правил (NUMBER, IDENTIFIER и т.д.).
 */
public class GrammarTokenizer extends Tokenizer.BaseTokenizer {

    /**
     * Правило для распознавания токена (паттерн + тип).
     */
    public static class TokenRule {

        private final String tokenType;

        private final Pattern pattern;

        public TokenRule(final String tokenType, final String regex) {
            super();
            this.tokenType = tokenType;
            pattern = Pattern.compile("^" + regex);
        }

        public String getTokenType() {
            return tokenType;
        }

        public Pattern getPattern() {
            return pattern;
        }
    }

    private final List<TokenRule> tokenRules;

    /**
     * Создать токенайзер.
     */
    public GrammarTokenizer() {
        super();
        tokenRules = new ArrayList<>();
    }

    /**
     * Добавить правило для распознавания токена.
     * Порядок добавления важен - правила проверяются по порядку!
     *
     * @param tokenType тип токена
     * @param regex     регулярное выражение для распознавания
     */
    public void addTokenRule(final String tokenType, final String regex) {
        tokenRules.add(new TokenRule(tokenType, regex));
    }

    /**
     * Добавить несколько правил сразу.
     *
     * @param rules пары (тип, регулярное выражение)
     */
    public void addTokenRules(final String... rules) {
        if (rules.length % 2 != 0) {
            throw new IllegalArgumentException("Rules must come in pairs: type, regex, type, regex, ...");
        }
        for (int i = 0; i < rules.length; i += 2) {
            addTokenRule(rules[i], rules[i + 1]);
        }
    }

    /**
     * Инициализировать токенайзер из грамматики.
     * Автоматически добавляет правила для всех терминалов из грамматики.
     *
     * @param grammarMap     карта нетерминалов к их продукциям
     * @param allProductions список всех продукций
     */
    public void initializeFromGrammar(final Map<String, List<Production>> grammarMap,
                                      final List<Production> allProductions) {
        // Собрать все терминалы из грамматики
        Set<String> terminals = extractTerminals(grammarMap, allProductions);

        // Добавить терминалы из грамматики в правильном порядке
        addTerminalRules(terminals);
    }

    /**
     * Извлечь все терминалы из грамматики.
     * Терминал - это символ, который не является нетерминалом.
     *
     * @param grammarMap     карта нетерминалов
     * @param allProductions все продукции
     *
     * @return множество терминалов
     */
    private Set<String> extractTerminals(final Map<String, List<Production>> grammarMap,
                                         final List<Production> allProductions) {
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

        // Классифицировать терминалы
        for (String terminal : terminals) {
            if ("$".equals(terminal)) {
                continue;
            }

            if (terminal.length() > 1) {
                if (isAlphaNumeric(terminal)) {
                    keywordTerminals.add(terminal);
                } else {
                    multiCharOperators.add(terminal);
                }
            } else {
                singleCharOperators.add(terminal);
            }
        }

        // Сортировать для консистентности
        Collections.sort(multiCharOperators);
        Collections.sort(singleCharOperators);
        Collections.sort(keywordTerminals);

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
            addTokenRule(op, Pattern.quote(op));
        }

        // 2. Ключевые слова (с проверкой границ слова)
        for (String keyword : keywordTerminals) {
            addTokenRule(keyword, "\\b" + Pattern.quote(keyword) + "\\b");
        }

        // 3. Одиночные символы
        for (String op : singleCharOperators) {
            addTokenRule(op, Pattern.quote(op));
        }
    }

    /**
     * Проверить, состоит ли строка из букв, цифр и подчёркивания.
     *
     * @param str строка для проверки
     *
     * @return true, если это валидный идентификатор
     */
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
                Matcher matcher = rule.getPattern().matcher(remaining);

                if (matcher.find()) {
                    String matchedText = matcher.group(0);
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
