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
 * The EBNF token type.
 *
 * @author Dmitry Shapovalov
 */
public enum EbnfTokenType {

    IDENTIFIER {
        @Override
        String stringValue(final String tokenText) {
            return name() + "(" + tokenText + ")";
        }
    },

    STRING {
        @Override
        String stringValue(final String tokenText) {
            return name() + "(" + tokenText + ")";
        }
    },

    EQUALS {
        @Override
        String stringValue(final String tokenText) {
            return name();
        }
    },

    COMMA {
        @Override
        String stringValue(final String tokenText) {
            return name();
        }
    },

    PIPE {
        @Override
        String stringValue(final String tokenText) {
            return name();
        }
    },

    SEMICOLON {
        @Override
        String stringValue(final String tokenText) {
            return name();
        }
    },

    LPAREN {
        @Override
        String stringValue(final String tokenText) {
            return name();
        }
    },

    RPAREN {
        @Override
        String stringValue(final String tokenText) {
            return name();
        }
    },

    LBRACKET {
        @Override
        String stringValue(final String tokenText) {
            return name();
        }
    },

    RBRACKET {
        @Override
        String stringValue(final String tokenText) {
            return name();
        }
    },

    LBRACE {
        @Override
        String stringValue(final String tokenText) {
            return name();
        }
    },

    RBRACE {
        @Override
        String stringValue(final String tokenText) {
            return name();
        }
    },

    QUESTION {
        @Override
        String stringValue(final String tokenText) {
            return name();
        }
    },

    PLUS {
        @Override
        String stringValue(final String tokenText) {
            return name();
        }
    },

    MINUS {
        @Override
        String stringValue(final String tokenText) {
            return name();
        }
    },

    ASTERISK {
        @Override
        String stringValue(final String tokenText) {
            return name();
        }
    },

    EOF {
        @Override
        String stringValue(final String tokenText) {
            return name();
        }
    };

    abstract String stringValue(String tokenText);

}
