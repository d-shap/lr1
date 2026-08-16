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
package ru.d_shap.lr1.ebnf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The EBNF grammar.
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfGrammar implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<EbnfRule> _rulesList;

    private final Map<String, EbnfRule> _rulesMap;

    /**
     * Create new object.
     *
     * @param rules the rules.
     */
    public EbnfGrammar(final List<EbnfRule> rules) {
        super();
        if (rules == null) {
            throw new NullPointerException("Rules should not be null");
        }
        for (EbnfRule rule : rules) {
            if (rule == null) {
                throw new NullPointerException("Rule should not be null");
            }
        }
        _rulesList = new ArrayList<>();
        _rulesMap = new HashMap<>();
        for (EbnfRule rule : rules) {
            _rulesList.add(rule);
            String name = rule.getName();
            _rulesMap.put(name, rule);
        }
    }

    /**
     * Get the number of the rules.
     *
     * @return the number of the rules.
     */
    public int getCount() {
        return _rulesList.size();
    }

    /**
     * Get the rule at the specified index.
     *
     * @param index the specified index.
     *
     * @return the rule at the specified index.
     */
    public EbnfRule getRule(final int index) {
        int size = _rulesList.size();
        if (index < 0 || index >= size) {
            String message = String.format("Index %s should be in bounds [0, %s)", index, size);
            throw new IndexOutOfBoundsException(message);
        }
        return _rulesList.get(index);
    }

    /**
     * Get the rule with the specified name.
     *
     * @param name the specified name.
     *
     * @return the rule with the specified name.
     */
    public EbnfRule getRule(final String name) {
        return _rulesMap.get(name);
    }

    @Override
    public String toString() {
        return _rulesList.toString();
    }

}
