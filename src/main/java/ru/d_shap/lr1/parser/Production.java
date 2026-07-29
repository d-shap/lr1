/// ////////////////////////////////////////////////////////////////////////////////////////////////
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
/// ////////////////////////////////////////////////////////////////////////////////////////////////
package ru.d_shap.lr1.parser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Production rule: A → α, where A is non-terminal and α is a sequence of symbols.
 *
 * @author Dmitry Shapovalov
 */
public final class Production implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String _lhs;

    private final List<String> _rhs;

    private final int _ruleNumber;

    /**
     * Create new production.
     *
     * @param lhs        the left-hand side (non-terminal).
     * @param rhs        the right-hand side (sequence of symbols).
     * @param ruleNumber the rule number for identification.
     */
    public Production(final String lhs, final List<String> rhs, final int ruleNumber) {
        super();
        _lhs = Objects.requireNonNull(lhs, "lhs cannot be null");
        _rhs = Collections.unmodifiableList(new ArrayList<>(Objects.requireNonNull(rhs, "rhs cannot be null")));
        _ruleNumber = ruleNumber;
    }

    /**
     * Get the left-hand side (non-terminal).
     *
     * @return the left-hand side.
     */
    public String getLhs() {
        return _lhs;
    }

    /**
     * Get the right-hand side (sequence of symbols).
     *
     * @return the right-hand side as unmodifiable list.
     */
    public List<String> getRhs() {
        return _rhs;
    }

    /**
     * Get the rule number.
     *
     * @return the rule number.
     */
    public int getRuleNumber() {
        return _ruleNumber;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Production that = (Production) o;
        return _ruleNumber == that._ruleNumber &&
                _lhs.equals(that._lhs) &&
                _rhs.equals(that._rhs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_lhs, _rhs, _ruleNumber);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_lhs).append(" → ");
        for (int i = 0; i < _rhs.size(); i++) {
            if (i > 0) {
                sb.append(" ");
            }
            sb.append(_rhs.get(i));
        }
        return sb.toString();
    }

}
