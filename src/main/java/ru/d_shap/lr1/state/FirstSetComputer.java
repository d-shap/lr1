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
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Computes FIRST sets for production rules.
 * <p>
 * FIRST(X) is the set of terminals that can appear as the first symbol in any string
 * that can be derived from X.
 *
 * @author Dmitry Shapovalov
 */
public final class FirstSetComputer {

    private final Map<String, List<Production>> _grammarMap;

    private final Map<String, Set<String>> _firstSets;

    /**
     * Create new object.
     *
     * @param grammarMap map of non-terminal names to their production rules.
     */
    public FirstSetComputer(final Map<String, List<Production>> grammarMap) {
        super();
        _grammarMap = grammarMap;
        _firstSets = new LinkedHashMap<>();
    }

    /**
     * Compute FIRST sets for all rules in the grammar.
     */
    public void compute() {
        // Initialize FIRST sets for all non-terminals
        for (String nonTerminal : _grammarMap.keySet()) {
            _firstSets.put(nonTerminal, new HashSet<String>());
        }

        // Iteratively compute FIRST sets until convergence
        boolean changed = true;
        while (changed) {
            changed = false;
            for (String nonTerminal : _grammarMap.keySet()) {
                Set<String> oldSet = new HashSet<>(_firstSets.get(nonTerminal));
                Set<String> newSet = new HashSet<>();

                // For each production rule for this non-terminal
                List<Production> productions = _grammarMap.get(nonTerminal);
                for (Production production : productions) {
                    Set<String> productionFirst = computeFirstForProduction(production);
                    newSet.addAll(productionFirst);
                }

                _firstSets.put(nonTerminal, newSet);
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
     * Compute FIRST set for a single production rule.
     *
     * @param production the production rule.
     *
     * @return the FIRST set for the specified production.
     */
    private Set<String> computeFirstForProduction(final Production production) {
        Set<String> result = new HashSet<>();
        List<String> rhs = production.getRhs();

        // If production is empty (epsilon production)
        if (rhs.isEmpty()) {
            return result;
        }

        // Iterate through symbols in RHS
        boolean allCanDeriveEpsilon = true;
        for (int i = 0; i < rhs.size(); i++) {
            String symbol = rhs.get(i);
            Set<String> symbolFirst = getFirstSetForSymbol(symbol);

            // Add all non-epsilon symbols from current symbol
            result.addAll(symbolFirst);

            // If current symbol cannot derive epsilon, stop here
            if (!canDeriveEpsilon(symbol)) {
                allCanDeriveEpsilon = false;
                break;
            }
        }

        // If all symbols can derive epsilon, this production derives epsilon
        if (allCanDeriveEpsilon) {
            // Mark epsilon capability (not added to FIRST set itself)
        }

        return result;
    }

    /**
     * Get FIRST set for a single symbol (terminal or non-terminal).
     *
     * @param symbol the symbol.
     *
     * @return the FIRST set for the symbol.
     */
    private Set<String> getFirstSetForSymbol(final String symbol) {
        Set<String> result = new HashSet<>();

        // Check if it's a non-terminal
        if (_grammarMap.containsKey(symbol)) {
            Set<String> nonTerminalFirst = _firstSets.get(symbol);
            if (nonTerminalFirst != null) {
                result.addAll(nonTerminalFirst);
            }
        } else {
            // It's a terminal, add it to FIRST set
            result.add(symbol);
        }

        return result;
    }

    /**
     * Check if a symbol can derive epsilon.
     *
     * @param symbol the symbol.
     *
     * @return true if the symbol can derive epsilon, false otherwise.
     */
    private boolean canDeriveEpsilon(final String symbol) {
        // Check if it's a non-terminal
        if (_grammarMap.containsKey(symbol)) {
            // A non-terminal can derive epsilon if any of its productions derives epsilon
            List<Production> productions = _grammarMap.get(symbol);
            for (Production production : productions) {
                if (production.getRhs().isEmpty()) {
                    return true;
                }
            }
            return false;
        } else {
            // Terminals cannot derive epsilon
            return false;
        }
    }

}
