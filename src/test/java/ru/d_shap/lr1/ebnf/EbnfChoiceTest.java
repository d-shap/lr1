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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import ru.d_shap.assertions.Assertions;
import ru.d_shap.lr1.Position;

/**
 * Tests for {@link EbnfChoice}.
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfChoiceTest {

    /**
     * Test class constructor.
     */
    public EbnfChoiceTest() {
        super();
    }

    /**
     * {@link EbnfChoice} class test.
     */
    @Test
    public void constructorTest() {
        Position position = new Position(1, 1);
        EbnfNode node1 = new EbnfTerminal(position, "a");
        EbnfNode node2 = new EbnfReference(position, "b");
        EbnfNode node3 = new EbnfSpecial(position, "c");
        List<EbnfNode> nodes = Arrays.asList(node1, node2, node3);
        new EbnfChoice(position, nodes);

        try {
            new EbnfChoice(null, nodes);
            Assertions.fail("EbnfChoice test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Position should not be null");
        }
        try {
            new EbnfChoice(position, null);
            Assertions.fail("EbnfChoice test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Expressions should not be null");
        }
    }

    /**
     * {@link EbnfChoice} class test.
     */
    @Test
    public void getCountTest() {
        Position position = new Position(1, 1);
        EbnfNode node1 = new EbnfTerminal(position, "a");
        EbnfNode node2 = new EbnfReference(position, "b");
        EbnfNode node3 = new EbnfSpecial(position, "c");

        EbnfChoice choice0 = new EbnfChoice(position, new ArrayList<EbnfNode>());
        Assertions.assertThat(choice0.getCount()).isEqualTo(0);

        EbnfChoice choice1 = new EbnfChoice(position, Collections.singletonList(node1));
        Assertions.assertThat(choice1.getCount()).isEqualTo(1);

        EbnfChoice choice2 = new EbnfChoice(position, Arrays.asList(node1, node2));
        Assertions.assertThat(choice2.getCount()).isEqualTo(2);

        EbnfChoice choice3 = new EbnfChoice(position, Arrays.asList(node1, node2, node3));
        Assertions.assertThat(choice3.getCount()).isEqualTo(3);
    }

    /**
     * {@link EbnfChoice} class test.
     */
    @Test
    public void getExpressionTest() {
        Position position = new Position(1, 1);
        EbnfNode node1 = new EbnfTerminal(position, "a");
        EbnfNode node2 = new EbnfReference(position, "b");
        EbnfNode node3 = new EbnfSpecial(position, "c");

        EbnfChoice choice0 = new EbnfChoice(position, new ArrayList<EbnfNode>());
        try {
            choice0.getExpression(-1);
            Assertions.fail("EbnfChoice test fail");
        } catch (IndexOutOfBoundsException ex) {
            Assertions.assertThat(ex).hasMessage("Index -1 should be in bounds [0, 0)");
        }
        try {
            choice0.getExpression(0);
            Assertions.fail("EbnfChoice test fail");
        } catch (IndexOutOfBoundsException ex) {
            Assertions.assertThat(ex).hasMessage("Index 0 should be in bounds [0, 0)");
        }
        try {
            choice0.getExpression(1);
            Assertions.fail("EbnfChoice test fail");
        } catch (IndexOutOfBoundsException ex) {
            Assertions.assertThat(ex).hasMessage("Index 1 should be in bounds [0, 0)");
        }

        EbnfChoice choice1 = new EbnfChoice(position, Collections.singletonList(node1));
        Assertions.assertThat(choice1.getExpression(0)).isSameAs(node1);
        try {
            choice1.getExpression(-1);
            Assertions.fail("EbnfChoice test fail");
        } catch (IndexOutOfBoundsException ex) {
            Assertions.assertThat(ex).hasMessage("Index -1 should be in bounds [0, 1)");
        }
        try {
            choice1.getExpression(1);
            Assertions.fail("EbnfChoice test fail");
        } catch (IndexOutOfBoundsException ex) {
            Assertions.assertThat(ex).hasMessage("Index 1 should be in bounds [0, 1)");
        }
        try {
            choice1.getExpression(2);
            Assertions.fail("EbnfChoice test fail");
        } catch (IndexOutOfBoundsException ex) {
            Assertions.assertThat(ex).hasMessage("Index 2 should be in bounds [0, 1)");
        }

        EbnfChoice choice2 = new EbnfChoice(position, Arrays.asList(node1, node2));
        Assertions.assertThat(choice2.getExpression(0)).isSameAs(node1);
        Assertions.assertThat(choice2.getExpression(1)).isSameAs(node2);
        try {
            choice2.getExpression(-1);
            Assertions.fail("EbnfChoice test fail");
        } catch (IndexOutOfBoundsException ex) {
            Assertions.assertThat(ex).hasMessage("Index -1 should be in bounds [0, 2)");
        }
        try {
            choice2.getExpression(2);
            Assertions.fail("EbnfChoice test fail");
        } catch (IndexOutOfBoundsException ex) {
            Assertions.assertThat(ex).hasMessage("Index 2 should be in bounds [0, 2)");
        }
        try {
            choice2.getExpression(3);
            Assertions.fail("EbnfChoice test fail");
        } catch (IndexOutOfBoundsException ex) {
            Assertions.assertThat(ex).hasMessage("Index 3 should be in bounds [0, 2)");
        }

        EbnfChoice choice3 = new EbnfChoice(position, Arrays.asList(node1, node2, node3));
        Assertions.assertThat(choice3.getExpression(0)).isSameAs(node1);
        Assertions.assertThat(choice3.getExpression(1)).isSameAs(node2);
        Assertions.assertThat(choice3.getExpression(2)).isSameAs(node3);
        try {
            choice3.getExpression(-1);
            Assertions.fail("EbnfChoice test fail");
        } catch (IndexOutOfBoundsException ex) {
            Assertions.assertThat(ex).hasMessage("Index -1 should be in bounds [0, 3)");
        }
        try {
            choice3.getExpression(3);
            Assertions.fail("EbnfChoice test fail");
        } catch (IndexOutOfBoundsException ex) {
            Assertions.assertThat(ex).hasMessage("Index 3 should be in bounds [0, 3)");
        }
        try {
            choice3.getExpression(4);
            Assertions.fail("EbnfChoice test fail");
        } catch (IndexOutOfBoundsException ex) {
            Assertions.assertThat(ex).hasMessage("Index 4 should be in bounds [0, 3)");
        }
    }

    /**
     * {@link EbnfChoice} class test.
     */
    @Test
    public void toStringTest() {
        Position position = new Position(1, 1);
        EbnfNode node1 = new EbnfTerminal(position, "a");
        EbnfNode node2 = new EbnfReference(position, "b");
        EbnfNode node3 = new EbnfSpecial(position, "c");

        EbnfChoice choice0 = new EbnfChoice(position, new ArrayList<EbnfNode>());
        Assertions.assertThat(choice0).hasToString("Choice()");

        EbnfChoice choice1 = new EbnfChoice(position, Collections.singletonList(node1));
        Assertions.assertThat(choice1).hasToString("Choice(Terminal(a))");

        EbnfChoice choice2 = new EbnfChoice(position, Arrays.asList(node1, node2));
        Assertions.assertThat(choice2).hasToString("Choice(Terminal(a), Reference(b))");

        EbnfChoice choice3 = new EbnfChoice(position, Arrays.asList(node1, node2, node3));
        Assertions.assertThat(choice3).hasToString("Choice(Terminal(a), Reference(b), Special(c))");
    }

    /**
     * {@link EbnfChoice} class test.
     */
    @Test
    public void serializationTest() {
        // TODO
    }

}
