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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A set of LR(1) items (a canonical LR(1) state).
 *
 * @author Dmitry Shapovalov
 */
public final class LR1ItemSet implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Set<LR1Item> _items;

    private final int _stateNumber;

    private volatile int _hashCode = 0;

    /**
     * Create new LR(1) item set.
     *
     * @param items       the set of LR(1) items.
     * @param stateNumber the state number for identification.
     */
    public LR1ItemSet(final Set<LR1Item> items, final int stateNumber) {
        super();
        _items = Collections.unmodifiableSet(new HashSet<>(
                Objects.requireNonNull(items, "items cannot be null")));
        _stateNumber = stateNumber;
    }

    /**
     * Create new LR(1) item set from a list of items.
     *
     * @param items       the list of LR(1) items.
     * @param stateNumber the state number for identification.
     */
    public LR1ItemSet(final Collection<LR1Item> items, final int stateNumber) {
        super();
        _items = Collections.unmodifiableSet(new HashSet<>(
                Objects.requireNonNull(items, "items cannot be null")));
        _stateNumber = stateNumber;
    }

    /**
     * Get all items in this set.
     *
     * @return unmodifiable set of items.
     */
    public Set<LR1Item> getItems() {
        return _items;
    }

    /**
     * Get the state number.
     *
     * @return the state number.
     */
    public int getStateNumber() {
        return _stateNumber;
    }

    /**
     * Get the size of the item set.
     *
     * @return the number of items.
     */
    public int size() {
        return _items.size();
    }

    /**
     * Check if the set contains a specific item.
     *
     * @param item the item to check.
     *
     * @return true if the set contains the item.
     */
    public boolean contains(final LR1Item item) {
        return _items.contains(item);
    }

    /**
     * Get all items where the dot is not at the end.
     *
     * @return list of incomplete items.
     */
    public List<LR1Item> getIncompleteItems() {
        List<LR1Item> result = new ArrayList<>();
        for (LR1Item item : _items) {
            if (!item.isComplete()) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * Get all items where the dot is at the end.
     *
     * @return list of complete items.
     */
    public List<LR1Item> getCompleteItems() {
        List<LR1Item> result = new ArrayList<>();
        for (LR1Item item : _items) {
            if (item.isComplete()) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * Group items by the symbol after the dot.
     *
     * @return map of symbol to list of items with that symbol after the dot.
     */
    public Map<String, List<LR1Item>> groupBySymbolAfterDot() {
        Map<String, List<LR1Item>> result = new HashMap<>();
        for (LR1Item item : _items) {
            String symbol = item.getSymbolAfterDot();
            if (symbol != null) {
                if (!result.containsKey(symbol)) {
                    result.put(symbol, new ArrayList<LR1Item>());
                }
                result.get(symbol).add(item);
            }
        }
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LR1ItemSet that = (LR1ItemSet) o;
        return _items.equals(that._items);
    }

    @Override
    public int hashCode() {
        if (_hashCode == 0) {
            _hashCode = _items.hashCode();
        }
        return _hashCode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("State ").append(_stateNumber).append(":\n");
        List<LR1Item> sortedItems = new ArrayList<>(_items);
        sortedItems.sort((a, b) -> {
            int cmp = a.getProduction().getLhs().compareTo(b.getProduction().getLhs());
            if (cmp != 0) return cmp;
            cmp = Integer.compare(a.getProduction().getRuleNumber(), b.getProduction().getRuleNumber());
            if (cmp != 0) return cmp;
            cmp = Integer.compare(a.getDotPosition(), b.getDotPosition());
            if (cmp != 0) return cmp;
            return a.getLookahead().compareTo(b.getLookahead());
        });
        for (LR1Item item : sortedItems) {
            sb.append("  ").append(item).append("\n");
        }
        return sb.toString();
    }

}
