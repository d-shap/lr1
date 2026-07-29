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

/**
 * The character token rule.
 *
 * @author Dmitry Shapovalov
 */
public final class CharacterTokenRule extends TokenRule {

    private static final long serialVersionUID = 1L;

    private final char _character;

    private final String _string;

    /**
     * Create new object.
     *
     * @param tokenType the token type.
     * @param character the character.
     */
    public CharacterTokenRule(final String tokenType, final char character) {
        super(tokenType);
        _character = character;
        _string = String.valueOf(_character);
    }

    @Override
    public String match(final String text) {
        if (text.length() > 0 && text.charAt(0) == _character) {
            return _string;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "TokenRule{tokenType='" + getTokenType() + "', character='" + _character + "'}";
    }

}
