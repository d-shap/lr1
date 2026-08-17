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
package ru.d_shap.lr1;

import java.io.IOException;

import org.junit.Test;

import ru.d_shap.assertions.Assertions;

/**
 * Tests for {@link ParserException}.
 *
 * @author Dmitry Shapovalov
 */
public final class ParserExceptionTest {

    /**
     * Test class constructor.
     */
    public ParserExceptionTest() {
        super();
    }

    /**
     * {@link ParserException} class test.
     */
    @Test
    public void errorMessageTest() {
        Assertions.assertThat(new ParserException(null)).messageIsNull();
        Assertions.assertThat(new ParserException("")).hasMessage("");
        Assertions.assertThat(new ParserException(" ")).hasMessage(" ");
        Assertions.assertThat(new ParserException("message")).hasMessage("message");

        Assertions.assertThat(new ParserException(null, null)).messageIsNull();
        Assertions.assertThat(new ParserException("", null)).hasMessage("");
        Assertions.assertThat(new ParserException(" ", null)).hasMessage(" ");
        Assertions.assertThat(new ParserException("message", null)).hasMessage("message");

        Assertions.assertThat(new ParserException(null, new IOException())).messageIsNull();
        Assertions.assertThat(new ParserException("", new IOException())).hasMessage("");
        Assertions.assertThat(new ParserException(" ", new IOException())).hasMessage(" ");
        Assertions.assertThat(new ParserException("message", new IOException())).hasMessage("message");

        Assertions.assertThat(new ParserException(null, new IOException(""))).messageIsNull();
        Assertions.assertThat(new ParserException("", new IOException(""))).hasMessage("");
        Assertions.assertThat(new ParserException(" ", new IOException(""))).hasMessage(" ");
        Assertions.assertThat(new ParserException("message", new IOException(""))).hasMessage("message");

        Assertions.assertThat(new ParserException(null, new IOException("cause"))).messageIsNull();
        Assertions.assertThat(new ParserException("", new IOException("cause"))).hasMessage("");
        Assertions.assertThat(new ParserException(" ", new IOException("cause"))).hasMessage(" ");
        Assertions.assertThat(new ParserException("message", new IOException("cause"))).hasMessage("message");
    }

    /**
     * {@link ParserException} class test.
     */
    @Test
    public void errorCauseTest() {
        Assertions.assertThat(new ParserException("")).causeIsNull();
        Assertions.assertThat(new ParserException(" ")).causeIsNull();
        Assertions.assertThat(new ParserException("message")).causeIsNull();

        Assertions.assertThat(new ParserException("", null)).causeIsNull();
        Assertions.assertThat(new ParserException(" ", null)).causeIsNull();
        Assertions.assertThat(new ParserException("message", null)).causeIsNull();

        Assertions.assertThat(new ParserException("", new IOException())).hasCause(IOException.class);
        Assertions.assertThat(new ParserException("", new IOException())).causeMessageIsNull();
        Assertions.assertThat(new ParserException(" ", new IOException())).hasCause(IOException.class);
        Assertions.assertThat(new ParserException(" ", new IOException())).causeMessageIsNull();
        Assertions.assertThat(new ParserException("message", new IOException())).hasCause(IOException.class);
        Assertions.assertThat(new ParserException("message", new IOException())).causeMessageIsNull();

        Assertions.assertThat(new ParserException("", new IOException(""))).hasCause(IOException.class);
        Assertions.assertThat(new ParserException("", new IOException(""))).hasCauseMessage("");
        Assertions.assertThat(new ParserException(" ", new IOException(""))).hasCause(IOException.class);
        Assertions.assertThat(new ParserException(" ", new IOException(""))).hasCauseMessage("");
        Assertions.assertThat(new ParserException("message", new IOException(""))).hasCause(IOException.class);
        Assertions.assertThat(new ParserException("message", new IOException(""))).hasCauseMessage("");

        Assertions.assertThat(new ParserException("", new IOException("cause"))).hasCause(IOException.class);
        Assertions.assertThat(new ParserException("", new IOException("cause"))).hasCauseMessage("cause");
        Assertions.assertThat(new ParserException(" ", new IOException("cause"))).hasCause(IOException.class);
        Assertions.assertThat(new ParserException(" ", new IOException("cause"))).hasCauseMessage("cause");
        Assertions.assertThat(new ParserException("message", new IOException("cause"))).hasCause(IOException.class);
        Assertions.assertThat(new ParserException("message", new IOException("cause"))).hasCauseMessage("cause");
    }

}
