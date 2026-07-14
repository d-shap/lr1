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

/**
 * The EBNF sequence.
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfSequence implements EbnfNode {

    private static final long serialVersionUID = 1L;

    private final List<EbnfNode> _expressions;

    /**
     * Create new object.
     *
     * @param expressions the expressions of the EBNF sequence.
     */
    public EbnfSequence(final List<EbnfNode> expressions) {
        super();
        if (expressions == null) {
            _expressions = null;
        } else {
            _expressions = new ArrayList<>(expressions);
        }
    }

    /**
     * Get the number of the expressions in the EBNF sequence.
     *
     * @return the number of the expressions in the EBNF sequence.
     */
    public int getCount() {
        if (_expressions == null) {
            return 0;
        } else {
            return _expressions.size();
        }
    }

    /**
     * Get the expression of the EBNF sequence at the specified index.
     *
     * @param index the specified index.
     *
     * @return the expression of the EBNF sequence at the specified index.
     */
    public EbnfNode getExpression(final int index) {
        if (_expressions == null) {
            return null;
        }
        if (index < 0 || index >= _expressions.size()) {
            return null;
        }
        return _expressions.get(index);
    }

}
