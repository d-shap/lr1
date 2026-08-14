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
package ru.d_shap.lr1.validator;

import ru.d_shap.lr1.EbnfValidationException;

/**
 * The EBNF undefined rule exception.
 *
 * @author Dmitry Shapovalov
 */
public class EbnfUndefinedRuleException extends EbnfValidationException {

    private static final long serialVersionUID = 1L;

    private final String _ruleName;

    /**
     * Create new object.
     *
     * @param line     the line number.
     * @param column   the column number.
     * @param ruleName the undefined rule name.
     */
    public EbnfUndefinedRuleException(final int line, final int column, final String ruleName) {
        super(line, column, "Undefined rule: '" + ruleName + "'");
        _ruleName = ruleName;
    }

    /**
     * Get the undefined rule name.
     *
     * @return the undefined rule name.
     */
    public String getRuleName() {
        return _ruleName;
    }

}
