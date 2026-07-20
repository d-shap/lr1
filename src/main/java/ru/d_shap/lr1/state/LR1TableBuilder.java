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

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Builds ACTION and GOTO tables for LR(1) parser.
 * <p>
 * Processes LR(1) states and creates the parsing tables based on:
 * - SHIFT actions for symbols after the dot
 * - REDUCE actions for complete items
 * - ACCEPT action for the start item
 * - GOTO transitions for non-terminals
 *
 * @author Dmitry Shapovalov
 */
public final class LR1TableBuilder {

    private final List<LR1ItemSet> _states;

    private final Map<Integer, Map<String, Integer>> _gotoTransitions;

    private final Map<String, List<Production>> _grammarMap;

    /**
     * Create a new LR(1) table builder.
     *
     * @param states          the list of all LR(1) states.
     * @param gotoTransitions the GOTO transitions between states.
     * @param grammarMap      the grammar map (non-terminal to productions).
     */
    public LR1TableBuilder(final List<LR1ItemSet> states,
                           final Map<Integer, Map<String, Integer>> gotoTransitions,
                           final Map<String, List<Production>> grammarMap) {
        super();
        _states = Objects.requireNonNull(states, "states cannot be null");
        _gotoTransitions = Objects.requireNonNull(gotoTransitions, "gotoTransitions cannot be null");
        _grammarMap = Objects.requireNonNull(grammarMap, "grammarMap cannot be null");
    }

    /**
     * Build ACTION and GOTO tables.
     *
     * @return the ACTION/GOTO table.
     */
    public ActionGotoTable buildTables() {
        ActionGotoTable table = new ActionGotoTable();

        // Process each state
        for (LR1ItemSet state : _states) {
            int stateNumber = state.getStateNumber();

            // Get GOTO transitions for this state
            Map<String, Integer> transitions = _gotoTransitions.get(stateNumber);

            // Add SHIFT actions and GOTO entries
            if (transitions != null) {
                for (Map.Entry<String, Integer> transition : transitions.entrySet()) {
                    String symbol = transition.getKey();
                    int nextState = transition.getValue();

                    if (isTerminal(symbol)) {
                        // SHIFT action for terminals
                        table.setShift(stateNumber, symbol, nextState);
                    } else {
                        // GOTO entry for non-terminals
                        table.setGoto(stateNumber, symbol, nextState);
                    }
                }
            }

            // Process complete items (REDUCE and ACCEPT actions)
            for (LR1Item item : state.getCompleteItems()) {
                Production production = item.getProduction();
                String lookahead = item.getLookahead();

                // Check if this is the start item [S' → S •, $]
                if (isStartProduction(production) && "$".equals(lookahead)) {
                    // ACCEPT action
                    table.setAccept(stateNumber, lookahead);
                } else {
                    // REDUCE action
                    int ruleNumber = production.getRuleNumber();
                    table.setReduce(stateNumber, lookahead, ruleNumber);
                }
            }
        }

        return table;
    }

    /**
     * Check if a symbol is a terminal (not in grammar map).
     *
     * @param symbol the symbol to check.
     *
     * @return true if the symbol is a terminal.
     */
    private boolean isTerminal(final String symbol) {
        return !_grammarMap.containsKey(symbol);
    }

    /**
     * Check if a production is the start production (S' → ...).
     *
     * @param production the production to check.
     *
     * @return true if this is the start production.
     */
    private boolean isStartProduction(final Production production) {
        return "S'".equals(production.getLhs());
    }

}
