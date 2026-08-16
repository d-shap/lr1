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
 * The EBNF repeat.
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfRepeat extends EbnfNode {

    private static final long serialVersionUID = 1L;

    private final EbnfNode _expression;

    private final EbnfRepeatOperator _repeatOperator;

    /**
     * Create new object.
     *
     * @param position       the position.
     * @param expression     the expression.
     * @param repeatOperator the repeat operator.
     */
    public EbnfRepeat(final Position position, final EbnfNode expression, final EbnfRepeatOperator repeatOperator) {
        super(position);
        if (expression == null) {
            throw new NullPointerException("Expression should not be null");
        }
        _expression = expression;
        if (repeatOperator == null) {
            throw new NullPointerException("Repeat operator should not be null");
        }
        _repeatOperator = repeatOperator;
    }

    /**
     * Get the expression of the EBNF repeat.
     *
     * @return the expression of the EBNF repeat.
     */
    public EbnfNode getExpression() {
        return _expression;
    }

    /**
     * Get the repeat operator.
     *
     * @return "*" for zero or more, "+" for one or more.
     */
    public EbnfRepeatOperator getOperator() {
        return _repeatOperator;
    }

    @Override
    public String toString() {
        return String.format("Repeat(%s%s)", _expression, _repeatOperator.getOperator());
    }

}
