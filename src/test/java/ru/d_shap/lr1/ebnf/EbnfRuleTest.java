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
import ru.d_shap.lr1.Position;

/**
 * Tests for {@link EbnfRule}.
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfRuleTest {

    /**
     * Test class constructor.
     */
    public EbnfRuleTest() {
        super();
    }

    /**
     * {@link EbnfRule} class test.
     */
    @Test
    public void constructorTest() {
        Position position = new Position(1, 1);
        EbnfNode node = new EbnfTerminal(position, "a");
        new EbnfRule(position, "a", node);

        try {
            new EbnfRule(null, "a", node);
            Assertions.fail("EbnfRule test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Position should not be null");
        }
        try {
            new EbnfRule(position, null, node);
            Assertions.fail("EbnfRule test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Name should not be null");
        }
        try {
            new EbnfRule(position, "a", null);
            Assertions.fail("EbnfRule test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Expression should not be null");
        }
    }

    /**
     * {@link EbnfRule} class test.
     */
    @Test
    public void getNameTest() {
        // TODO
    }

    /**
     * {@link EbnfRule} class test.
     */
    @Test
    public void getExpressionTest() {
        // TODO
    }

    /**
     * {@link EbnfRule} class test.
     */
    @Test
    public void toStringTest() {
        // TODO
    }

    /**
     * {@link EbnfRule} class test.
     */
    @Test
    public void serializationTest() {
        // TODO
    }

}
