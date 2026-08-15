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
import java.util.List;
import java.util.Map;

import ru.d_shap.lr1.ebnf.EbnfChoice;
import ru.d_shap.lr1.ebnf.EbnfGrammar;
import ru.d_shap.lr1.ebnf.EbnfNode;
import ru.d_shap.lr1.ebnf.EbnfOptional;
import ru.d_shap.lr1.ebnf.EbnfReference;
import ru.d_shap.lr1.ebnf.EbnfRepeat;
import ru.d_shap.lr1.ebnf.EbnfRepeatOperator;
import ru.d_shap.lr1.ebnf.EbnfRule;
import ru.d_shap.lr1.ebnf.EbnfSequence;
import ru.d_shap.lr1.ebnf.EbnfSpecial;
import ru.d_shap.lr1.ebnf.EbnfTerminal;

/**
 * Converts EBNF grammar to LR(1) Production rules.
 * <p>
 * Handles EBNF constructs:
 * - Choice (|) → multiple productions
 * - Sequence → single production with symbols in order
 * - Optional (?) → two productions (with and without)
 * - Repeat (*, +) → recursive productions
 *
 * @author Dmitry Shapovalov
 */
public final class GrammarConverter {

    private final EbnfGrammar _ebnfGrammar;

    private final Map<String, List<Production>> _grammarMap;

    private final List<Production> _allProductions;

    private int _ruleNumber;

    private int _auxCounter;

    /**
     * Create a new grammar converter.
     *
     * @param ebnfGrammar the EBNF grammar to convert.
     */
    public GrammarConverter(final EbnfGrammar ebnfGrammar) {
        super();
        _ebnfGrammar = ebnfGrammar;
        _grammarMap = new HashMap<>();
        _allProductions = new ArrayList<>();
        _ruleNumber = 0;
        _auxCounter = 0;
        convertGrammar();
    }

    /**
     * Convert EBNF grammar to production rules.
     */
    private void convertGrammar() {
        for (EbnfRule ebnfRule : _ebnfGrammar.getRules()) {
            String lhs = ebnfRule.getName();
            List<String> rhs = new ArrayList<>();
            convertNode(ebnfRule.getExpression(), rhs, lhs);
            // Add the main production rule for this grammar rule
            if (!rhs.isEmpty()) {
                addProduction(lhs, rhs);
            }
        }
    }

    /**
     * Convert an EBNF node to a list of symbols for a production RHS.
     * May create additional productions for complex EBNF constructs.
     *
     * @param node      the EBNF node to convert.
     * @param rhs       the RHS list to populate.
     * @param parentLhs the LHS of the parent production (for error context).
     */
    private void convertNode(final EbnfNode node, final List<String> rhs, final String parentLhs) {
        if (node == null) {
            return;
        }

        if (node instanceof EbnfTerminal) {
            EbnfTerminal terminal = (EbnfTerminal) node;
            rhs.add(terminal.getValue());

        } else if (node instanceof EbnfReference) {
            EbnfReference ref = (EbnfReference) node;
            rhs.add(ref.getName());

        } else if (node instanceof EbnfSequence) {
            EbnfSequence seq = (EbnfSequence) node;
            for (int i = 0; i < seq.getCount(); i++) {
                convertNode(seq.getExpression(i), rhs, parentLhs);
            }

        } else if (node instanceof EbnfChoice) {
            // Choice creates multiple productions: A → B | C | D
            // becomes A → B; A → C; A → D
            // Use auxiliary non-terminal to handle choice in any context
            EbnfChoice choice = (EbnfChoice) node;
            String auxName = createAuxName();

            // Create production for each alternative
            for (int i = 0; i < choice.getCount(); i++) {
                List<String> altRhs = new ArrayList<>();
                convertNode(choice.getExpression(i), altRhs, auxName);
                addProduction(auxName, altRhs);
            }

            rhs.add(auxName);

        } else if (node instanceof EbnfOptional) {
            // Optional creates two productions: A → ... | ε (empty)
            EbnfOptional optional = (EbnfOptional) node;
            String auxName = createAuxName();

            // Create two productions for auxiliary non-terminal
            List<String> withContent = new ArrayList<>();
            convertNode(optional.getExpression(), withContent, auxName);
            addProduction(auxName, withContent);

            // Empty alternative
            addProduction(auxName, new ArrayList<String>());

            rhs.add(auxName);

        } else if (node instanceof EbnfRepeat) {
            // Repeat: A* → innerExpr A | ε  (zero or more)
            //         A+ → innerExpr A | innerExpr (one or more)
            EbnfRepeat repeat = (EbnfRepeat) node;
            String auxName = createAuxName();

            // Recursive production: A → innerExpr A
            List<String> recursive = new ArrayList<>();
            convertNode(repeat.getExpression(), recursive, auxName);
            recursive.add(auxName);
            addProduction(auxName, recursive);

            if (repeat.getOperator() == EbnfRepeatOperator.ONE_OR_MANY) {
                // For A+: also add base case A → innerExpr
                List<String> base = new ArrayList<>();
                convertNode(repeat.getExpression(), base, auxName);
                addProduction(auxName, base);
            } else {
                // For A*: add empty alternative A → ε
                addProduction(auxName, new ArrayList<String>());
            }

            rhs.add(auxName);

        } else if (node instanceof EbnfSpecial) {
            // Special sequence: treat as a terminal (e.g., ?whitespace?)
            EbnfSpecial special = (EbnfSpecial) node;
            rhs.add("?" + special.getValue() + "?");
        }
    }

    /**
     * Add a production rule to the grammar.
     *
     * @param lhs the left-hand side.
     * @param rhs the right-hand side.
     */
    private void addProduction(final String lhs, final List<String> rhs) {
        Production production = new Production(lhs, new ArrayList<>(rhs), _ruleNumber);
        _allProductions.add(production);

        List<Production> productions = _grammarMap.get(lhs);
        if (productions == null) {
            productions = new ArrayList<>();
            _grammarMap.put(lhs, productions);
        }
        productions.add(production);

        _ruleNumber++;
    }

    /**
     * Create an auxiliary non-terminal name.
     *
     * @return the generated auxiliary name.
     */
    private String createAuxName() {
        int idx = _auxCounter;
        _auxCounter++;
        return "_aux_" + idx;
    }

    /**
     * Get grammar as map of non-terminal to productions.
     *
     * @return the grammar map.
     */
    public Map<String, List<Production>> getGrammarMap() {
        return new HashMap<>(_grammarMap);
    }

    /**
     * \n * Get all productions.
     *
     * @return list of all productions.
     */
    public List<Production> getAllProductions() {
        return new ArrayList<>(_allProductions);
    }

}
