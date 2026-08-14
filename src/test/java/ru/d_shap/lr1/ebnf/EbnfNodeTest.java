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
 * Tests for {@link EbnfNode}.
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfNodeTest {

    /**
     * Test class constructor.
     */
    public EbnfNodeTest() {
        super();
    }

    /**
     * {@link EbnfNode} class test.
     */
    @Test
    public void constructorTest() {
        new EbnfNode(new Position(1, 1));

        try {
            new EbnfNode(null);
            Assertions.fail("EbnfNode test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Position should not be null");
        }
    }

    /**
     * {@link EbnfNode} class test.
     */
    @Test
    public void getPositionTest() {
        Position position = new Position(10, 20);
        EbnfNode node = new EbnfNode(position);
        Assertions.assertThat(node.getPosition()).isSameAs(position);
    }

    /**
     * {@link EbnfNode} class test.
     */
    @Test
    public void getLineTest() {
        Assertions.assertThat(new EbnfNode(new Position(1, 1)).getLine()).isEqualTo(1);
        Assertions.assertThat(new EbnfNode(new Position(10, 20)).getLine()).isEqualTo(10);
    }

    /**
     * {@link EbnfNode} class test.
     */
    @Test
    public void getColumnTest() {
        Assertions.assertThat(new EbnfNode(new Position(1, 1)).getColumn()).isEqualTo(1);
        Assertions.assertThat(new EbnfNode(new Position(10, 20)).getColumn()).isEqualTo(20);
    }

    /**
     * {@link EbnfNode} class test.
     */
    @Test
    public void serializationTest() {
        EbnfNode node = new EbnfNode(new Position(10, 20));
        EbnfNode deserialized = SerializationHelper.serializeAndDeserialize(node);
        Assertions.assertThat(deserialized.getLine()).isEqualTo(10);
        Assertions.assertThat(deserialized.getColumn()).isEqualTo(20);
    }

}
