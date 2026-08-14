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
 * Tests for {@link EbnfSequence}.
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfSequenceTest {

    /**
     * Test class constructor.
     */
    public EbnfSequenceTest() {
        super();
    }

    /**
     * {@link EbnfSequence} class test.
     */
    @Test
    public void constructorTest() {
        Position position = new Position(1, 1);
        EbnfNode node1 = new EbnfTerminal(position, "a");
        EbnfNode node2 = new EbnfReference(position, "b");
        EbnfNode node3 = new EbnfSpecial(position, "c");
        List<EbnfNode> nodes = Arrays.asList(node1, node2, node3);
        new EbnfSequence(position, nodes);

        try {
            new EbnfSequence(null, nodes);
            Assertions.fail("EbnfSequence test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Position should not be null");
        }
        try {
            new EbnfSequence(position, null);
            Assertions.fail("EbnfSequence test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Expressions should not be null");
        }
    }

    /**
     * {@link EbnfSequence} class test.
     */
    @Test
    public void getCountTest() {
        Position position = new Position(1, 1);
        EbnfNode node1 = new EbnfTerminal(position, "a");
        EbnfNode node2 = new EbnfReference(position, "b");
        EbnfNode node3 = new EbnfSpecial(position, "c");

        EbnfSequence sequence0 = new EbnfSequence(position, new ArrayList<EbnfNode>());
        Assertions.assertThat(sequence0.getCount()).isEqualTo(0);

        EbnfSequence sequence1 = new EbnfSequence(position, Collections.singletonList(node1));
        Assertions.assertThat(sequence1.getCount()).isEqualTo(1);

        EbnfSequence sequence2 = new EbnfSequence(position, Arrays.asList(node1, node2));
        Assertions.assertThat(sequence2.getCount()).isEqualTo(2);

        EbnfSequence sequence3 = new EbnfSequence(position, Arrays.asList(node1, node2, node3));
        Assertions.assertThat(sequence3.getCount()).isEqualTo(3);
    }

    /**
     * {@link EbnfSequence} class test.
     */
    @Test
    public void getExpressionTest() {
        // TODO
    }

    /**
     * {@link EbnfSequence} class test.
     */
    @Test
    public void toStringTest() {
        // TODO
    }

    /**
     * {@link EbnfSequence} class test.
     */
    @Test
    public void serializationTest() {
        // TODO
    }

}
