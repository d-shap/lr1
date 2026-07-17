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
package ru.d_shap.lr1.ebnf;

import java.util.List;

/**
 * The EBNF circular dependency exception.
 *
 * @author Dmitry Shapovalov
 */
public class EbnfCircularDependencyException extends EbnfValidationException {

    private static final long serialVersionUID = 1L;

    private final List<String> _cycle;

    /**
     * Create new object.
     *
     * @param line   the line number.
     * @param column the column number.
     * @param cycle  the cycle of rules.
     */
    public EbnfCircularDependencyException(final int line, final int column, final List<String> cycle) {
        super(line, column, "Circular dependency: " + formatCycle(cycle));
        _cycle = cycle;
    }

    /**
     * Get the cycle of rules.
     *
     * @return the cycle of rules.
     */
    public List<String> getCycle() {
        return _cycle;
    }

    private static String formatCycle(final List<String> cycle) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cycle.size(); i++) {
            if (i > 0) {
                sb.append(" -> ");
            }
            sb.append(cycle.get(i));
        }
        return sb.toString();
    }

}
