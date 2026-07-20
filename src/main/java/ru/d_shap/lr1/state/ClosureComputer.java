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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Computes the closure of an LR(1) item set.
 * <p>
 * The closure of a set of LR(1) items is computed by finding all items that can be
 * derived from the initial set. For each item [A → α • B β, a], if B is a non-terminal,
 * we add all items [B → • γ, b] where b is in FIRST(β a).
 *
 * @author Dmitry Shapovalov
 */
public final class ClosureComputer {

    private final Map<String, List<Production>> _grammarMap;

    private final Map<String, Set<String>> _firstSets;

    /**
     * Create a new closure computer.
     *
     * @param grammarMap the grammar as a map of non-terminal to list of productions.
     * @param firstSets  the FIRST sets for all non-terminals.
     */
    public ClosureComputer(final Map<String, List<Production>> grammarMap, final Map<String, Set<String>> firstSets) {
        super();
        _grammarMap = Objects.requireNonNull(grammarMap, "grammarMap cannot be null");
        _firstSets = Objects.requireNonNull(firstSets, "firstSets cannot be null");
    }

    /**
     * Compute the closure of a set of LR(1) items.
     *
     * @param items the initial set of LR(1) items.
     *
     * @return the closure of the items.
     */
    public Set<LR1Item> closure(final Set<LR1Item> items) {
        Set<LR1Item> result = new HashSet<>(Objects.requireNonNull(items, "items cannot be null"));

        boolean changed = true;
        while (changed) {
            changed = false;

            Set<LR1Item> add = new HashSet<>();

            for (LR1Item item : result) {
                // Skip items where the dot is at the end (complete items)
                if (item.isComplete()) {
                    continue;
                }

                // Get the symbol after the dot
                String symbol = item.getSymbolAfterDot();
                if (symbol == null) {
                    continue;
                }

                // Check if the symbol is a non-terminal
                if (!_grammarMap.containsKey(symbol)) {
                    continue;
                }

                // Get beta (symbols after B in A → α • B β)
                List<String> beta = item.getProduction().getRhs().subList(
                        item.getDotPosition() + 1,
                        item.getProduction().getRhs().size());

                // Compute FIRST(β + lookahead)
                Set<String> lookaheads = computeFirst(beta, item.getLookahead());

                // Add items [B → • γ, b] for each production B → γ and lookahead b
                List<Production> productions = _grammarMap.get(symbol);
                if (productions != null) {
                    for (Production production : productions) {
                        for (String lookahead : lookaheads) {
                            LR1Item newItem = new LR1Item(production, 0, lookahead);
                            if (!result.contains(newItem)) {
                                add.add(newItem);
                            }
                        }
                    }
                }
            }

            // Add new items and check if anything was added
            if (result.addAll(add)) {
                changed = true;
            }
        }

        return result;
    }

    /**
     * Compute FIRST set for a sequence of symbols followed by a lookahead.
     *
     * @param symbols   the sequence of symbols.
     * @param lookahead the lookahead symbol.
     *
     * @return the FIRST set of symbols + lookahead.
     */
    private Set<String> computeFirst(final List<String> symbols, final String lookahead) {
        Set<String> result = new HashSet<>();

        for (String symbol : symbols) {
            if (_grammarMap.containsKey(symbol)) {
                // It's a non-terminal
                Set<String> symbolFirst = _firstSets.get(symbol);
                if (symbolFirst == null) {
                    symbolFirst = new HashSet<>();
                    _firstSets.put(symbol, symbolFirst);
                }
                for (String terminal : symbolFirst) {
                    if (!"ε".equals(terminal)) {
                        result.add(terminal);
                    }
                }

                // If symbol can't derive epsilon, stop
                if (!symbolFirst.contains("ε")) {
                    return result;
                }
            } else {
                // It's a terminal, add it and stop
                result.add(symbol);
                return result;
            }
        }

        // If all symbols can derive epsilon (or list is empty), add lookahead
        result.add(lookahead);
        return result;
    }

}
