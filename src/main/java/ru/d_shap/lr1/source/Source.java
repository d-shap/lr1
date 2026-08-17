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
 * The source.
 *
 * @param <S> the generic type of the source.
 * @param <T> the generic type of the result.
 *
 * @author Dmitry Shapovalov
 */
public abstract class Source<S, T> {

    private final CharConsumer<T> _charConsumer;

    protected Source(final CharConsumer<T> charConsumer) {
        super();
        _charConsumer = charConsumer;
    }

    public final T parse(final S source) {
        _charConsumer.reset();
        processSource(source);
        _charConsumer.accept(0, 0, -1);
        T result = _charConsumer.getResult();
        _charConsumer.reset();
        return result;
    }

    protected abstract void processSource(S source);

    protected final void accept(final char ch) {
        _charConsumer.accept(0, 0, ch);
    }

}
