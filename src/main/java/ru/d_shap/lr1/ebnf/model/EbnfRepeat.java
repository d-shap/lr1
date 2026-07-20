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
package ru.d_shap.lr1.ebnf.model;

/**
 * The EBNF repeat.
 * <p>
 * Supports:
 * - "*" (zero or more repetitions)
 * - "+" (one or more repetitions)
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfRepeat extends EbnfNode {

    private static final long serialVersionUID = 1L;

    private final EbnfNode _expression;

    private final String _operator;

    /**
     * Create new object with default operator "*".
     *
     * @param line       the line number.
     * @param column     the column number.
     * @param expression the expression of the EBNF repeat.
     */
    public EbnfRepeat(final int line, final int column, final EbnfNode expression) {
        this(line, column, expression, "*");
    }

    /**
     * Create new object.
     *
     * @param line       the line number.
     * @param column     the column number.
     * @param expression the expression of the EBNF repeat.
     * @param operator   the repeat operator ("*" or "+").
     */
    public EbnfRepeat(final int line, final int column, final EbnfNode expression, final String operator) {
        super(line, column);
        _expression = expression;
        if ("*".equals(operator) || "+".equals(operator)) {
            _operator = operator;
        } else {
            _operator = "*"; // Default to "*" if invalid operator
        }
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
    public String getOperator() {
        return _operator;
    }

}
