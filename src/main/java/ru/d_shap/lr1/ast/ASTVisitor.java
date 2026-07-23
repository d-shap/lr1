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
 * Интерфейс для обхода и обработки AST.
 * Реализует паттерн Visitor для универсального обхода дерева без привязки к грамматике.
 *
 * @param <T> тип результата обхода
 */
public interface ASTVisitor<T> {

    /**
     * Посетить узел правила (нетерминала).
     *
     * @param node узел правила
     *
     * @return результат обхода узла
     */
    T visitRule(RuleNode node);

    /**
     * Посетить узел терминала (токена).
     *
     * @param node узел терминала
     *
     * @return результат обхода узла
     */
    T visitTerminal(TerminalNode node);

    /**
     * Базовая реализация visitor с поддержкой рекурсивного обхода.
     */
    abstract class AbstractVisitor<T> implements ASTVisitor<T> {

        /**
         * Обойти все дочерние узлы.
         *
         * @param node родительский узел
         *
         * @return список результатов обхода дочерних узлов
         */
        protected List<T> visitChildren(final RuleNode node) {
            List<T> results = new ArrayList<>();
            for (ASTNode child : node.getChildren()) {
                results.add(child.accept(this));
            }
            return results;
        }
    }

}
