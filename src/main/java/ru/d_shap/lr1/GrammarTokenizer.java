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
 * Универсальный токенайзер, настраиваемый через правила токенизации.
 * Поддерживает регулярные выражения для распознавания различных типов токенов.
 * Может быть инициализирован автоматически из грамматики.
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
     * Сначала добавляются общие паттерны (числа, идентификаторы), затем специфичные терминалы.
     *
     * @param grammarMap     карта нетерминалов к их продукциям
     * @param allProductions список всех продукций
     */
    public void initializeFromGrammar(final Map<String, List<Production>> grammarMap,
                                      final List<Production> allProductions) {
        // Собрать все терминалы из грамматики
        Set<String> terminals = extractTerminals(grammarMap, allProductions);

        // Добавить общие паттерны в начало (они более приоритетны)
        addCommonPatterns();

        // Добавить терминалы из грамматики в специфическом порядке
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
                // Если символ не является нетерминалом, это терминал
                if (!nonTerminals.contains(symbol)) {
                    terminals.add(symbol);
                }
            }
        }

        return terminals;
    }

    /**
     * Добавить общие паттерны токенизации (цифры, точка, идентификаторы).
     */
    private void addCommonPatterns() {
        // Пробелы и комментарии (должны пропускаться)
        addTokenRule("WHITESPACE", "\\s+");

        // Цифры (отдельные)
        for (char c = '0'; c <= '9'; c++) {
            addTokenRule(String.valueOf(c), Pattern.quote(String.valueOf(c)));
        }

        // Точка для decimal_part
        addTokenRule(".", Pattern.quote("."));

        // Идентификаторы (для функций: sin, cos, tan и т.д.)
        addTokenRule("IDENTIFIER", "[a-zA-Z_][a-zA-Z0-9_]*");
    }

    /**
     * Добавить правила для терминалов из грамматики.
     * Более длинные терминалы добавляются в начало, чтобы они совпадали в первую очередь.
     *
     * @param terminals множество терминалов
     */
    private void addTerminalRules(final Set<String> terminals) {
        // Отделить операторы от других терминалов
        List<String> operators = new ArrayList<>();
        List<String> otherTerminals = new ArrayList<>();

        for (String terminal : terminals) {
            // Исключить синтетические терминалы (например, '$')
            if ("$".equals(terminal)) {
                continue;
            }

            // Пропустить цифры и точку - они уже добавлены в addCommonPatterns()
            if (terminal.length() == 1 && (terminal.matches("\\d") || ".".equals(terminal))) {
                continue;
            }

            // Операторы - это короткие спецсимволы
            if (isOperator(terminal)) {
                operators.add(terminal);
            } else {
                otherTerminals.add(terminal);
            }
        }

        // Сортировать операторы по длине (длинные в начало)

        Collections.sort(operators, new Comparator<String>() {

            @Override

            public int compare(final String a, final String b) {

                return Integer.compare(b.length(), a.length());

            }

        });

        // Добавить операторы первыми (они имеют высший приоритет)

        for (String op : operators) {

            String escapedOp = Pattern.quote(op);

            addTokenRule(op, escapedOp);

        }

        // Добавить остальные терминалы (ключевые слова для функций)

        for (String terminal : otherTerminals) {

            // Ключевые слова должны быть до IDENTIFIER для корректного совпадения

            // Добавляем правило с проверкой границы слова

            addTokenRule(terminal, "\\b" + Pattern.quote(terminal) + "\\b");

        }
    }

    /**
     * Проверить, является ли строка оператором (спецсимволом).
     *
     * @param str строка для проверки
     *
     * @return true, если это оператор
     */
    private boolean isOperator(final String str) {
        // Операторы - это строки, состоящие из спецсимволов
        return str.matches("[^a-zA-Z0-9_$]+");
    }

    /**
     * Проверить, является ли строка ключевым словом.
     *
     * @param str строка для проверки
     *
     * @return true, если это выглядит как ключевое слово
     */
    private boolean isKeyword(final String str) {
        // Ключевые слова - это идентификаторы, которые не состоят только из букв/цифр
        return str.matches("[a-zA-Z][a-zA-Z0-9]*");
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
            // Попробовать применить каждое правило по порядку
            boolean matched = false;

            for (TokenRule rule : tokenRules) {
                String remaining = text.substring(position);
                Matcher matcher = rule.getPattern().matcher(remaining);

                if (matcher.find()) {
                    String matchedText = matcher.group(0);
                    String tokenType = rule.getTokenType();

                    // Создать токен
                    Token token = new Token(tokenType, matchedText, line, column, position);
                    tokens.add(token);

                    // Обновить позицию и координаты
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
                    break; // Перейти к следующему символу
                }
            }

            if (!matched) {
                // Неизвестный символ
                throw new TokenizerException(
                        "Unexpected character: '" + text.charAt(position) + "'",
                        line, column
                );
            }
        }

        // Отфильтровать пропускаемые токены
        tokens = filterSkipTokens(tokens);

        // Добавить EOF
        tokens = addEOFToken(tokens);

        return tokens;
    }

    @Override
    protected List<String> getSkipTokenTypes() {
        List<String> skipTypes = new ArrayList<>();
        skipTypes.add("WHITESPACE");
        skipTypes.add("COMMENT");
        skipTypes.add("LINE_COMMENT");
        skipTypes.add("BLOCK_COMMENT");
        return skipTypes;
    }

}
