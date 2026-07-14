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

import java.io.Serializable;

/**
 * The EBNF token.
 *
 * @author Dmitry Shapovalov
 */
final class EbnfToken implements Serializable {

    private static final long serialVersionUID = 1L;

    private final EbnfTokenType _type;

    private final String _text;

    private final int _line;

    private final int _column;

    EbnfToken(final EbnfTokenType type, final String text, final int line, final int column) {
        super();
        _type = type;
        _text = text;
        _line = line;
        _column = column;
    }

    EbnfTokenType getType() {
        return _type;
    }

    String getText() {
        return _text;
    }

    int getLine() {
        return _line;
    }

    int getColumn() {
        return _column;
    }

}
