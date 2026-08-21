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
package ru.d_shap.lr1.parser;

import java.io.Serializable;

/**
 * The EBNF token.
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfToken implements Serializable {

    private static final long serialVersionUID = 1L;

    private final EbnfTokenType _tokenType;

    private final String _tokenText;

    private final int _line;

    private final int _column;

    EbnfToken(final EbnfTokenType tokenType, final String tokenText, final int line, final int column) {
        super();
        _tokenType = tokenType;
        _tokenText = tokenText;
        _line = line;
        _column = column;
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
     * Get the EBNF token text.
     *
     * @return the EBNF token text.
     */
    public String getTokenText() {
        return _tokenText;
    }

    /**
     * Get the EBNF token line.
     *
     * @return the EBNF token line.
     */
    public int getLine() {
        return _line;
    }

    /**
     * Get the EBNF token column.
     *
     * @return the EBNF token column.
     */
    public int getColumn() {
        return _column;
    }

    @Override
    public String toString() {
        return _tokenType.stringValue(_tokenText);
    }

}
