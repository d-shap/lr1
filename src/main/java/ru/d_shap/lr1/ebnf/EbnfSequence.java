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

import java.util.ArrayList;
import java.util.List;

import ru.d_shap.lr1.Position;

/**
 * The EBNF sequence.
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfSequence extends EbnfNode {

    private static final long serialVersionUID = 1L;

    private final List<EbnfNode> _expressions;

    /**
     * Create new object.
     *
     * @param position    the position.
     * @param expressions the expressions.
     */
    public EbnfSequence(final Position position, final List<EbnfNode> expressions) {
        super(position);
        if (expressions == null) {
            throw new NullPointerException("Expressions should not be null");
        }
        _expressions = new ArrayList<>(expressions);
    }

    /**
     * Get the number of the expressions.
     *
     * @return the number of the expressions.
     */
    public int getCount() {
        return _expressions.size();
    }

    /**
     * Get the expression at the specified index.
     *
     * @param index the specified index.
     *
     * @return the expression at the specified index.
     */
    public EbnfNode getExpression(final int index) {
        int size = _expressions.size();
        if (index < 0 || index >= size) {
            String message = String.format("Index %s should be in bounds [0, %s)", index, size);
            throw new IndexOutOfBoundsException(message);
        }
        return _expressions.get(index);
    }

    @Override
    public String toString() {
        return String.format("Sequence(%s)", _expressions);
    }

}
