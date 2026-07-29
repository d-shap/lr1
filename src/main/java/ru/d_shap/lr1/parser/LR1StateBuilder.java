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
package ru.d_shap.lr1.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Builds all LR(1) states and computes state transitions.
 * <p>
 * This builder creates the canonical collection of LR(1) items (states),
 * and computes the GOTO transitions between states.
 *
 * @author Dmitry Shapovalov
 */
public final class LR1StateBuilder {

    private final ClosureComputer _closureComputer;

    private final List<LR1ItemSet> _states;

    private final Map<Integer, Map<String, Integer>> _gotoTransitions;

    /**
     * Create a new LR(1) state builder.
     *
     * @param grammarMap the grammar as a map of non-terminal to list of productions.
     * @param firstSets  the FIRST sets for all non-terminals.
     */
    public LR1StateBuilder(final Map<String, List<Production>> grammarMap, final Map<String, Set<String>> firstSets) {
        super();
        _closureComputer = new ClosureComputer(grammarMap, firstSets);
        _states = new ArrayList<>();
        _gotoTransitions = new HashMap<>();
    }

    /**
     * Build all LR(1) states starting from the initial item set.
     *
     * @param initialItems the initial set of LR(1) items.
     *
     * @return list of all LR(1) states.
     */
    public List<LR1ItemSet> buildStates(final Set<LR1Item> initialItems) {
        _states.clear();
        _gotoTransitions.clear();

        // Compute closure of initial items
        Set<LR1Item> closuredInitial = _closureComputer.closure(Objects.requireNonNull(initialItems, "initialItems cannot be null"));

        // Create initial state
        LR1ItemSet initialState = new LR1ItemSet(closuredInitial, 0);
        _states.add(initialState);
        Map<String, Integer> gotoTransitions = new HashMap<>();
        _gotoTransitions.put(0, gotoTransitions);

        // Process states using breadth-first search
        for (int i = 0; i < _states.size(); i++) {
            LR1ItemSet currentState = _states.get(i);
            processState(currentState, i);
        }

        return new ArrayList<>(_states);
    }

    /**
     * Process a state and create transitions to new states.
     *
     * @param state       the current state.
     * @param stateNumber the state number.
     */
    private void processState(final LR1ItemSet state, final int stateNumber) {
        // Group items by symbol after the dot
        Map<String, List<LR1Item>> grouped = state.groupBySymbolAfterDot();

        // For each symbol, compute GOTO
        for (Map.Entry<String, List<LR1Item>> entry : grouped.entrySet()) {
            String symbol = entry.getKey();
            List<LR1Item> items = entry.getValue();

            // Advance all items with this symbol
            Set<LR1Item> advancedItems = new HashSet<>();
            for (LR1Item item : items) {
                advancedItems.add(item.advance());
            }

            // Compute closure of advanced items
            Set<LR1Item> closuredItems = _closureComputer.closure(advancedItems);

            // Check if this state already exists
            int nextStateNumber = findOrCreateState(closuredItems);

            // Record transition
            _gotoTransitions.get(stateNumber).put(symbol, nextStateNumber);
        }
    }

    /**
     * Find existing state with the same items or create a new one.
     *
     * @param items the items to search for.
     *
     * @return the state number (existing or newly created).
     */
    private int findOrCreateState(final Set<LR1Item> items) {
        // Check if state with same items already exists
        for (LR1ItemSet state : _states) {
            if (state.getItems().equals(items)) {
                return state.getStateNumber();
            }
        }

        // Create new state
        int newStateNumber = _states.size();
        LR1ItemSet newState = new LR1ItemSet(items, newStateNumber);
        _states.add(newState);
        Map<String, Integer> gotoTransitions = new HashMap<>();
        _gotoTransitions.put(newStateNumber, gotoTransitions);

        return newStateNumber;
    }

    /**
     * Get all built states.
     *
     * @return list of all LR(1) states.
     */
    public List<LR1ItemSet> getStates() {
        return new ArrayList<>(_states);
    }

    /**
     * Get GOTO transitions from a state.
     *
     * @param stateNumber the state number.
     *
     * @return map of symbol to next state number.
     */
    public Map<String, Integer> getStateTransitions(final int stateNumber) {
        Map<String, Integer> result = _gotoTransitions.get(stateNumber);
        if (result == null) {
            return new HashMap<>();
        } else {
            return new HashMap<>(result);
        }
    }

    /**
     * Get all GOTO transitions.
     *
     * @return map of state number to symbol-to-state transitions.
     */
    public Map<Integer, Map<String, Integer>> getAllTransitions() {
        Map<Integer, Map<String, Integer>> result = new HashMap<>();
        for (Map.Entry<Integer, Map<String, Integer>> entry : _gotoTransitions.entrySet()) {
            result.put(entry.getKey(), new HashMap<>(entry.getValue()));
        }
        return result;
    }

}
