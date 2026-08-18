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

/**
 * The char consumer adapter.
 *
 * @param <R> the generic type of the result.
 *
 * @author Dmitry Shapovalov
 */
public final class CharConsumerAdapter<R> implements CharConsumer<R> {

    private static final long serialVersionUID = 1L;

    private final CharConsumerEx<R> _charConsumerEx;

    /**
     * Create new object.
     *
     * @param charConsumerEx the extended char consumer
     */
    public CharConsumerAdapter(final CharConsumerEx<R> charConsumerEx) {
        super();
        if (charConsumerEx == null) {
            throw new NullPointerException("Extended char consumer should not be null");
        }
        _charConsumerEx = charConsumerEx;
    }

    @Override
    public void reset() {
        _charConsumerEx.reset();
    }

    @Override
    public void accept(final int line, final int column, final int ch) {
        _charConsumerEx.accept(line, column, ch, 0);
    }

    @Override
    public R getResult() {
        return _charConsumerEx.getResult();
    }

}
