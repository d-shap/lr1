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

import java.io.IOException;

import org.junit.Test;

import ru.d_shap.assertions.Assertions;

/**
 * Tests for {@link SourceException}.
 *
 * @author Dmitry Shapovalov
 */
public final class SourceExceptionTest {

    /**
     * Test class constructor.
     */
    public SourceExceptionTest() {
        super();
    }

    /**
     * {@link SourceException} class test.
     */
    @Test
    public void errorMessageTest() {
        Assertions.assertThat(new SourceException(null)).hasMessage("Source processing exception");
        Assertions.assertThat(new SourceException(new IOException())).hasMessage("Source processing exception");
        Assertions.assertThat(new SourceException(new IOException(""))).hasMessage("Source processing exception");
        Assertions.assertThat(new SourceException(new IOException("cause"))).hasMessage("Source processing exception");
    }

    /**
     * {@link SourceException} class test.
     */
    @Test
    public void errorCauseTest() {
        Assertions.assertThat(new SourceException(null)).causeIsNull();
        Assertions.assertThat(new SourceException(new IOException())).hasCause(IOException.class);
        Assertions.assertThat(new SourceException(new IOException())).causeMessageIsNull();
        Assertions.assertThat(new SourceException(new IOException(""))).hasCause(IOException.class);
        Assertions.assertThat(new SourceException(new IOException(""))).hasCauseMessage("");
        Assertions.assertThat(new SourceException(new IOException("cause"))).hasCause(IOException.class);
        Assertions.assertThat(new SourceException(new IOException("cause"))).hasCauseMessage("cause");
    }

}
