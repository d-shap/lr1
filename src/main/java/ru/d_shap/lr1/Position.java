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
package ru.d_shap.lr1;

import java.io.Serializable;

/**
 * The position.
 *
 * @author Dmitry Shapovalov
 */
public final class Position implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int _line;

    private final int _column;

    /**
     * Create new object.
     *
     * @param line   the line.
     * @param column the column.
     */
    public Position(final int line, final int column) {
        super();
        if (line < 0) {
            throw new IllegalArgumentException("Line should not be negative");
        }
        _line = line;
        if (column < 0) {
            throw new IllegalArgumentException("Column should not be negative");
        }
        _column = column;
    }

    /**
     * Get the line.
     *
     * @return the line.
     */
    public int getLine() {
        return _line;
    }

    /**
     * Get the column.
     *
     * @return the column.
     */
    public int getColumn() {
        return _column;
    }

    @Override
    public String toString() {
        return String.format("line %s, column %s", _line, _column);
    }

}
