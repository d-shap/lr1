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

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.Test;

import ru.d_shap.assertions.Assertions;
import ru.d_shap.assertions.mock.MockInputStream;
import ru.d_shap.assertions.util.SerializationHelper;

/**
 * Tests for {@link InputStreamSource}.
 *
 * @author Dmitry Shapovalov
 */
public final class InputStreamSourceTest {

    /**
     * Test class constructor.
     */
    public InputStreamSourceTest() {
        super();
    }

    /**
     * {@link InputStreamSource} class test.
     */
    @Test
    public void parseSourceTest() {
        CharConsumer<List<String>> charConsumer = new CharConsumerImpl();
        InputStreamSource<List<String>> source = new InputStreamSource<>(charConsumer);

        List<String> list1 = source.parse(MockInputStream.builder().setContent("".getBytes(StandardCharsets.UTF_8)).buildInputStream());
        Assertions.assertThat(list1).containsExactlyInOrder("eof at 0:0");

        List<String> list2 = source.parse(MockInputStream.builder().setContent("abc".getBytes(StandardCharsets.UTF_8)).buildInputStream());
        Assertions.assertThat(list2).containsExactlyInOrder("97 at 1:1", "98 at 1:2", "99 at 1:3", "eof at 0:0");

        List<String> list3 = source.parse(MockInputStream.builder().setContent("a\nbc\n  \r\n d".getBytes(StandardCharsets.UTF_8)).buildInputStream());
        Assertions.assertThat(list3).containsExactlyInOrder("97 at 1:1", "10 at 1:2", "98 at 2:1", "99 at 2:2", "10 at 2:3", "32 at 3:1", "32 at 3:2", "13 at 3:3", "10 at 3:4", "32 at 4:1", "100 at 4:2", "eof at 0:0");

        List<String> list4 = source.parse(MockInputStream.builder().setContent("\n\n\n".getBytes(StandardCharsets.UTF_8)).buildInputStream());
        Assertions.assertThat(list4).containsExactlyInOrder("10 at 1:1", "10 at 2:1", "10 at 3:1", "eof at 0:0");

        List<String> list5 = source.parse(MockInputStream.builder().setContent("\r\r\r".getBytes(StandardCharsets.UTF_8)).buildInputStream());
        Assertions.assertThat(list5).containsExactlyInOrder("13 at 1:1", "13 at 1:2", "13 at 1:3", "eof at 0:0");
    }

    /**
     * {@link InputStreamSource} class test.
     */
    @Test
    public void parseSourceExTest() {
        CharConsumerEx<List<String>> charConsumerEx = new CharConsumerExImpl();
        InputStreamSource<List<String>> source = new InputStreamSource<>(charConsumerEx);

        List<String> list1 = source.parse(MockInputStream.builder().setContent("".getBytes(StandardCharsets.UTF_8)).buildInputStream());
        Assertions.assertThat(list1).containsExactlyInOrder("eof at 0:0");

        List<String> list2 = source.parse(MockInputStream.builder().setContent("abc".getBytes(StandardCharsets.UTF_8)).buildInputStream());
        Assertions.assertThat(list2).containsExactlyInOrder("97 (98) at 1:1", "98 (99) at 1:2", "99 (eof) at 1:3", "eof at 0:0");

        List<String> list3 = source.parse(MockInputStream.builder().setContent("a\nbc\n  \r\n d".getBytes(StandardCharsets.UTF_8)).buildInputStream());
        Assertions.assertThat(list3).containsExactlyInOrder("97 (10) at 1:1", "10 (98) at 1:2", "98 (99) at 2:1", "99 (10) at 2:2", "10 (32) at 2:3", "32 (32) at 3:1", "32 (13) at 3:2", "13 (10) at 3:3", "10 (32) at 3:4", "32 (100) at 4:1", "100 (eof) at 4:2", "eof at 0:0");

        List<String> list4 = source.parse(MockInputStream.builder().setContent("\n\n\n".getBytes(StandardCharsets.UTF_8)).buildInputStream());
        Assertions.assertThat(list4).containsExactlyInOrder("10 (10) at 1:1", "10 (10) at 2:1", "10 (eof) at 3:1", "eof at 0:0");

        List<String> list5 = source.parse(MockInputStream.builder().setContent("\r\r\r".getBytes(StandardCharsets.UTF_8)).buildInputStream());
        Assertions.assertThat(list5).containsExactlyInOrder("13 (13) at 1:1", "13 (13) at 1:2", "13 (eof) at 1:3", "eof at 0:0");
    }

    /**
     * {@link InputStreamSource} class test.
     */
    @Test
    public void parseSourceReadExceptionTest() {
        CharConsumer<List<String>> charConsumer = new CharConsumerImpl();
        InputStreamSource<List<String>> source = new InputStreamSource<>(charConsumer);
        try {
            source.parse(MockInputStream.builder().setContent("abc".getBytes(StandardCharsets.UTF_8)).setReadException("read ex").buildInputStream());
            Assertions.fail("InputStreamSource test fail");
        } catch (SourceException ex) {
            Assertions.assertThat(ex).hasMessage("Source processing exception");
            Assertions.assertThat(ex).hasCauseMessage("read ex");
        }
    }

    /**
     * {@link InputStreamSource} class test.
     */
    @Test
    public void parseSourceCloseExceptionTest() {
        CharConsumer<List<String>> charConsumer = new CharConsumerImpl();
        InputStreamSource<List<String>> source = new InputStreamSource<>(charConsumer);
        try {
            source.parse(MockInputStream.builder().setContent("abc".getBytes(StandardCharsets.UTF_8)).setCloseException("close ex").buildInputStream());
            Assertions.fail("InputStreamSource test fail");
        } catch (SourceException ex) {
            Assertions.assertThat(ex).hasMessage("Source processing exception");
            Assertions.assertThat(ex).hasCauseMessage("close ex");
        }
    }

    /**
     * {@link InputStreamSource} class test.
     */
    @Test
    public void parseSourceClosedTest() {
        CharConsumer<List<String>> charConsumer = new CharConsumerImpl();
        InputStreamSource<List<String>> source = new InputStreamSource<>(charConsumer);
        InputStream inputStream = MockInputStream.builder().setContent("abc".getBytes(StandardCharsets.UTF_8)).buildInputStream();
        Assertions.assertThat(((MockInputStream) inputStream).isClosed()).isFalse();
        List<String> list = source.parse(inputStream);
        Assertions.assertThat(((MockInputStream) inputStream).isClosed()).isTrue();
    }

    /**
     * {@link InputStreamSource} class test.
     */
    @Test
    public void serializationTest() {
        CharConsumer<List<String>> charConsumer = new CharConsumerImpl();
        InputStreamSource<List<String>> source = new InputStreamSource<>(charConsumer);
        InputStreamSource<List<String>> deserialized = SerializationHelper.serializeAndDeserialize(source);
        List<String> list = deserialized.parse(MockInputStream.builder().setContent("abc".getBytes(StandardCharsets.UTF_8)).buildInputStream());
        Assertions.assertThat(list).containsExactlyInOrder("97 at 1:1", "98 at 1:2", "99 at 1:3", "eof at 0:0");
    }

}
