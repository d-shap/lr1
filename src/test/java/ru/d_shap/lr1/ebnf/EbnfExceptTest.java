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
 * Tests for {@link EbnfExcept}.
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfExceptTest {

    /**
     * Test class constructor.
     */
    public EbnfExceptTest() {
        super();
    }

    /**
     * {@link EbnfExcept} class test.
     */
    @Test
    public void constructorTest() {
        Position position = new Position(1, 1);
        EbnfNode node1 = new EbnfTerminal(position, "a");
        EbnfNode node2 = new EbnfSpecial(position, "b");
        new EbnfExcept(position, node1, node2);

        try {
            new EbnfExcept(null, node1, node2);
            Assertions.fail("EbnfExcept test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Position should not be null");
        }
        try {
            new EbnfExcept(position, null, node2);
            Assertions.fail("EbnfExcept test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Expression should not be null");
        }
        try {
            new EbnfExcept(position, node1, null);
            Assertions.fail("EbnfExcept test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Exception should not be null");
        }
    }

    /**
     * {@link EbnfExcept} class test.
     */
    @Test
    public void getExpressionTest() {
        Position position = new Position(1, 1);
        EbnfNode node1 = new EbnfTerminal(position, "a");
        EbnfNode node2 = new EbnfSpecial(position, "b");
        EbnfExcept except = new EbnfExcept(position, node1, node2);
        Assertions.assertThat(except.getExpression()).isSameAs(node1);
    }

    /**
     * {@link EbnfExcept} class test.
     */
    @Test
    public void getExceptionTest() {
        Position position = new Position(1, 1);
        EbnfNode node1 = new EbnfTerminal(position, "a");
        EbnfNode node2 = new EbnfSpecial(position, "b");
        EbnfExcept except = new EbnfExcept(position, node1, node2);
        Assertions.assertThat(except.getException()).isSameAs(node2);
    }

    /**
     * {@link EbnfExcept} class test.
     */
    @Test
    public void toStringTest() {
        Position position = new Position(1, 1);
        EbnfNode node1 = new EbnfTerminal(position, "a");
        EbnfNode node2 = new EbnfSpecial(position, "b");
        EbnfExcept except = new EbnfExcept(position, node1, node2);
        Assertions.assertThat(except).hasToString("Except(Terminal(a)-Special(b))");
    }

    /**
     * {@link EbnfExcept} class test.
     */
    @Test
    public void serializationTest() {
        Position position = new Position(10, 20);
        EbnfNode node1 = new EbnfTerminal(position, "a");
        EbnfNode node2 = new EbnfSpecial(position, "b");
        EbnfExcept except = new EbnfExcept(position, node1, node2);
        EbnfExcept deserialized = SerializationHelper.serializeAndDeserialize(except);
        Assertions.assertThat(deserialized.getLine()).isEqualTo(10);
        Assertions.assertThat(deserialized.getColumn()).isEqualTo(20);
        Assertions.assertThat(deserialized).hasToString("Except(Terminal(a)-Special(b))");
    }

}
