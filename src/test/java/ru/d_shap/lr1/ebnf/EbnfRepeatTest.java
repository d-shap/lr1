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
 * Tests for {@link EbnfRepeat}.
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfRepeatTest {

    /**
     * Test class constructor.
     */
    public EbnfRepeatTest() {
        super();
    }

    /**
     * {@link EbnfRepeat} class test.
     */
    @Test
    public void constructorTest() {
        Position position = new Position(0, 0);
        EbnfNode node = new EbnfTerminal(position, "a");
        new EbnfRepeat(position, node, EbnfRepeatOperator.ZERO_OR_MANY);

        try {
            new EbnfRepeat(null, node, EbnfRepeatOperator.ZERO_OR_MANY);
            Assertions.fail("EbnfRepeat test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Position should not be null");
        }
        try {
            new EbnfRepeat(position, null, EbnfRepeatOperator.ZERO_OR_MANY);
            Assertions.fail("EbnfRepeat test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Expression should not be null");
        }
        try {
            new EbnfRepeat(position, node, null);
            Assertions.fail("EbnfRepeat test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Repeat operator should not be null");
        }
    }

    /**
     * {@link EbnfRepeat} class test.
     */
    @Test
    public void getExpressionTest() {
        Position position = new Position(0, 0);
        EbnfNode node = new EbnfTerminal(position, "a");
        EbnfRepeat repeat = new EbnfRepeat(position, node, EbnfRepeatOperator.ZERO_OR_MANY);
        Assertions.assertThat(repeat.getExpression()).isSameAs(node);
    }

    /**
     * {@link EbnfRepeat} class test.
     */
    @Test
    public void getRepeatOperatorTest() {
        Position position = new Position(0, 0);
        EbnfNode node = new EbnfTerminal(position, "a");
        EbnfRepeat repeat = new EbnfRepeat(position, node, EbnfRepeatOperator.ZERO_OR_MANY);
        Assertions.assertThat(repeat.getRepeatOperator()).isEqualTo(EbnfRepeatOperator.ZERO_OR_MANY);
    }

    /**
     * {@link EbnfRepeat} class test.
     */
    @Test
    public void toStringTest() {
        Position position = new Position(0, 0);
        EbnfNode node = new EbnfTerminal(position, "a");
        EbnfRepeat repeat = new EbnfRepeat(position, node, EbnfRepeatOperator.ZERO_OR_MANY);
        Assertions.assertThat(repeat).hasToString("Repeat(Terminal(a)*)");
    }

    /**
     * {@link EbnfRepeat} class test.
     */
    @Test
    public void serializationTest() {
        Position position = new Position(10, 20);
        EbnfNode node = new EbnfTerminal(position, "a");
        EbnfRepeat repeat = new EbnfRepeat(position, node, EbnfRepeatOperator.ZERO_OR_MANY);
        EbnfRepeat deserialized = SerializationHelper.serializeAndDeserialize(repeat);
        Assertions.assertThat(deserialized.getLine()).isEqualTo(10);
        Assertions.assertThat(deserialized.getColumn()).isEqualTo(20);
        Assertions.assertThat(deserialized.getRepeatOperator()).isEqualTo(EbnfRepeatOperator.ZERO_OR_MANY);
        Assertions.assertThat(deserialized).hasToString("Repeat(Terminal(a)*)");
    }

}
