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

import java.util.Arrays;
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
        List<EbnfNode> nodes = Arrays.asList(node1, node2);
        new EbnfChoice(new Position(1, 1), nodes);

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
        // TODO
    }

    /**
     * {@link EbnfChoice} class test.
     */
    @Test
    public void getExpressionTest() {
        // TODO
    }

    /**
     * {@link EbnfChoice} class test.
     */
    @Test
    public void toStringTest() {
        // TODO
    }

    /**
     * {@link EbnfChoice} class test.
     */
    @Test
    public void serializationTest() {
        // TODO
    }

}
