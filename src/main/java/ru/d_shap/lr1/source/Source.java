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

    private int _line;

    private int _column;

    /**
     * Create new object.
     *
     * @param charConsumer the char consumer.
     */
    protected Source(final CharConsumer<R> charConsumer) {
        super();
        if (charConsumer == null) {
            throw new NullPointerException("Char consumer should not be null");
        }
        _charConsumer = charConsumer;
        _line = 0;
        _column = 0;
    }

    /**
     * Parse the source and return the result.
     *
     * @param source the source.
     *
     * @return the result.
     */
    public final R parse(final S source) {
        if (source == null) {
            throw new NullPointerException("Source should not be null");
        }
        try {
            _charConsumer.reset();
            _line = 1;
            _column = 1;
            parseSource(source);
            _charConsumer.accept(0, 0, -1);
            return _charConsumer.getResult();
        } catch (IOException ex) {
            throw new SourceException(ex);
        }
    }

    /**
     * Parse the source.
     *
     * @param source the source.
     *
     * @throws IOException IO exception.
     */
    protected abstract void parseSource(S source) throws IOException;

    /**
     * Accept the char.
     *
     * @param ch the char.
     */
    protected final void accept(final int ch) {
        _charConsumer.accept(_line, _column, ch);
        if (ch == '\n') {
            _line++;
            _column = 1;
        } else {
            _column++;
        }
    }

}
