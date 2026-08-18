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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * The input stream source.
 *
 * @param <R> the generic type of the result.
 *
 * @author Dmitry Shapovalov
 */
public final class InputStreamSource<R> extends Source<InputStream, R> {

    private static final long serialVersionUID = 1L;

    /**
     * Create new object.
     *
     * @param charConsumer the char consumer.
     */
    public InputStreamSource(final CharConsumer<R> charConsumer) {
        super(charConsumer);
    }

    /**
     * Create new object.
     *
     * @param charConsumerEx the extended char consumer.
     */
    public InputStreamSource(final CharConsumerEx<R> charConsumerEx) {
        super(charConsumerEx);
    }

    @Override
    protected void parseSource(final InputStream source) throws IOException {
        try {
            Reader reader = new InputStreamReader(source);
            int ch;
            while (true) {
                ch = reader.read();
                if (ch < 0) {
                    break;
                } else {
                    accept(ch);
                }
            }
        } finally {
            source.close();
        }
    }

}
