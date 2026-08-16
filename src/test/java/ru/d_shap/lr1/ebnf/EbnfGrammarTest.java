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
import ru.d_shap.assertions.util.SerializationHelper;
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
        Position position = new Position(1, 1);
        EbnfNode node1 = new EbnfTerminal(position, "a");
        EbnfRule rule1 = new EbnfRule(position, "n1", node1);
        EbnfNode node2 = new EbnfReference(position, "b");
        EbnfRule rule2 = new EbnfRule(position, "n2", node2);
        EbnfNode node3 = new EbnfSpecial(position, "c");
        EbnfRule rule3 = new EbnfRule(position, "n3", node3);

        EbnfGrammar grammar0 = new EbnfGrammar(new ArrayList<EbnfRule>());
        try {
            grammar0.getRule(-1);
            Assertions.fail("EbnfGrammar test fail");
        } catch (IndexOutOfBoundsException ex) {
            Assertions.assertThat(ex).hasMessage("Index -1 should be in bounds [0, 0)");
        }
        try {
            grammar0.getRule(0);
            Assertions.fail("EbnfGrammar test fail");
        } catch (IndexOutOfBoundsException ex) {
            Assertions.assertThat(ex).hasMessage("Index 0 should be in bounds [0, 0)");
        }
        try {
            grammar0.getRule(1);
            Assertions.fail("EbnfGrammar test fail");
        } catch (IndexOutOfBoundsException ex) {
            Assertions.assertThat(ex).hasMessage("Index 1 should be in bounds [0, 0)");
        }

        EbnfGrammar grammar1 = new EbnfGrammar(Collections.singletonList(rule1));
        Assertions.assertThat(grammar1.getRule(0)).isSameAs(rule1);
        try {
            grammar1.getRule(-1);
            Assertions.fail("EbnfGrammar test fail");
        } catch (IndexOutOfBoundsException ex) {
            Assertions.assertThat(ex).hasMessage("Index -1 should be in bounds [0, 1)");
        }
        try {
            grammar1.getRule(1);
            Assertions.fail("EbnfGrammar test fail");
        } catch (IndexOutOfBoundsException ex) {
            Assertions.assertThat(ex).hasMessage("Index 1 should be in bounds [0, 1)");
        }
        try {
            grammar1.getRule(2);
            Assertions.fail("EbnfGrammar test fail");
        } catch (IndexOutOfBoundsException ex) {
            Assertions.assertThat(ex).hasMessage("Index 2 should be in bounds [0, 1)");
        }

        EbnfGrammar grammar2 = new EbnfGrammar(Arrays.asList(rule1, rule2));
        Assertions.assertThat(grammar2.getRule(0)).isSameAs(rule1);
        Assertions.assertThat(grammar2.getRule(1)).isSameAs(rule2);
        try {
            grammar2.getRule(-1);
            Assertions.fail("EbnfGrammar test fail");
        } catch (IndexOutOfBoundsException ex) {
            Assertions.assertThat(ex).hasMessage("Index -1 should be in bounds [0, 2)");
        }
        try {
            grammar2.getRule(2);
            Assertions.fail("EbnfGrammar test fail");
        } catch (IndexOutOfBoundsException ex) {
            Assertions.assertThat(ex).hasMessage("Index 2 should be in bounds [0, 2)");
        }
        try {
            grammar2.getRule(3);
            Assertions.fail("EbnfGrammar test fail");
        } catch (IndexOutOfBoundsException ex) {
            Assertions.assertThat(ex).hasMessage("Index 3 should be in bounds [0, 2)");
        }

        EbnfGrammar grammar3 = new EbnfGrammar(Arrays.asList(rule1, rule2, rule3));
        Assertions.assertThat(grammar3.getRule(0)).isSameAs(rule1);
        Assertions.assertThat(grammar3.getRule(1)).isSameAs(rule2);
        Assertions.assertThat(grammar3.getRule(2)).isSameAs(rule3);
        try {
            grammar3.getRule(-1);
            Assertions.fail("EbnfGrammar test fail");
        } catch (IndexOutOfBoundsException ex) {
            Assertions.assertThat(ex).hasMessage("Index -1 should be in bounds [0, 3)");
        }
        try {
            grammar3.getRule(3);
            Assertions.fail("EbnfGrammar test fail");
        } catch (IndexOutOfBoundsException ex) {
            Assertions.assertThat(ex).hasMessage("Index 3 should be in bounds [0, 3)");
        }
        try {
            grammar3.getRule(4);
            Assertions.fail("EbnfGrammar test fail");
        } catch (IndexOutOfBoundsException ex) {
            Assertions.assertThat(ex).hasMessage("Index 4 should be in bounds [0, 3)");
        }
    }

    /**
     * {@link EbnfGrammar} class test.
     */
    @Test
    public void getRuleWithNameTest() {
        Position position = new Position(1, 1);
        EbnfNode node1 = new EbnfTerminal(position, "a");
        EbnfRule rule1 = new EbnfRule(position, "n1", node1);
        EbnfNode node2 = new EbnfReference(position, "b");
        EbnfRule rule2 = new EbnfRule(position, "n2", node2);
        EbnfNode node3 = new EbnfSpecial(position, "c");
        EbnfRule rule3 = new EbnfRule(position, "n3", node3);

        EbnfGrammar grammar0 = new EbnfGrammar(new ArrayList<EbnfRule>());
        Assertions.assertThat(grammar0.getRule("n")).isNull();

        EbnfGrammar grammar1 = new EbnfGrammar(Collections.singletonList(rule1));
        Assertions.assertThat(grammar1.getRule("n1")).isSameAs(rule1);
        Assertions.assertThat(grammar1.getRule("n")).isNull();

        EbnfGrammar grammar2 = new EbnfGrammar(Arrays.asList(rule1, rule2));
        Assertions.assertThat(grammar2.getRule("n1")).isSameAs(rule1);
        Assertions.assertThat(grammar2.getRule("n2")).isSameAs(rule2);
        Assertions.assertThat(grammar2.getRule("n")).isNull();

        EbnfGrammar grammar3 = new EbnfGrammar(Arrays.asList(rule1, rule2, rule3));
        Assertions.assertThat(grammar3.getRule("n1")).isSameAs(rule1);
        Assertions.assertThat(grammar3.getRule("n2")).isSameAs(rule2);
        Assertions.assertThat(grammar3.getRule("n3")).isSameAs(rule3);
        Assertions.assertThat(grammar3.getRule("n")).isNull();
    }

    /**
     * {@link EbnfGrammar} class test.
     */
    @Test
    public void toStringTest() {
        Position position = new Position(1, 1);
        EbnfNode node1 = new EbnfTerminal(position, "a");
        EbnfRule rule1 = new EbnfRule(position, "n1", node1);
        EbnfNode node2 = new EbnfReference(position, "b");
        EbnfRule rule2 = new EbnfRule(position, "n2", node2);
        EbnfNode node3 = new EbnfSpecial(position, "c");
        EbnfRule rule3 = new EbnfRule(position, "n3", node3);

        EbnfGrammar grammar0 = new EbnfGrammar(new ArrayList<EbnfRule>());
        Assertions.assertThat(grammar0).hasToString("[]");

        EbnfGrammar grammar1 = new EbnfGrammar(Collections.singletonList(rule1));
        Assertions.assertThat(grammar1).hasToString("[Rule(n1=Terminal(a))]");

        EbnfGrammar grammar2 = new EbnfGrammar(Arrays.asList(rule1, rule2));
        Assertions.assertThat(grammar2).hasToString("[Rule(n1=Terminal(a)), Rule(n2=Reference(b))]");

        EbnfGrammar grammar3 = new EbnfGrammar(Arrays.asList(rule1, rule2, rule3));
        Assertions.assertThat(grammar3).hasToString("[Rule(n1=Terminal(a)), Rule(n2=Reference(b)), Rule(n3=Special(c))]");
    }

    /**
     * {@link EbnfGrammar} class test.
     */
    @Test
    public void serializationTest() {
        Position position = new Position(1, 1);
        EbnfNode node1 = new EbnfTerminal(position, "a");
        EbnfRule rule1 = new EbnfRule(position, "n1", node1);
        EbnfNode node2 = new EbnfReference(position, "b");
        EbnfRule rule2 = new EbnfRule(position, "n2", node2);
        EbnfNode node3 = new EbnfSpecial(position, "c");
        EbnfRule rule3 = new EbnfRule(position, "n3", node3);
        List<EbnfRule> rules = Arrays.asList(rule1, rule2, rule3);
        EbnfGrammar grammar = new EbnfGrammar(rules);
        EbnfGrammar deserialized = SerializationHelper.serializeAndDeserialize(grammar);
        Assertions.assertThat(deserialized.getCount()).isEqualTo(3);
        Assertions.assertThat(deserialized).hasToString("[Rule(n1=Terminal(a)), Rule(n2=Reference(b)), Rule(n3=Special(c))]");
    }

}
