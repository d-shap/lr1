///////////////////////////////////////////////////////////////////////////////////////////////////
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
///////////////////////////////////////////////////////////////////////////////////////////////////
package ru.d_shap.lr1.source;

import java.io.Serializable;

/**
 * The char consumer with the next char.
 *
 * @param <R> the generic type of the result.
 *
 * @author Dmitry Shapovalov
 */
public interface CharConsumer2<R> extends Serializable {

    /**
     * Reset the consumer.
     */
    void reset();

    /**
     * Accept the char.
     *
     * @param line   the line.
     * @param column the column.
     * @param ch     the char.
     * @param next   the next char.
     */
    void accept(int line, int column, int ch, int next);

    /**
     * Get the result.
     *
     * @return the result.
     */
    R getResult();

}
