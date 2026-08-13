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

/**
 * The EBNF repeat operator.
 *
 * @author Dmitry Shapovalov
 */
public enum EbnfRepeatOperator {

    ZERO_OR_ONE("?"),

    ZERO_OR_MANY("*"),

    ONE_OR_MANY("+");

    private final String _operator;

    EbnfRepeatOperator(final String operator) {
        _operator = operator;
    }

    @Override
    public String toString() {
        return _operator;
    }

}
