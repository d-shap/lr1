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

/**
 * The string source.
 *
 * @param <R> the generic type of the result.
 *
 * @author Dmitry Shapovalov
 */
public final class StringSource<R> extends Source<String, R> {

    private static final long serialVersionUID = 1L;

    /**
     * Create new object.
     *
     * @param charConsumer the char consumer.
     */
    public StringSource(final CharConsumer<R> charConsumer) {
        super(charConsumer);
    }

    /**
     * Create new object.
     *
     * @param charConsumerEx the extended char consumer.
     */
    public StringSource(final CharConsumerEx<R> charConsumerEx) {
        super(charConsumerEx);
    }

    @Override
    protected void parseSource(final String source) throws IOException {
        int length = source.length();
        for (int i = 0; i < length; i++) {
            char ch = source.charAt(i);
            accept(ch);
        }
    }

}
