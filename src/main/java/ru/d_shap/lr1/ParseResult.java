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

import ru.d_shap.lr1.ast.ASTNode;

/**
 * Результат парсинга содержит построенное AST и информацию об успехе/ошибке.
 */
public class ParseResult {

    private final boolean success;

    private final ASTNode ast;

    private final String errorMessage;

    private final int errorLine;

    private final int errorColumn;

    /**
     * Создать успешный результат парсинга.
     *
     * @param ast корневой узел построенного дерева
     */
    public ParseResult(final ASTNode ast) {
        super();
        success = true;
        this.ast = Objects.requireNonNull(ast, "ast cannot be null");
        errorMessage = null;
        errorLine = -1;
        errorColumn = -1;
    }

    /**
     * Создать результат с ошибкой.
     *
     * @param errorMessage сообщение об ошибке
     * @param errorLine    номер строки ошибки
     * @param errorColumn  номер колонки ошибки
     */
    public ParseResult(final String errorMessage, final int errorLine, final int errorColumn) {
        super();
        success = false;
        ast = null;
        this.errorMessage = Objects.requireNonNull(errorMessage, "errorMessage cannot be null");
        this.errorLine = errorLine;
        this.errorColumn = errorColumn;
    }

    /**
     * Проверить, был ли парсинг успешным.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Получить корневой узел AST (если парсинг успешен).
     *
     * @return корневой узел AST
     *
     * @throws IllegalStateException если парсинг был неуспешным
     */
    public ASTNode getAST() {
        if (!success) {
            throw new IllegalStateException("Cannot get AST: parsing failed with error: " + errorMessage);
        }
        return ast;
    }

    /**
     * Получить сообщение об ошибке (если парсинг неуспешен).
     *
     * @return сообщение об ошибке или null, если парсинг успешен
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Получить номер строки с ошибкой.
     */
    public int getErrorLine() {
        return errorLine;
    }

    /**
     * Получить номер колонки с ошибкой.
     */
    public int getErrorColumn() {
        return errorColumn;
    }

    /**
     * Получить строку с информацией об ошибке.
     */
    public String getErrorLocationString() {
        if (!success && errorLine >= 0) {
            return String.format("[%d:%d]", errorLine, errorColumn);
        }
        return "";
    }

    @Override
    public String toString() {
        if (success) {
            return "ParseResult(SUCCESS)";
        } else {
            return String.format("ParseResult(ERROR: %s at %s)", errorMessage, getErrorLocationString());
        }
    }

}
