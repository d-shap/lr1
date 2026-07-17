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
package ru.d_shap.lr1.ebnf;

/**
 * The EBNF validation exception.
 *
 * @author Dmitry Shapovalov
 */
public class EbnfValidationException extends EbnfException {

    private static final long serialVersionUID = 1L;

    private final int _line;

    private final int _column;

    /**
     * Create new object.
     *
     * @param line the line number.
     * @param column the column number.
     * @param message the message.
     */
    public EbnfValidationException(final int line, final int column, final String message) {
        super(message);
        _line = line;
        _column = column;
    }

    /**
     * Get the line number.
     *
     * @return the line number.
     */
    public int getLine() {
        return _line;
    }

    /**
     * Get the column number.
     *
     * @return the column number.
     */
    public int getColumn() {
        return _column;
    }

}
