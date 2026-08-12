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
package ru.d_shap.lr1.model;

/**
 * The EBNF terminal.
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfTerminal extends EbnfNode {

    private static final long serialVersionUID = 1L;

    private final String _value;

    /**
     * Create new object.
     *
     * @param position the position.
     * @param value    the value of the EBNF terminal.
     */
    public EbnfTerminal(final Position position, final String value) {
        super(position);
        _value = value;
    }

    /**
     * Get the value of the EBNF terminal.
     *
     * @return the value of the EBNF terminal.
     */
    public String getValue() {
        return _value;
    }

    @Override
    public String toString() {
        Position position = getPosition();
        return String.format("Terminal(%s) %s", _value, position);
    }

}
