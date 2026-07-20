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
package ru.d_shap.lr1.state;

import java.io.Serializable;
import java.util.Objects;

/**
 * LR(1) Item: [A → α • β, a]
 * Represents a production with a dot marking the position and a lookahead symbol.
 *
 * @author Dmitry Shapovalov
 */
public final class LR1Item implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Production _production;
    private final int _dotPosition;
    private final String _lookahead;

    /**
     * Create new LR(1) item.
     *
     * @param production the production rule.
     * @param dotPosition the position of the dot (0 to production.getRhs().size()).
     * @param lookahead the lookahead symbol.
     */
    public LR1Item(final Production production, final int dotPosition, final String lookahead) {
        super();
        _production = Objects.requireNonNull(production, "production cannot be null");
        if (dotPosition < 0 || dotPosition > production.getRhs().size()) {
            throw new IllegalArgumentException(
                String.format("dotPosition must be between 0 and %d, but got %d",
                    production.getRhs().size(), dotPosition));
        }
        _dotPosition = dotPosition;
        _lookahead = Objects.requireNonNull(lookahead, "lookahead cannot be null");
    }

    /**
     * Get the production rule.
     *
     * @return the production.
     */
    public Production getProduction() {
        return _production;
    }

    /**
     * Get the position of the dot.
     *
     * @return the dot position (0 to production.getRhs().size()).
     */
    public int getDotPosition() {
        return _dotPosition;
    }

    /**
     * Get the lookahead symbol.
     *
     * @return the lookahead.
     */
    public String getLookahead() {
        return _lookahead;
    }

    /**
     * Check if the item is complete (dot is at the end).
     *
     * @return true if dotPosition == production.getRhs().size().
     */
    public boolean isComplete() {
        return _dotPosition == _production.getRhs().size();
    }

    /**
     * Get the symbol after the dot (if exists).
     *
     * @return the symbol after the dot, or null if dot is at the end.
     */
    public String getSymbolAfterDot() {
        if (isComplete()) {
            return null;
        }
        return _production.getRhs().get(_dotPosition);
    }

    /**
     * Create a new item with the dot advanced by one position.
     *
     * @return a new LR1Item with the dot moved forward.
     * @throws IllegalStateException if the item is already complete.
     */
    public LR1Item advance() {
        if (isComplete()) {
            throw new IllegalStateException("Cannot advance a complete item");
        }
        return new LR1Item(_production, _dotPosition + 1, _lookahead);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LR1Item item = (LR1Item) o;
        return _dotPosition == item._dotPosition &&
               _production.equals(item._production) &&
               _lookahead.equals(item._lookahead);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_production, _dotPosition, _lookahead);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(_production.getLhs()).append(" →");

        for (int i = 0; i < _production.getRhs().size(); i++) {
            if (i == _dotPosition) {
                sb.append(" •");
            }
            sb.append(" ").append(_production.getRhs().get(i));
        }

        if (_dotPosition == _production.getRhs().size()) {
            sb.append(" •");
        }

        sb.append(", ").append(_lookahead).append("]");
        return sb.toString();
    }

}
