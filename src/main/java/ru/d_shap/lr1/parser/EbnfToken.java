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
package ru.d_shap.lr1.parser;

import java.io.Serializable;

import ru.d_shap.lr1.Position;

/**
 * The EBNF token.
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfToken implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Position _position;

    private final EbnfTokenType _tokenType;

    private final String _tokenValue;

    EbnfToken(final Position position, final EbnfTokenType tokenType, final String tokenValue) {
        super();
        if (position == null) {
            throw new NullPointerException("Position should not be null");
        }
        _position = position;
        if (tokenType == null) {
            throw new NullPointerException("Token type should not be null");
        }
        _tokenType = tokenType;
        if (tokenValue == null) {
            throw new NullPointerException("Token value should not be null");
        }
        _tokenValue = tokenValue;
    }

    /**
     * Get the position.
     *
     * @return the position.
     */
    public Position getPosition() {
        return _position;
    }

    /**
     * Get the EBNF token type.
     *
     * @return the EBNF token type.
     */
    public EbnfTokenType getTokenType() {
        return _tokenType;
    }

    /**
     * Get the EBNF token value.
     *
     * @return the EBNF token value.
     */
    public String getTokenValue() {
        return _tokenValue;
    }

    @Override
    public String toString() {
        return _tokenType.tokenText(_tokenValue);
    }

}
