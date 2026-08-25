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
import ru.d_shap.lr1.Position;

/**
 * Tests for {@link EbnfToken}.
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfTokenTest {

    /**
     * Test class constructor.
     */
    public EbnfTokenTest() {
        super();
    }

    /**
     * {@link EbnfToken} class test.
     */
    @Test
    public void constructorTest() {
        Position position = new Position(1, 1);
        new EbnfToken(position, EbnfTokenType.ASTERISK, "*");

        try {
            new EbnfToken(null, EbnfTokenType.ASTERISK, "*");
            Assertions.fail("EbnfToken test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Position should not be null");
        }
        try {
            new EbnfToken(position, null, "*");
            Assertions.fail("EbnfToken test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Token type should not be null");
        }
        try {
            new EbnfToken(position, EbnfTokenType.ASTERISK, null);
            Assertions.fail("EbnfToken test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Token value should not be null");
        }
    }

    /**
     * {@link EbnfToken} class test.
     */
    @Test
    public void getPositionTest() {
        Position position = new Position(1, 1);
        EbnfToken token = new EbnfToken(position, EbnfTokenType.ASTERISK, "*");
        Assertions.assertThat(token.getPosition()).isSameAs(position);
    }

    /**
     * {@link EbnfToken} class test.
     */
    @Test
    public void getTokenTypeTest() {
        Position position = new Position(1, 1);
        EbnfToken token = new EbnfToken(position, EbnfTokenType.ASTERISK, "*");
        Assertions.assertThat(token.getTokenType()).isEqualTo(EbnfTokenType.ASTERISK);
    }

    /**
     * {@link EbnfToken} class test.
     */
    @Test
    public void getTokenValueTest() {
        Position position = new Position(1, 1);
        EbnfToken token = new EbnfToken(position, EbnfTokenType.ASTERISK, "*");
        Assertions.assertThat(token.getTokenValue()).isEqualTo("*");
    }

    /**
     * {@link EbnfToken} class test.
     */
    @Test
    public void toStringTest() {
        Position position = new Position(1, 1);

        EbnfToken ebnfToken1 = new EbnfToken(position, EbnfTokenType.IDENTIFIER, "value");
        Assertions.assertThat(ebnfToken1).hasToString("IDENTIFIER(value)");

        EbnfToken ebnfToken2 = new EbnfToken(position, EbnfTokenType.EQUALS, "=");
        Assertions.assertThat(ebnfToken2).hasToString("EQUALS");

        EbnfToken ebnfToken3 = new EbnfToken(position, EbnfTokenType.ASTERISK, "*");
        Assertions.assertThat(ebnfToken3).hasToString("ASTERISK");
    }

    /**
     * {@link EbnfToken} class test.
     */
    @Test
    public void serializationTest() {
        // TODO
    }

}
