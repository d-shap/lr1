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
package ru.d_shap.lr1.lexer;

import java.io.Serializable;

/**
 * The token rule.
 *
 * @author Dmitry Shapovalov
 */
public abstract class TokenRule implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String _tokenType;

    /**
     * Create new object.
     *
     * @param tokenType the token type.
     */
    protected TokenRule(final String tokenType) {
        super();
        _tokenType = tokenType;
    }

    /**
     * Get the token type.
     *
     * @return the token type.
     */
    public String getTokenType() {
        return _tokenType;
    }

    /**
     * Check if the text matches the rule.
     *
     * @param text the text.
     *
     * @return the matched text, or null if no match.
     */
    public abstract String match(String text);

}
