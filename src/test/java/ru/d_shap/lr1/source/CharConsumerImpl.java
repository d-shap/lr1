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

import java.util.ArrayList;
import java.util.List;

/**
 * {@link CharConsumer} implementation.
 *
 * @author Dmitry Shapovalov
 */
public final class CharConsumerImpl implements CharConsumer<List<String>> {

    private static final long serialVersionUID = 1L;

    private final List<String> _list;

    /**
     * Create new object.
     */
    public CharConsumerImpl() {
        super();
        _list = new ArrayList<>();
    }

    @Override
    public void reset() {
        _list.clear();
    }

    @Override
    public void accept(final int line, final int column, final int ch) {
        String str = String.format("%s at %s:%s", ch, line, column);
        _list.add(str);
    }

    @Override
    public List<String> getResult() {
        return _list;
    }

}
