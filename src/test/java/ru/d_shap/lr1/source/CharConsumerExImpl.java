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
 * {@link CharConsumerEx} implementation.
 *
 * @author Dmitry Shapovalov
 */
public final class CharConsumerExImpl implements CharConsumerEx<List<String>> {

    private static final long serialVersionUID = 1L;

    private List<String> _list;

    /**
     * Create new object.
     */
    public CharConsumerExImpl() {
        super();
        _list = null;
    }

    @Override
    public void reset() {
        _list = new ArrayList<>();
    }

    @Override
    public void accept(final int line, final int column, final int ch, final int next) {
        if (ch >= 0) {
            String str = String.format("%s (%s) at %s:%s", ch, next, line, column);
            _list.add(str);
        }
    }

    @Override
    public List<String> getResult() {
        return _list;
    }

}
