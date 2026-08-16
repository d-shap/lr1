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

import org.junit.Test;

import ru.d_shap.assertions.Assertions;
import ru.d_shap.lr1.Position;

/**
 * Tests for {@link EbnfGrammar}.
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfGrammarTest {

    /**
     * Test class constructor.
     */
    public EbnfGrammarTest() {
        super();
    }

    /**
     * {@link EbnfGrammar} class test.
     */
    @Test
    public void constructorTest() {
        Position position = new Position(1, 1);
        EbnfNode node1 = new EbnfTerminal(position, "a");
        EbnfRule rule1 = new EbnfRule(position, "n1", node1);
        EbnfNode node2 = new EbnfReference(position, "b");
        EbnfRule rule2 = new EbnfRule(position, "n2", node2);
        EbnfNode node3 = new EbnfSpecial(position, "c");
        EbnfRule rule3 = new EbnfRule(position, "n3", node3);
        new EbnfGrammar(Arrays.asList(rule1, rule2, rule3));

        try {
            new EbnfGrammar(null);
            Assertions.fail("EbnfGrammar test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Rules should not be null");
        }
        try {
            new EbnfGrammar(Arrays.asList(null, rule2, rule3));
            Assertions.fail("EbnfGrammar test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Rule should not be null");
        }
        try {
            new EbnfGrammar(Arrays.asList(rule1, null, rule3));
            Assertions.fail("EbnfGrammar test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Rule should not be null");
        }
        try {
            new EbnfGrammar(Arrays.asList(rule1, rule2, null));
            Assertions.fail("EbnfGrammar test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Rule should not be null");
        }
    }

    /**
     * {@link EbnfGrammar} class test.
     */
    @Test
    public void getCountTest() {
        Position position = new Position(1, 1);
        EbnfNode node1 = new EbnfTerminal(position, "a");
        EbnfRule rule1 = new EbnfRule(position, "n1", node1);
        EbnfNode node2 = new EbnfReference(position, "b");
        EbnfRule rule2 = new EbnfRule(position, "n2", node2);
        EbnfNode node3 = new EbnfSpecial(position, "c");
        EbnfRule rule3 = new EbnfRule(position, "n3", node3);

        EbnfGrammar grammar0 = new EbnfGrammar(new ArrayList<EbnfRule>());
        Assertions.assertThat(grammar0.getCount()).isEqualTo(0);

        EbnfGrammar grammar1 = new EbnfGrammar(Collections.singletonList(rule1));
        Assertions.assertThat(grammar1.getCount()).isEqualTo(1);

        EbnfGrammar grammar2 = new EbnfGrammar(Arrays.asList(rule1, rule2));
        Assertions.assertThat(grammar2.getCount()).isEqualTo(2);

        EbnfGrammar grammar3 = new EbnfGrammar(Arrays.asList(rule1, rule2, rule3));
        Assertions.assertThat(grammar3.getCount()).isEqualTo(3);
    }

    /**
     * {@link EbnfGrammar} class test.
     */
    @Test
    public void getRuleAtIndexTest() {
        // TODO
    }

    /**
     * {@link EbnfGrammar} class test.
     */
    @Test
    public void getRuleWithNameTest() {
        // TODO
    }

    /**
     * {@link EbnfGrammar} class test.
     */
    @Test
    public void toStringTest() {
        // TODO
    }

    /**
     * {@link EbnfGrammar} class test.
     */
    @Test
    public void serializationTest() {
        // TODO
    }

}
