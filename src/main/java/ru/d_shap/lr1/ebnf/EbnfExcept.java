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

import ru.d_shap.lr1.Position;

/**
 * The EBNF exception (set difference).
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfExcept extends EbnfNode {

    private static final long serialVersionUID = 1L;

    private final EbnfNode _base;

    private final EbnfNode _exception;

    /**
     * Create new object.
     *
     * @param position  the position.
     * @param base      the base expression of the EBNF exception.
     * @param exception the exception expression (to exclude) of the EBNF exception.
     */
    public EbnfExcept(final Position position, final EbnfNode base, final EbnfNode exception) {
        super(position);
        _base = base;
        _exception = exception;
    }

    /**
     * Get the base expression of the EBNF exception.
     *
     * @return the base expression of the EBNF exception.
     */
    public EbnfNode getBase() {
        return _base;
    }

    /**
     * Get the exception expression of the EBNF exception.
     *
     * @return the exception expression of the EBNF exception.
     */
    public EbnfNode getException() {
        return _exception;
    }

    @Override
    public String toString() {
        return String.format("Except(%s-%s)", _base, _exception);
    }

}
