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
package ru.d_shap.lr1.ebnf;

import java.io.Serializable;

/**
 * The EBNF node.
 *
 * @author Dmitry Shapovalov
 */
public class EbnfNode implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Position _position;

    /**
     * Create new object.
     *
     * @param position the position.
     */
    protected EbnfNode(final Position position) {
        super();
        if (position == null) {
            throw new NullPointerException("Position should not be null");
        }
        _position = position;
    }

    /**
     * Get the position.
     *
     * @return the position.
     */
    public final Position getPosition() {
        return _position;
    }

    /**
     * Get the line.
     *
     * @return the line.
     */
    public final int getLine() {
        return _position.getLine();
    }

    /**
     * Get the column.
     *
     * @return the column.
     */
    public final int getColumn() {
        return _position.getColumn();
    }

}
