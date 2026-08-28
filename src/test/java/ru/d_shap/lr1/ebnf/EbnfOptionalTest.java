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

import org.junit.Test;

import ru.d_shap.assertions.Assertions;
import ru.d_shap.assertions.util.SerializationHelper;
import ru.d_shap.lr1.Position;

/**
 * Tests for {@link EbnfOptional}.
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfOptionalTest {

    /**
     * Test class constructor.
     */
    public EbnfOptionalTest() {
        super();
    }

    /**
     * {@link EbnfOptional} class test.
     */
    @Test
    public void constructorTest() {
        Position position = new Position(0, 0);
        EbnfNode node = new EbnfTerminal(position, "a");
        new EbnfOptional(position, node);

        try {
            new EbnfOptional(null, node);
            Assertions.fail("EbnfOptional test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Position should not be null");
        }
        try {
            new EbnfOptional(position, null);
            Assertions.fail("EbnfOptional test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Expression should not be null");
        }
    }

    /**
     * {@link EbnfOptional} class test.
     */
    @Test
    public void getExpressionTest() {
        Position position = new Position(0, 0);
        EbnfNode node = new EbnfTerminal(position, "a");
        EbnfOptional optional = new EbnfOptional(position, node);
        Assertions.assertThat(optional.getExpression()).isSameAs(node);
    }

    /**
     * {@link EbnfOptional} class test.
     */
    @Test
    public void toStringTest() {
        Position position = new Position(0, 0);
        EbnfNode node = new EbnfTerminal(position, "a");
        EbnfOptional optional = new EbnfOptional(position, node);
        Assertions.assertThat(optional).hasToString("Optional(Terminal(a))");
    }

    /**
     * {@link EbnfOptional} class test.
     */
    @Test
    public void serializationTest() {
        Position position = new Position(10, 20);
        EbnfNode node = new EbnfTerminal(position, "a");
        EbnfOptional optional = new EbnfOptional(position, node);
        EbnfOptional deserialized = SerializationHelper.serializeAndDeserialize(optional);
        Assertions.assertThat(deserialized.getLine()).isEqualTo(10);
        Assertions.assertThat(deserialized.getColumn()).isEqualTo(20);
        Assertions.assertThat(deserialized).hasToString("Optional(Terminal(a))");
    }

}
