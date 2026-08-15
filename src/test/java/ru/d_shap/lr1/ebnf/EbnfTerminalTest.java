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
 * Tests for {@link EbnfTerminal}.
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfTerminalTest {

    /**
     * Test class constructor.
     */
    public EbnfTerminalTest() {
        super();
    }

    /**
     * {@link EbnfTerminal} class test.
     */
    @Test
    public void constructorTest() {
        Position position = new Position(1, 1);
        new EbnfTerminal(position, "a");

        try {
            new EbnfTerminal(null, "a");
            Assertions.fail("EbnfTerminal test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Position should not be null");
        }
        try {
            new EbnfTerminal(position, null);
            Assertions.fail("EbnfTerminal test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Value should not be null");
        }
    }

    /**
     * {@link EbnfTerminal} class test.
     */
    @Test
    public void getValueTest() {
        Position position = new Position(1, 1);

        EbnfTerminal terminal0 = new EbnfTerminal(position, "");
        Assertions.assertThat(terminal0.getValue()).isEqualTo("");

        EbnfTerminal terminal1 = new EbnfTerminal(position, "a");
        Assertions.assertThat(terminal1.getValue()).isEqualTo("a");

        EbnfTerminal terminal2 = new EbnfTerminal(position, "b12");
        Assertions.assertThat(terminal2.getValue()).isEqualTo("b12");
    }

    /**
     * {@link EbnfTerminal} class test.
     */
    @Test
    public void toStringTest() {
        Position position = new Position(1, 1);

        EbnfTerminal terminal0 = new EbnfTerminal(position, "");
        Assertions.assertThat(terminal0).hasToString("Terminal()");

        EbnfTerminal terminal1 = new EbnfTerminal(position, "a");
        Assertions.assertThat(terminal1).hasToString("Terminal(a)");

        EbnfTerminal terminal2 = new EbnfTerminal(position, "b12");
        Assertions.assertThat(terminal2).hasToString("Terminal(b12)");
    }

    /**
     * {@link EbnfTerminal} class test.
     */
    @Test
    public void serializationTest() {
        Position position = new Position(10, 20);
        EbnfTerminal terminal = new EbnfTerminal(position, "a");
        EbnfTerminal deserialized = SerializationHelper.serializeAndDeserialize(terminal);
        Assertions.assertThat(deserialized.getLine()).isEqualTo(10);
        Assertions.assertThat(deserialized.getColumn()).isEqualTo(20);
        Assertions.assertThat(deserialized.getValue()).isEqualTo("a");
        Assertions.assertThat(deserialized).hasToString("Terminal(a)");
    }

}
