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

import java.util.Objects;

/**
 * Представляет токен (терминал) в исходном коде.
 * Содержит информацию о типе токена, его значении и позиции в исходном коде.
 */
public class Token {

    private final String type;

    private final String value;

    private final int line;

    private final int column;

    private final int position;

    /**
     * Создать токен.
     *
     * @param type     тип токена (например, "IDENTIFIER", "NUMBER", "KEYWORD")
     * @param value    значение токена (текст из исходного кода)
     * @param line     номер строки (1-based)
     * @param column   номер колонки (1-based)
     * @param position абсолютная позиция в тексте (0-based)
     */
    public Token(final String type, final String value, final int line, final int column, final int position) {
        super();
        this.type = Objects.requireNonNull(type, "type cannot be null");
        this.value = Objects.requireNonNull(value, "value cannot be null");
        this.line = line;
        this.column = column;
        this.position = position;
    }

    /**
     * Получить тип токена.
     */
    public String getType() {
        return type;
    }

    /**
     * Получить значение токена.
     */
    public String getValue() {
        return value;
    }

    /**
     * Получить номер строки.
     */
    public int getLine() {
        return line;
    }

    /**
     * Получить номер колонки.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Получить абсолютную позицию в тексте.
     */
    public int getPosition() {
        return position;
    }

    /**
     * Получить строковое представление позиции.
     */
    public String getLocationString() {
        return String.format("[%d:%d]", line, column);
    }

    @Override
    public String toString() {
        return String.format("Token('%s'='%s' at %s)", type, value, getLocationString());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Token token = (Token) o;
        return line == token.line &&
                column == token.column &&
                position == token.position &&
                Objects.equals(type, token.type) &&
                Objects.equals(value, token.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value, line, column, position);
    }

}
