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

/**
 * The EBNF rule reference.
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfRuleReference implements EbnfNode {

    private static final long serialVersionUID = 1L;

    private final String _name;

    /**
     * Create new object.
     *
     * @param name the name of the EBNF rule.
     */
    public EbnfRuleReference(final String name) {
        super();
        _name = name;
    }

    /**
     * Get the name of the EBNF rule.
     *
     * @return the name of the EBNF rule.
     */
    public String getName() {
        return _name;
    }

}
