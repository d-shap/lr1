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

import java.util.List;

/**
 * Интерфейс для токенизации (лексического анализа) исходного кода.
 * Реализации должны преобразовывать текст в последовательность токенов.
 */
public interface Tokenizer {

    /**
     * Токенизировать текст и вернуть список токенов.
     *
     * @param text исходный текст для токенизации
     *
     * @return список токенов
     *
     * @throws TokenizerException если произошла ошибка при токенизации
     */
    List<Token> tokenize(String text) throws TokenizerException;

    /**
     * Исключение при ошибке токенизации.
     */
    class TokenizerException extends Exception {

        private static final long serialVersionUID = 1L;

        private final int line;

        private final int column;

        public TokenizerException(final String message, final int line, final int column) {
            super(message);
            this.line = line;
            this.column = column;
        }

        public TokenizerException(final String message, final Throwable cause, final int line, final int column) {
            super(message, cause);
            this.line = line;
            this.column = column;
        }

        public int getLine() {
            return line;
        }

        public int getColumn() {
            return column;
        }

        @Override
        public String toString() {
            return String.format("%s at [%d:%d]", getMessage(), line, column);
        }
    }

    /**
     * Базовая реализация токенизатора для простых грамматик.
     * Может быть переопределена для поддержки специальных правил лексического анализа.
     */
    abstract class BaseTokenizer implements Tokenizer {

        /**
         * Добавить специальный токен конца файла (EOF).
         *
         * @param tokens список токенов
         *
         * @return список с добавленным EOF токеном
         */
        protected List<Token> addEOFToken(final List<Token> tokens) {
            if (tokens.isEmpty()) {
                tokens.add(new Token("EOF", "", 1, 1, 0));
            } else {
                Token lastToken = tokens.get(tokens.size() - 1);
                int eofPosition = lastToken.getPosition() + lastToken.getValue().length();
                int eofLine = lastToken.getLine();
                int eofColumn = lastToken.getColumn() + lastToken.getValue().length();
                tokens.add(new Token("EOF", "", eofLine, eofColumn, eofPosition));
            }
            return tokens;
        }
    }

}
