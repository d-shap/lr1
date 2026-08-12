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
package ru.d_shap.lr1.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The EBNF grammar.
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfGrammar implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<String, EbnfRule> _rules;

    /**
     * Create new object.
     *
     * @param rules the list of the EBNF rules.
     */
    public EbnfGrammar(final List<EbnfRule> rules) {
        super();
        if (rules == null) {
            _rules = null;
        } else {
            _rules = new LinkedHashMap<>();
            for (EbnfRule ebnfRule : rules) {
                if (ebnfRule != null) {
                    _rules.put(ebnfRule.getName(), ebnfRule);
                }
            }
        }
    }

    /**
     * Get the EBNF rule for the specified name.
     *
     * @param name the specified name.
     *
     * @return the EBNF rule for the specified name.
     */
    public EbnfRule getEbnfRule(final String name) {
        if (_rules == null) {
            return null;
        } else {
            return _rules.get(name);
        }
    }

    /**
     * Get all rules.
     *
     * @return the list of all rules.
     */
    public List<EbnfRule> getRules() {
        if (_rules == null) {
            return new java.util.ArrayList<>();
        } else {
            return new java.util.ArrayList<>(_rules.values());
        }
    }

    @Override
    public String toString() {
        return _rules.toString();
    }

}
