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
 * The EBNF special sequence.
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfSpecialSequence implements EbnfNode {

    private static final long serialVersionUID = 1L;

    private final String _text;

    /**
     * Create new object.
     *
     * @param text the text of the EBNF special sequence.
     */
    public EbnfSpecialSequence(final String text) {
        super();
        _text = text;
    }

    /**
     * Get the text of the EBNF special sequence.
     *
     * @return the text of the EBNF special sequence.
     */
    public String getText() {
        return _text;
    }

}
