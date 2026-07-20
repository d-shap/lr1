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
import java.util.List;
import java.util.Map;

/**
 * ACTION and GOTO tables for LR(1) parser.
 * <p>
 * ACTION table: action[state][terminal] → shift state, reduce rule, or accept
 * GOTO table: goto[state][non-terminal] → next state
 *
 * @author Dmitry Shapovalov
 */
public final class ActionGotoTable {

    /**
     * Action type.
     */
    public enum ActionType {
        /**
         * Shift action: push symbol and move to next state
         */
        SHIFT,
        /**
         * Reduce action: reduce by a production rule
         */
        REDUCE,
        /**
         * Accept action: parsing complete
         */
        ACCEPT,
        /**
         * Error: no valid action
         */
        ERROR
    }

    /**
     * Represents a parser action.
     */
    public static final class Action {

        private final ActionType _type;

        private final int _value; // state number for SHIFT, rule number for REDUCE

        /**
         * Create a shift action.
         *
         * @param nextState the next state number.
         *
         * @return shift action.
         */
        public static Action shift(final int nextState) {
            return new Action(ActionType.SHIFT, nextState);
        }

        /**
         * Create a reduce action.
         *
         * @param ruleNumber the production rule number.
         *
         * @return reduce action.
         */
        public static Action reduce(final int ruleNumber) {
            return new Action(ActionType.REDUCE, ruleNumber);
        }

        /**
         * Create an accept action.
         *
         * @return accept action.
         */
        public static Action accept() {
            return new Action(ActionType.ACCEPT, -1);
        }

        /**
         * Create an error action.
         *
         * @return error action.
         */
        public static Action error() {
            return new Action(ActionType.ERROR, -1);
        }

        private Action(final ActionType type, final int value) {
            super();
            _type = type;
            _value = value;
        }

        /**
         * Get action type.
         *
         * @return the action type.
         */
        public ActionType getType() {
            return _type;
        }

        /**
         * Get action value (state for SHIFT, rule for REDUCE).
         *
         * @return the value.
         */
        public int getValue() {
            return _value;
        }

        @Override
        public String toString() {
            switch (_type) {
                case SHIFT:
                    return "s" + _value;
                case REDUCE:
                    return "r" + _value;
                case ACCEPT:
                    return "acc";
                case ERROR:
                    return "err";
                default:
                    return "?";
            }
        }
    }

    private final Map<Integer, Map<String, Action>> _actionTable;

    private final Map<Integer, Map<String, Integer>> _gotoTable;

    /**
     * Create a new ACTION/GOTO table.
     */
    public ActionGotoTable() {
        super();
        _actionTable = new HashMap<>();
        _gotoTable = new HashMap<>();
    }

    /**
     * Set a shift action in the ACTION table.
     *
     * @param state     the state number.
     * @param symbol    the terminal symbol.
     * @param nextState the next state number.
     */
    public void setShift(final int state, final String symbol, final int nextState) {
        Map<String, Action> row = _actionTable.get(state);
        if (row == null) {
            row = new HashMap<>();
            _actionTable.put(state, row);
        }
        row.put(symbol, Action.shift(nextState));
    }

    /**
     * Set a reduce action in the ACTION table.
     *
     * @param state      the state number.
     * @param symbol     the lookahead symbol.
     * @param ruleNumber the production rule number.
     */
    public void setReduce(final int state, final String symbol, final int ruleNumber) {
        Map<String, Action> row = _actionTable.get(state);
        if (row == null) {
            row = new HashMap<>();
            _actionTable.put(state, row);
        }
        row.put(symbol, Action.reduce(ruleNumber));
    }

    /**
     * Set an accept action in the ACTION table.
     *
     * @param state  the state number.
     * @param symbol the lookahead symbol (usually "$").
     */
    public void setAccept(final int state, final String symbol) {
        Map<String, Action> row = _actionTable.get(state);
        if (row == null) {
            row = new HashMap<>();
            _actionTable.put(state, row);
        }
        row.put(symbol, Action.accept());
    }

    /**
     * Set a GOTO transition.
     *
     * @param state       the state number.
     * @param nonTerminal the non-terminal symbol.
     * @param nextState   the next state number.
     */
    public void setGoto(final int state, final String nonTerminal, final int nextState) {
        Map<String, Integer> row = _gotoTable.get(state);
        if (row == null) {
            row = new HashMap<>();
            _gotoTable.put(state, row);
        }
        row.put(nonTerminal, nextState);
    }

    /**
     * Get action from ACTION table.
     *
     * @param state  the state number.
     * @param symbol the terminal symbol.
     *
     * @return the action, or error action if not found.
     */
    public Action getAction(final int state, final String symbol) {
        Map<String, Action> row = _actionTable.get(state);
        if (row == null) {
            return Action.error();
        }
        Action action = row.get(symbol);
        if (action != null) {
            return action;
        } else {
            return Action.error();
        }
    }

    /**
     * Get GOTO transition.
     *
     * @param state       the state number.
     * @param nonTerminal the non-terminal symbol.
     *
     * @return the next state number, or -1 if not found.
     */
    public int getGoto(final int state, final String nonTerminal) {
        Map<String, Integer> row = _gotoTable.get(state);
        if (row == null) {
            return -1;
        }
        Integer nextState = row.get(nonTerminal);
        if (nextState != null) {
            return nextState;
        } else {
            return -1;
        }
    }

    /**
     * Print ACTION/GOTO tables.
     *
     * @param productions the production rules.
     *
     * @return formatted table as string.
     */
    public String printTables(final List<Production> productions) {
        StringBuilder sb = new StringBuilder();

        sb.append("=== ACTION TABLE ===\n");
        for (int state = 0; state < _actionTable.size(); state++) {
            Map<String, Action> row = _actionTable.get(state);
            if (row != null) {
                sb.append("State ").append(state).append(": ");
                for (Map.Entry<String, Action> entry : row.entrySet()) {
                    sb.append(entry.getKey()).append("→").append(entry.getValue()).append(" ");
                }
                sb.append("\n");
            }
        }

        sb.append("\n=== GOTO TABLE ===\n");
        for (int state = 0; state < _gotoTable.size(); state++) {
            Map<String, Integer> row = _gotoTable.get(state);
            if (row != null) {
                sb.append("State ").append(state).append(": ");
                for (Map.Entry<String, Integer> entry : row.entrySet()) {
                    sb.append(entry.getKey()).append("→").append(entry.getValue()).append(" ");
                }
                sb.append("\n");
            }
        }

        return sb.toString();
    }

}
