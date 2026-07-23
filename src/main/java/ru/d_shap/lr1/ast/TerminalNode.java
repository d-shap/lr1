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

/**
 * Узел для терминала (токена).
 * Содержит информацию о типе токена и его значение.
 */
public class TerminalNode implements ASTNode {

    private final String tokenType;

    private final String tokenValue;

    private final int line;

    private final int column;

    /**
     * Создать узел терминала.
     *
     * @param tokenType  тип токена (например, "IDENTIFIER", "NUMBER", "PLUS")
     * @param tokenValue значение токена (текст из исходного кода)
     */
    public TerminalNode(final String tokenType, final String tokenValue) {
        this(tokenType, tokenValue, -1, -1);
    }

    /**
     * Создать узел терминала с информацией о позиции.
     *
     * @param tokenType  тип токена
     * @param tokenValue значение токена
     * @param line       номер строки в исходном коде
     * @param column     номер колонки в исходном коде
     */
    public TerminalNode(final String tokenType, final String tokenValue, final int line, final int column) {
        super();
        this.tokenType = Objects.requireNonNull(tokenType, "tokenType cannot be null");
        this.tokenValue = Objects.requireNonNull(tokenValue, "tokenValue cannot be null");
        this.line = line;
        this.column = column;
    }

    @Override
    public String getType() {
        return tokenType;
    }

    @Override
    public Object getValue() {
        return tokenValue;
    }

    /**
     * Получить номер строки в исходном коде.
     */
    public int getLine() {
        return line;
    }

    /**
     * Получить номер колонки в исходном коде.
     */
    public int getColumn() {
        return column;
    }

    @Override
    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visitTerminal(this);
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public ASTNode getChild(final int index) {
        throw new IndexOutOfBoundsException("TerminalNode has no children");
    }

    @Override
    public String toString() {
        return String.format("TerminalNode(%s='%s')", tokenType, tokenValue);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TerminalNode that = (TerminalNode) o;
        return line == that.line &&
                column == that.column &&
                Objects.equals(tokenType, that.tokenType) &&
                Objects.equals(tokenValue, that.tokenValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokenType, tokenValue, line, column);
    }

}
