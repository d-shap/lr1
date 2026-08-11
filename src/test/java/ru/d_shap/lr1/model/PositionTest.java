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
package ru.d_shap.lr1.model;

import org.junit.Test;

import ru.d_shap.assertions.Assertions;

/**
 * Tests for {@link Position}.
 *
 * @author Dmitry Shapovalov
 */
public final class PositionTest {

    /**
     * Test class constructor.
     */
    public PositionTest() {
        super();
    }

    /**
     * {@link Position} class test.
     */
    @Test
    public void constructorTest() {
        new Position(1, 1);
        new Position(1, 2);
        new Position(2, 1);

        try {
            new Position(0, 1);
            Assertions.fail("Position test fail");
        } catch (IllegalArgumentException ex) {
            Assertions.assertThat(ex).hasMessage("Line should be positive");
        }
        try {
            new Position(-1, 1);
            Assertions.fail("Position test fail");
        } catch (IllegalArgumentException ex) {
            Assertions.assertThat(ex).hasMessage("Line should be positive");
        }
        try {
            new Position(1, 0);
            Assertions.fail("Position test fail");
        } catch (IllegalArgumentException ex) {
            Assertions.assertThat(ex).hasMessage("Column should be positive");
        }
        try {
            new Position(1, -1);
            Assertions.fail("Position test fail");
        } catch (IllegalArgumentException ex) {
            Assertions.assertThat(ex).hasMessage("Column should be positive");
        }
    }

    /**
     * {@link Position} class test.
     */
    @Test
    public void getLineTest() {
        Assertions.assertThat(new Position(1, 1).getLine()).isEqualTo(1);
        Assertions.assertThat(new Position(2, 1).getLine()).isEqualTo(2);
        Assertions.assertThat(new Position(3, 1).getLine()).isEqualTo(3);
    }

    /**
     * {@link Position} class test.
     */
    @Test
    public void getColumnTest() {
        Assertions.assertThat(new Position(1, 1).getColumn()).isEqualTo(1);
        Assertions.assertThat(new Position(1, 2).getColumn()).isEqualTo(2);
        Assertions.assertThat(new Position(1, 3).getColumn()).isEqualTo(3);
    }

    /**
     * {@link Position} class test.
     */
    @Test
    public void toStringTest() {
        Assertions.assertThat(new Position(1, 1)).hasToString(" at (line 1; column 1)");
        Assertions.assertThat(new Position(2, 1)).hasToString(" at (line 2; column 1)");
        Assertions.assertThat(new Position(1, 2)).hasToString(" at (line 1; column 2)");
        Assertions.assertThat(new Position(3, 3)).hasToString(" at (line 3; column 3)");
    }

}
