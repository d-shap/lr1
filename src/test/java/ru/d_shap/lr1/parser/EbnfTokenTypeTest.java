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

import org.junit.Test;

import ru.d_shap.assertions.Assertions;

/**
 * Tests for {@link EbnfTokenType}.
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfTokenTypeTest {

    /**
     * Test class constructor.
     */
    public EbnfTokenTypeTest() {
        super();
    }

    /**
     * {@link EbnfTokenType} class test.
     */
    @Test
    public void valueCountTest() {
        Assertions.assertThat(EbnfTokenType.class).asEnum().hasValueCount(17);
    }

    /**
     * {@link EbnfTokenType} class test.
     */
    @Test
    public void tokenTextTest() {
        Assertions.assertThat(EbnfTokenType.IDENTIFIER.tokenText("value")).isEqualTo("IDENTIFIER(value)");
        Assertions.assertThat(EbnfTokenType.STRING.tokenText("value")).isEqualTo("STRING(value)");
        Assertions.assertThat(EbnfTokenType.EQUALS.tokenText("value")).isEqualTo("EQUALS");
        Assertions.assertThat(EbnfTokenType.COMMA.tokenText("value")).isEqualTo("COMMA");
        Assertions.assertThat(EbnfTokenType.PIPE.tokenText("value")).isEqualTo("PIPE");
        Assertions.assertThat(EbnfTokenType.SEMICOLON.tokenText("value")).isEqualTo("SEMICOLON");
        Assertions.assertThat(EbnfTokenType.LPAREN.tokenText("value")).isEqualTo("LPAREN");
        Assertions.assertThat(EbnfTokenType.RPAREN.tokenText("value")).isEqualTo("RPAREN");
        Assertions.assertThat(EbnfTokenType.LBRACKET.tokenText("value")).isEqualTo("LBRACKET");
        Assertions.assertThat(EbnfTokenType.RBRACKET.tokenText("value")).isEqualTo("RBRACKET");
        Assertions.assertThat(EbnfTokenType.LBRACE.tokenText("value")).isEqualTo("LBRACE");
        Assertions.assertThat(EbnfTokenType.RBRACE.tokenText("value")).isEqualTo("RBRACE");
        Assertions.assertThat(EbnfTokenType.QUESTION.tokenText("value")).isEqualTo("QUESTION");
        Assertions.assertThat(EbnfTokenType.PLUS.tokenText("value")).isEqualTo("PLUS");
        Assertions.assertThat(EbnfTokenType.MINUS.tokenText("value")).isEqualTo("MINUS");
        Assertions.assertThat(EbnfTokenType.ASTERISK.tokenText("value")).isEqualTo("ASTERISK");
        Assertions.assertThat(EbnfTokenType.EOF.tokenText("value")).isEqualTo("EOF");
    }

}
