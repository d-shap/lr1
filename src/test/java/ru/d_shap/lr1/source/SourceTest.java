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

import java.io.Reader;
import java.util.List;

import org.junit.Test;

import ru.d_shap.assertions.Assertions;
import ru.d_shap.assertions.mock.MockReader;
import ru.d_shap.assertions.util.SerializationHelper;

/**
 * Tests for {@link Source}.
 *
 * @author Dmitry Shapovalov
 */
public final class SourceTest {

    /**
     * Test class constructor.
     */
    public SourceTest() {
        super();
    }

    /**
     * {@link Source} class test.
     */
    @Test
    public void constructorTest() {
        CharConsumer<List<String>> charConsumer = new CharConsumerImpl();
        new StringSource<>(charConsumer);
        CharConsumerEx<List<String>> charConsumerEx = new CharConsumerExImpl();
        new StringSource<>(charConsumerEx);

        try {
            new StringSource<>((CharConsumer<?>) null);
            Assertions.fail("Source test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Char consumer should not be null");
        }
        try {
            new StringSource<>((CharConsumerEx<?>) null);
            Assertions.fail("Source test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Extended char consumer should not be null");
        }
    }

    /**
     * {@link Source} class test.
     */
    @Test
    public void parseTest() {
        CharConsumer<List<String>> charConsumer = new CharConsumerImpl();
        Source<String, List<String>> source = new StringSource<>(charConsumer);

        List<String> list1 = source.parse("");
        Assertions.assertThat(list1).containsExactlyInOrder("eof at 0:0");

        List<String> list2 = source.parse("abc");
        Assertions.assertThat(list1).containsExactlyInOrder("97 at 1:1", "98 at 1:2", "99 at 1:3", "eof at 0:0");
        Assertions.assertThat(list2).containsExactlyInOrder("97 at 1:1", "98 at 1:2", "99 at 1:3", "eof at 0:0");

        List<String> list3 = source.parse("ab\r\nc");
        Assertions.assertThat(list1).containsExactlyInOrder("97 at 1:1", "98 at 1:2", "13 at 1:3", "10 at 1:4", "99 at 2:1", "eof at 0:0");
        Assertions.assertThat(list2).containsExactlyInOrder("97 at 1:1", "98 at 1:2", "13 at 1:3", "10 at 1:4", "99 at 2:1", "eof at 0:0");
        Assertions.assertThat(list3).containsExactlyInOrder("97 at 1:1", "98 at 1:2", "13 at 1:3", "10 at 1:4", "99 at 2:1", "eof at 0:0");

        List<String> list4 = source.parse("ab\rc\n");
        Assertions.assertThat(list1).containsExactlyInOrder("97 at 1:1", "98 at 1:2", "13 at 1:3", "99 at 1:4", "10 at 1:5", "eof at 0:0");
        Assertions.assertThat(list2).containsExactlyInOrder("97 at 1:1", "98 at 1:2", "13 at 1:3", "99 at 1:4", "10 at 1:5", "eof at 0:0");
        Assertions.assertThat(list3).containsExactlyInOrder("97 at 1:1", "98 at 1:2", "13 at 1:3", "99 at 1:4", "10 at 1:5", "eof at 0:0");
        Assertions.assertThat(list4).containsExactlyInOrder("97 at 1:1", "98 at 1:2", "13 at 1:3", "99 at 1:4", "10 at 1:5", "eof at 0:0");

        try {
            source.parse(null);
            Assertions.fail("Source test fail");
        } catch (NullPointerException ex) {
            Assertions.assertThat(ex).hasMessage("Source should not be null");
        }
    }

    /**
     * {@link Source} class test.
     */
    @Test
    public void parseFailTest() {
        CharConsumer<List<String>> charConsumer = new CharConsumerImpl();
        Source<Reader, List<String>> source = new ReaderSource<>(charConsumer);
        try {
            source.parse(MockReader.builder().setContent("abc").setReadException("read ex").buildReader());
            Assertions.fail("Source test fail");
        } catch (SourceException ex) {
            Assertions.assertThat(ex).hasMessage("Source processing exception");
            Assertions.assertThat(ex).hasCauseMessage("read ex");
        }
    }

    /**
     * {@link Source} class test.
     */
    @Test
    public void serializationTest() {
        CharConsumer<List<String>> charConsumer = new CharConsumerImpl();
        Source<String, List<String>> source = new StringSource<>(charConsumer);
        Source<String, List<String>> deserialized = SerializationHelper.serializeAndDeserialize(source);
        List<String> list = deserialized.parse("abc");
        Assertions.assertThat(list).containsExactlyInOrder("97 at 1:1", "98 at 1:2", "99 at 1:3", "eof at 0:0");
    }

}
