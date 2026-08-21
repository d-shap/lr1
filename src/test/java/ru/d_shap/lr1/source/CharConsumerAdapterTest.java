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
package ru.d_shap.lr1.source;

import java.util.List;

import org.junit.Test;

import ru.d_shap.assertions.Assertions;
import ru.d_shap.assertions.util.SerializationHelper;

/**
 * Tests for {@link CharConsumerAdapter}.
 *
 * @author Dmitry Shapovalov
 */
public final class CharConsumerAdapterTest {

    /**
     * Test class constructor.
     */
    public CharConsumerAdapterTest() {
        super();
    }

    /**
     * {@link CharConsumerAdapter} class test.
     */
    @Test
    public void constructorTest() {
        CharConsumerEx<List<String>> charConsumerEx = new CharConsumerExImpl();
        new CharConsumerAdapter<>(charConsumerEx);

        try {
            new CharConsumerAdapter<>(null);
            Assertions.fail("CharConsumerAdapter test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Extended char consumer should not be null");
        }
    }

    /**
     * {@link CharConsumerAdapter} class test.
     */
    @Test
    public void resetTest() {
        CharConsumerEx<List<String>> charConsumerEx = new CharConsumerExImpl();
        CharConsumerAdapter<List<String>> charConsumerAdapter = new CharConsumerAdapter<>(charConsumerEx);
        Assertions.assertThat(charConsumerAdapter.getResult()).containsExactlyInOrder();
        charConsumerAdapter.reset();
        Assertions.assertThat(charConsumerAdapter.getResult()).containsExactlyInOrder();
        charConsumerAdapter.accept(1, 1, '1');
        charConsumerAdapter.accept(0, 0, -1);
        Assertions.assertThat(charConsumerAdapter.getResult()).containsExactlyInOrder("49 (eof) at 1:1", "eof at 0:0");
        charConsumerAdapter.reset();
        Assertions.assertThat(charConsumerAdapter.getResult()).containsExactlyInOrder();
    }

    /**
     * {@link CharConsumerAdapter} class test.
     */
    @Test
    public void acceptTest() {
        CharConsumerEx<List<String>> charConsumerEx = new CharConsumerExImpl();
        CharConsumerAdapter<List<String>> charConsumerAdapter = new CharConsumerAdapter<>(charConsumerEx);
        charConsumerAdapter.reset();
        Assertions.assertThat(charConsumerAdapter.getResult()).containsExactlyInOrder();
        charConsumerAdapter.accept(0, 0, -1);
        Assertions.assertThat(charConsumerAdapter.getResult()).containsExactlyInOrder("eof at 0:0");
        charConsumerAdapter.reset();
        Assertions.assertThat(charConsumerAdapter.getResult()).containsExactlyInOrder();
        charConsumerAdapter.accept(1, 1, '1');
        Assertions.assertThat(charConsumerAdapter.getResult()).containsExactlyInOrder();
        charConsumerAdapter.accept(1, 2, '2');
        Assertions.assertThat(charConsumerAdapter.getResult()).containsExactlyInOrder("49 (50) at 1:1");
        charConsumerAdapter.accept(1, 3, '3');
        Assertions.assertThat(charConsumerAdapter.getResult()).containsExactlyInOrder("49 (50) at 1:1", "50 (51) at 1:2");
        charConsumerAdapter.accept(1, 4, '4');
        Assertions.assertThat(charConsumerAdapter.getResult()).containsExactlyInOrder("49 (50) at 1:1", "50 (51) at 1:2", "51 (52) at 1:3");
        charConsumerAdapter.accept(0, 0, -1);
        Assertions.assertThat(charConsumerAdapter.getResult()).containsExactlyInOrder("49 (50) at 1:1", "50 (51) at 1:2", "51 (52) at 1:3", "52 (eof) at 1:4", "eof at 0:0");
        charConsumerAdapter.reset();
        Assertions.assertThat(charConsumerAdapter.getResult()).containsExactlyInOrder();
    }

    /**
     * {@link CharConsumerAdapter} class test.
     */
    @Test
    public void getResultTest() {
        CharConsumerEx<List<String>> charConsumerEx = new CharConsumerExImpl();
        CharConsumerAdapter<List<String>> charConsumerAdapter = new CharConsumerAdapter<>(charConsumerEx);
        Assertions.assertThat(charConsumerAdapter.getResult()).containsExactlyInOrder();
        Assertions.assertThat(charConsumerAdapter.getResult()).containsExactlyInOrder();
        charConsumerAdapter.reset();
        Assertions.assertThat(charConsumerAdapter.getResult()).containsExactlyInOrder();
        Assertions.assertThat(charConsumerAdapter.getResult()).containsExactlyInOrder();
        charConsumerAdapter.accept(1, 1, '1');
        Assertions.assertThat(charConsumerAdapter.getResult()).containsExactlyInOrder();
        Assertions.assertThat(charConsumerAdapter.getResult()).containsExactlyInOrder();
        charConsumerAdapter.accept(0, 0, -1);
        Assertions.assertThat(charConsumerAdapter.getResult()).containsExactlyInOrder("49 (eof) at 1:1", "eof at 0:0");
        Assertions.assertThat(charConsumerAdapter.getResult()).containsExactlyInOrder("49 (eof) at 1:1", "eof at 0:0");
        charConsumerAdapter.reset();
        Assertions.assertThat(charConsumerAdapter.getResult()).containsExactlyInOrder();
        charConsumerAdapter.getResult().add("value");
        Assertions.assertThat(charConsumerAdapter.getResult()).containsExactlyInOrder("value");
        charConsumerAdapter.accept(0, 0, -1);
        Assertions.assertThat(charConsumerAdapter.getResult()).containsExactlyInOrder("value", "eof at 0:0");
        Assertions.assertThat(charConsumerAdapter.getResult()).containsExactlyInOrder("value", "eof at 0:0");
    }

    /**
     * {@link CharConsumerAdapter} class test.
     */
    @Test
    public void serializationTest() {
        CharConsumerEx<List<String>> charConsumerEx = new CharConsumerExImpl();
        CharConsumerAdapter<List<String>> charConsumerAdapter = new CharConsumerAdapter<>(charConsumerEx);
        CharConsumerAdapter<List<String>> deserialized = SerializationHelper.serializeAndDeserialize(charConsumerAdapter);
        deserialized.reset();
        deserialized.accept(1, 1, '1');
        deserialized.accept(1, 2, '2');
        deserialized.accept(1, 3, '3');
        deserialized.accept(0, 0, -1);
        Assertions.assertThat(deserialized.getResult()).containsExactlyInOrder("49 (50) at 1:1", "50 (51) at 1:2", "51 (eof) at 1:3", "eof at 0:0");
    }

}
