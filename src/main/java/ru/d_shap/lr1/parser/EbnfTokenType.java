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

/**
 * The EBNF token type.
 *
 * @author Dmitry Shapovalov
 */
public enum EbnfTokenType {

    IDENTIFIER {
        @Override
        String tokenText(final String text) {
            return name() + "(" + text + ")";
        }
    },

    STRING {
        @Override
        String tokenText(final String text) {
            return name() + "(" + text + ")";
        }
    },

    EQUALS {
        @Override
        String tokenText(final String text) {
            return name();
        }
    },

    COMMA {
        @Override
        String tokenText(final String text) {
            return name();
        }
    },

    PIPE {
        @Override
        String tokenText(final String text) {
            return name();
        }
    },

    SEMICOLON {
        @Override
        String tokenText(final String text) {
            return name();
        }
    },

    LPAREN {
        @Override
        String tokenText(final String text) {
            return name();
        }
    },

    RPAREN {
        @Override
        String tokenText(final String text) {
            return name();
        }
    },

    LBRACKET {
        @Override
        String tokenText(final String text) {
            return name();
        }
    },

    RBRACKET {
        @Override
        String tokenText(final String text) {
            return name();
        }
    },

    LBRACE {
        @Override
        String tokenText(final String text) {
            return name();
        }
    },

    RBRACE {
        @Override
        String tokenText(final String text) {
            return name();
        }
    },

    QUESTION {
        @Override
        String tokenText(final String text) {
            return name();
        }
    },

    PLUS {
        @Override
        String tokenText(final String text) {
            return name();
        }
    },

    MINUS {
        @Override
        String tokenText(final String text) {
            return name();
        }
    },

    ASTERISK {
        @Override
        String tokenText(final String text) {
            return name();
        }
    },

    EOF {
        @Override
        String tokenText(final String text) {
            return name();
        }
    };

    abstract String tokenText(String text);

}
