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

    private final List<EbnfNode> _elements;

    /**
     * Create new object.
     *
     * @param elements the elements of the EBNF sequence.
     */
    public EbnfSequence(final List<EbnfNode> elements) {
        super();
        if (elements == null) {
            _elements = null;
        } else {
            _elements = new ArrayList<>(elements);
        }
    }

    /**
     * Get the size of the EBNF sequence.
     *
     * @return the size of the EBNF sequence.
     */
    public int size() {
        if (_elements == null) {
            return 0;
        } else {
            return _elements.size();
        }
    }

    /**
     * Get the element of the EBNF sequence at the specified index.
     *
     * @param index the specified index.
     *
     * @return the element of the EBNF sequence at the specified index.
     */
    public EbnfNode getEbnfNode(final int index) {
        if (_elements == null) {
            return null;
        }
        if (index < 0 || index >= _elements.size()) {
            return null;
        }
        return _elements.get(index);
    }

}
