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
 * The EBNF rule.
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfRule extends EbnfNode {

    private static final long serialVersionUID = 1L;

    private final String _name;

    private final EbnfNode _expression;

    /**
     * Create new object.
     *
     * @param line the line number.
     * @param column the column number.
     * @param name       the name of the EBNF rule.
     * @param expression the expression of the EBNF rule.
     */
    public EbnfRule(final int line, final int column, final String name, final EbnfNode expression) {
        super(line, column);
        _name = name;
        _expression = expression;
    }

    /**
     * Get the name of the EBNF rule.
     *
     * @return the name of the EBNF rule.
     */
    public String getName() {
        return _name;
    }

    /**
     * Get the expression of the EBNF rule.
     *
     * @return the expression of the EBNF rule.
     */
    public EbnfNode getExpression() {
        return _expression;
    }

}
