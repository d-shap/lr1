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
 * Tests for {@link EbnfReference}.
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfReferenceTest {

    /**
     * Test class constructor.
     */
    public EbnfReferenceTest() {
        super();
    }

    /**
     * {@link EbnfReference} class test.
     */
    @Test
    public void constructorTest() {
        Position position = new Position(1, 1);
        new EbnfReference(position, "a");

        try {
            new EbnfReference(null, "a");
            Assertions.fail("EbnfReference test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Position should not be null");
        }
        try {
            new EbnfReference(position, null);
            Assertions.fail("EbnfReference test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Name should not be null");
        }
    }

    /**
     * {@link EbnfReference} class test.
     */
    @Test
    public void getNameTest() {
        Position position = new Position(1, 1);

        EbnfReference reference0 = new EbnfReference(position, "");
        Assertions.assertThat(reference0.getName()).isEqualTo("");

        EbnfReference reference1 = new EbnfReference(position, "a");
        Assertions.assertThat(reference1.getName()).isEqualTo("a");

        EbnfReference reference2 = new EbnfReference(position, "b12");
        Assertions.assertThat(reference2.getName()).isEqualTo("b12");
    }

    /**
     * {@link EbnfReference} class test.
     */
    @Test
    public void toStringTest() {
        Position position = new Position(1, 1);

        EbnfReference reference0 = new EbnfReference(position, "");
        Assertions.assertThat(reference0).hasToString("Reference()");

        EbnfReference reference1 = new EbnfReference(position, "a");
        Assertions.assertThat(reference1).hasToString("Reference(a)");

        EbnfReference reference2 = new EbnfReference(position, "b12");
        Assertions.assertThat(reference2).hasToString("Reference(b12)");
    }

    /**
     * {@link EbnfReference} class test.
     */
    @Test
    public void serializationTest() {
        Position position = new Position(10, 20);
        EbnfReference reference = new EbnfReference(position, "a");
        EbnfReference deserialized = SerializationHelper.serializeAndDeserialize(reference);
        Assertions.assertThat(deserialized.getLine()).isEqualTo(10);
        Assertions.assertThat(deserialized.getColumn()).isEqualTo(20);
        Assertions.assertThat(deserialized.getName()).isEqualTo("a");
        Assertions.assertThat(deserialized).hasToString("Reference(a)");
    }

}
