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
package ru.d_shap.lr1.state;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import ru.d_shap.lr1.ebnf.model.EbnfChoice;
import ru.d_shap.lr1.ebnf.model.EbnfGrammar;
import ru.d_shap.lr1.ebnf.model.EbnfNode;
import ru.d_shap.lr1.ebnf.model.EbnfOptional;
import ru.d_shap.lr1.ebnf.model.EbnfRepeat;
import ru.d_shap.lr1.ebnf.model.EbnfRule;
import ru.d_shap.lr1.ebnf.model.EbnfRuleReference;
import ru.d_shap.lr1.ebnf.model.EbnfSequence;
import ru.d_shap.lr1.ebnf.model.EbnfTerminal;

/**
 * Computes FIRST sets for EBNF grammar.
 * <p>
 * FIRST(X) is the set of terminals that can appear as the first symbol in any string
 * that can be derived from X.
 *
 * @author Dmitry Shapovalov
 */
public final class FirstSetComputer {

    private static final String EPSILON = "ε";

    private final EbnfGrammar _grammar;

    private final Map<String, Set<String>> _firstSets;

    /**
     * Create new object.
     *
     * @param grammar the EBNF grammar.
     */
    public FirstSetComputer(final EbnfGrammar grammar) {
        super();
        _grammar = grammar;
        _firstSets = new LinkedHashMap<>();
    }

    /**
     * Compute FIRST sets for all rules in the grammar.
     */
    public void compute() {
        // Initialize FIRST sets for all rules
        for (EbnfRule rule : _grammar.getRules()) {
            Set<String> set = new HashSet<>();
            _firstSets.put(rule.getName(), set);
        }

        // Iteratively compute FIRST sets until convergence
        boolean changed = true;
        while (changed) {
            changed = false;
            for (EbnfRule rule : _grammar.getRules()) {
                Set<String> oldSet = new HashSet<>(_firstSets.get(rule.getName()));
                Set<String> newSet = computeFirst(rule.getExpression());
                _firstSets.put(rule.getName(), newSet);
                if (!oldSet.equals(newSet)) {
                    changed = true;
                }
            }
        }
    }

    /**
     * Get FIRST set for a specific rule name.
     *
     * @param ruleName the name of the rule.
     *
     * @return the FIRST set for the specified rule.
     */
    public Set<String> getFirstSet(final String ruleName) {
        Set<String> result = _firstSets.get(ruleName);
        if (result == null) {
            return new HashSet<>();
        }
        return new HashSet<>(result);
    }

    /**
     * Get all FIRST sets.
     *
     * @return a map of rule names to their FIRST sets.
     */
    public Map<String, Set<String>> getAllFirstSets() {
        Map<String, Set<String>> result = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : _firstSets.entrySet()) {
            result.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }
        return result;
    }

    /**
     * Compute FIRST set for an EBNF node.
     *
     * @param node the EBNF node.
     *
     * @return the FIRST set for the specified node.
     */
    private Set<String> computeFirst(final EbnfNode node) {
        Set<String> result = new HashSet<>();

        if (node == null) {
            return result;
        }

        if (node instanceof EbnfTerminal) {
            EbnfTerminal terminal = (EbnfTerminal) node;
            result.add(terminal.getValue());
        } else if (node instanceof EbnfRuleReference) {
            EbnfRuleReference reference = (EbnfRuleReference) node;
            String ruleName = reference.getName();
            Set<String> ruleFirstSet = _firstSets.get(ruleName);
            if (ruleFirstSet != null) {
                result.addAll(ruleFirstSet);
            }
        } else if (node instanceof EbnfSequence) {
            EbnfSequence sequence = (EbnfSequence) node;
            for (int i = 0; i < sequence.getCount(); i++) {
                EbnfNode expr = sequence.getExpression(i);
                Set<String> exprFirst = computeFirst(expr);

                // Add all non-epsilon symbols from current expression
                for (String symbol : exprFirst) {
                    if (!EPSILON.equals(symbol)) {
                        result.add(symbol);
                    }
                }

                // If current expression can derive epsilon, continue to next
                if (!exprFirst.contains(EPSILON)) {
                    break;
                }
            }

            // If all expressions can derive epsilon, add epsilon
            boolean allCanDeriveEpsilon = true;
            for (int i = 0; i < sequence.getCount(); i++) {
                EbnfNode expr = sequence.getExpression(i);
                Set<String> exprFirst = computeFirst(expr);
                if (!exprFirst.contains(EPSILON)) {
                    allCanDeriveEpsilon = false;
                    break;
                }
            }
            if (allCanDeriveEpsilon && sequence.getCount() > 0) {
                result.add(EPSILON);
            }
        } else if (node instanceof EbnfChoice) {
            EbnfChoice choice = (EbnfChoice) node;
            for (int i = 0; i < choice.getCount(); i++) {
                EbnfNode expr = choice.getExpression(i);
                Set<String> exprFirst = computeFirst(expr);
                result.addAll(exprFirst);
            }
        } else if (node instanceof EbnfOptional) {
            EbnfOptional optional = (EbnfOptional) node;
            Set<String> exprFirst = computeFirst(optional.getExpression());
            // Add only non-epsilon symbols from optional expression
            for (String symbol : exprFirst) {
                if (!EPSILON.equals(symbol)) {
                    result.add(symbol);
                }
            }
            // Optional can always derive epsilon
            result.add(EPSILON);
        } else if (node instanceof EbnfRepeat) {
            EbnfRepeat repeat = (EbnfRepeat) node;
            Set<String> exprFirst = computeFirst(repeat.getExpression());
            result.addAll(exprFirst);
            // For * (zero or more): can derive epsilon
            // For + (one or more): cannot derive epsilon
            if ("*".equals(repeat.getOperator())) {
                result.add(EPSILON);
            }
        }

        return result;
    }

}
