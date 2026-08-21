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

}
