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

import java.io.Serializable;
import java.util.Objects;

/**
 * LR parser action: shift, reduce, accept, or error.
 *
 * @author Dmitry Shapovalov
 */
public final class LRAction implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Action type.
     */
    public enum ActionType {
        /**
         * Shift action: push token and goto next state
         */
        SHIFT,
        /**
         * Reduce action: pop symbols and reduce by production
         */
        REDUCE,
        /**
         * Accept action: parsing completed successfully
         */
        ACCEPT,
        /**
         * Error action: parsing error
         */
        ERROR
    }

    private final ActionType _type;

    private final int _stateOrRuleNumber;

    /**
     * Create a shift action.
     *
     * @param nextState the next state number to push.
     *
     * @return the shift action.
     */
    public static LRAction shift(final int nextState) {
        if (nextState < 0) {
            throw new IllegalArgumentException("nextState cannot be negative");
        }
        return new LRAction(ActionType.SHIFT, nextState);
    }

    /**
     * Create a reduce action.
     *
     * @param ruleNumber the production rule number to reduce by.
     *
     * @return the reduce action.
     */
    public static LRAction reduce(final int ruleNumber) {
        if (ruleNumber < 0) {
            throw new IllegalArgumentException("ruleNumber cannot be negative");
        }
        return new LRAction(ActionType.REDUCE, ruleNumber);
    }

    /**
     * Create an accept action.
     *
     * @return the accept action.
     */
    public static LRAction accept() {
        return new LRAction(ActionType.ACCEPT, -1);
    }

    /**
     * Create an error action.
     *
     * @return the error action.
     */
    public static LRAction error() {
        return new LRAction(ActionType.ERROR, -1);
    }

    /**
     * Create new action.
     *
     * @param type              the action type.
     * @param stateOrRuleNumber the state number (for shift) or rule number (for reduce).
     */
    private LRAction(final ActionType type, final int stateOrRuleNumber) {
        super();
        _type = Objects.requireNonNull(type, "type cannot be null");
        _stateOrRuleNumber = stateOrRuleNumber;
    }

    /**
     * Get the action type.
     *
     * @return the type.
     */
    public ActionType getType() {
        return _type;
    }

    /**
     * Get the state or rule number.
     *
     * @return the state number for shift, rule number for reduce, or -1 for accept/error.
     */
    public int getStateOrRuleNumber() {
        return _stateOrRuleNumber;
    }

    /**
     * Check if this is a shift action.
     *
     * @return true if this is a shift.
     */
    public boolean isShift() {
        return _type == ActionType.SHIFT;
    }

    /**
     * Check if this is a reduce action.
     *
     * @return true if this is a reduce.
     */
    public boolean isReduce() {
        return _type == ActionType.REDUCE;
    }

    /**
     * Check if this is an accept action.
     *
     * @return true if this is accept.
     */
    public boolean isAccept() {
        return _type == ActionType.ACCEPT;
    }

    /**
     * Check if this is an error action.
     *
     * @return true if this is error.
     */
    public boolean isError() {
        return _type == ActionType.ERROR;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LRAction action = (LRAction) o;
        return _stateOrRuleNumber == action._stateOrRuleNumber &&
                _type == action._type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_type, _stateOrRuleNumber);
    }

    @Override
    public String toString() {
        switch (_type) {
            case SHIFT:
                return "shift(" + _stateOrRuleNumber + ")";
            case REDUCE:
                return "reduce(" + _stateOrRuleNumber + ")";
            case ACCEPT:
                return "accept";
            case ERROR:
                return "error";
            default:
                return "unknown";
        }
    }

}
