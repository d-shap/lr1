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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Универсальный токенайзер, настраиваемый через правила токенизации.
 * Поддерживает регулярные выражения для распознавания различных типов токенов.
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

    @Override
    public List<Token> tokenize(final String text) throws TokenizerException {
        List<Token> tokens = new ArrayList<>();
        int position = 0;
        int line = 1;
        int column = 1;

        while (position < text.length()) {
            // Попробовать применить каждое правило
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
