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
 * Tests for {@link EbnfSpecial}.
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfSpecialTest {

    /**
     * Test class constructor.
     */
    public EbnfSpecialTest() {
        super();
    }

    /**
     * {@link EbnfSpecial} class test.
     */
    @Test
    public void constructorTest() {
        Position position = new Position(0, 0);
        new EbnfSpecial(position, "a");

        try {
            new EbnfSpecial(null, "a");
            Assertions.fail("EbnfSpecial test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Position should not be null");
        }
        try {
            new EbnfSpecial(position, null);
            Assertions.fail("EbnfSpecial test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Value should not be null");
        }
    }

    /**
     * {@link EbnfSpecial} class test.
     */
    @Test
    public void getValueTest() {
        Position position = new Position(0, 0);

        EbnfSpecial special0 = new EbnfSpecial(position, "");
        Assertions.assertThat(special0.getValue()).isEqualTo("");

        EbnfSpecial special1 = new EbnfSpecial(position, "a");
        Assertions.assertThat(special1.getValue()).isEqualTo("a");

        EbnfSpecial special2 = new EbnfSpecial(position, "b12");
        Assertions.assertThat(special2.getValue()).isEqualTo("b12");
    }

    /**
     * {@link EbnfSpecial} class test.
     */
    @Test
    public void toStringTest() {
        Position position = new Position(0, 0);

        EbnfSpecial special0 = new EbnfSpecial(position, "");
        Assertions.assertThat(special0).hasToString("Special()");

        EbnfSpecial special1 = new EbnfSpecial(position, "a");
        Assertions.assertThat(special1).hasToString("Special(a)");

        EbnfSpecial special2 = new EbnfSpecial(position, "b12");
        Assertions.assertThat(special2).hasToString("Special(b12)");
    }

    /**
     * {@link EbnfSpecial} class test.
     */
    @Test
    public void serializationTest() {
        Position position = new Position(10, 20);
        EbnfSpecial special = new EbnfSpecial(position, "a");
        EbnfSpecial deserialized = SerializationHelper.serializeAndDeserialize(special);
        Assertions.assertThat(deserialized.getLine()).isEqualTo(10);
        Assertions.assertThat(deserialized.getColumn()).isEqualTo(20);
        Assertions.assertThat(deserialized.getValue()).isEqualTo("a");
        Assertions.assertThat(deserialized).hasToString("Special(a)");
    }

}
