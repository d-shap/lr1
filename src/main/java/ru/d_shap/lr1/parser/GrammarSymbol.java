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
 * Represents a grammar symbol (terminal or non-terminal).
 *
 * @author Dmitry Shapovalov
 */
public final class GrammarSymbol implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String _name;

    private final SymbolType _type;

    /**
     * Symbol type.
     */
    public enum SymbolType {
        /**
         * Terminal symbol (token)
         */
        TERMINAL,
        /**
         * Non-terminal symbol (rule)
         */
        NON_TERMINAL,
        /**
         * End of input symbol
         */
        EOF
    }

    /**
     * Create a new grammar symbol.
     *
     * @param name the symbol name.
     * @param type the symbol type.
     */
    public GrammarSymbol(final String name, final SymbolType type) {
        super();
        _name = Objects.requireNonNull(name, "name cannot be null");
        _type = Objects.requireNonNull(type, "type cannot be null");
    }

    /**
     * Create a terminal symbol.
     *
     * @param name the terminal name.
     *
     * @return the terminal symbol.
     */
    public static GrammarSymbol terminal(final String name) {
        return new GrammarSymbol(name, SymbolType.TERMINAL);
    }

    /**
     * Create a non-terminal symbol.
     *
     * @param name the non-terminal name.
     *
     * @return the non-terminal symbol.
     */
    public static GrammarSymbol nonTerminal(final String name) {
        return new GrammarSymbol(name, SymbolType.NON_TERMINAL);
    }

    /**
     * Create the end-of-input symbol.
     *
     * @return the EOF symbol.
     */
    public static GrammarSymbol eof() {
        return new GrammarSymbol("$", SymbolType.EOF);
    }

    /**
     * Get the symbol name.
     *
     * @return the name.
     */
    public String getName() {
        return _name;
    }

    /**
     * Get the symbol type.
     *
     * @return the type.
     */
    public SymbolType getType() {
        return _type;
    }

    /**
     * Check if this is a terminal symbol.
     *
     * @return true if this is a terminal.
     */
    public boolean isTerminal() {
        return _type == SymbolType.TERMINAL;
    }

    /**
     * Check if this is a non-terminal symbol.
     *
     * @return true if this is a non-terminal.
     */
    public boolean isNonTerminal() {
        return _type == SymbolType.NON_TERMINAL;
    }

    /**
     * Check if this is the end-of-input symbol.
     *
     * @return true if this is EOF.
     */
    public boolean isEof() {
        return _type == SymbolType.EOF;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GrammarSymbol symbol = (GrammarSymbol) o;
        return _name.equals(symbol._name) && _type == symbol._type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, _type);
    }

    @Override
    public String toString() {
        return _name;
    }

}
