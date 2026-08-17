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

import java.io.Serializable;

/**
 * The source.
 *
 * @param <S> the generic type of the source.
 * @param <R> the generic type of the result.
 *
 * @author Dmitry Shapovalov
 */
public abstract class Source<S, R> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final CharConsumer<R> _charConsumer;

    /**
     * Create new object.
     *
     * @param charConsumer the char consumer.
     */
    protected Source(final CharConsumer<R> charConsumer) {
        super();
        _charConsumer = charConsumer;
    }

    /**
     * Parse the source and return the result.
     *
     * @param source the source.
     *
     * @return the result.
     */
    public final R parse(final S source) {
        _charConsumer.reset();
        parseSource(source);
        _charConsumer.accept(0, 0, -1);
        R result = _charConsumer.getResult();
        _charConsumer.reset();
        return result;
    }

    /**
     * Parse the source.
     *
     * @param source the source.
     */
    protected abstract void parseSource(S source);

    /**
     * Accept the char.
     *
     * @param ch the char.
     */
    protected final void accept(final int ch) {
        _charConsumer.accept(0, 0, ch);
    }

}
