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
     * @param position   the position.
     * @param name       the name.
     * @param expression the expression.
     */
    public EbnfRule(final Position position, final String name, final EbnfNode expression) {
        super(position);
        if (name == null) {
            throw new NullPointerException("Name should not be null");
        }
        _name = name;
        if (expression == null) {
            throw new NullPointerException("Expression should not be null");
        }
        _expression = expression;
    }

    /**
     * Get the name.
     *
     * @return the name.
     */
    public String getName() {
        return _name;
    }

    /**
     * Get the expression.
     *
     * @return the expression.
     */
    public EbnfNode getExpression() {
        return _expression;
    }

    @Override
    public String toString() {
        return String.format("Rule(%s=%s)", _name, _expression);
    }

}
