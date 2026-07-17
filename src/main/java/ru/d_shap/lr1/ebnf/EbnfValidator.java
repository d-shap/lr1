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
package ru.d_shap.lr1.ebnf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.d_shap.lr1.ebnf.model.EbnfChoice;
import ru.d_shap.lr1.ebnf.model.EbnfExcept;
import ru.d_shap.lr1.ebnf.model.EbnfGrammar;
import ru.d_shap.lr1.ebnf.model.EbnfNode;
import ru.d_shap.lr1.ebnf.model.EbnfOptional;
import ru.d_shap.lr1.ebnf.model.EbnfRepeat;
import ru.d_shap.lr1.ebnf.model.EbnfRule;
import ru.d_shap.lr1.ebnf.model.EbnfRuleReference;
import ru.d_shap.lr1.ebnf.model.EbnfSequence;
import ru.d_shap.lr1.ebnf.model.EbnfSpecialSequence;
import ru.d_shap.lr1.ebnf.model.EbnfTerminal;

/**
 * The EBNF validator.
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfValidator {

    private final EbnfGrammar _grammar;

    private final Map<String, EbnfRule> _definedRules;

    private final Map<String, Boolean> _hasTerminal;

    private final Set<String> _reachableRules;

    private final List<EbnfValidationException> _errors;

    /**
     * Create new object.
     *
     * @param grammar the EBNF grammar.
     */
    public EbnfValidator(final EbnfGrammar grammar) {
        super();
        _grammar = grammar;
        _definedRules = new HashMap<>();
        _hasTerminal = new HashMap<>();
        _reachableRules = new HashSet<>();
        _errors = new ArrayList<>();
    }

    /**
     * Validate the grammar.
     *
     */
    public void validate() {
        _errors.clear();
        _definedRules.clear();
        _hasTerminal.clear();
        _reachableRules.clear();

        List<EbnfRule> rules = _grammar.getRules();
        if (rules.isEmpty()) {
            return;
        }

        // Collect all defined rules
        for (EbnfRule rule : rules) {
            if (_definedRules.containsKey(rule.getName())) {
                _errors.add(new EbnfDuplicateRuleException(rule.getLine(), rule.getColumn(), rule.getName()));
            } else {
                _definedRules.put(rule.getName(), rule);
            }
        }

        // Check for undefined rule references and analyze terminal reachability
        for (EbnfRule rule : rules) {
            validateNode(rule.getExpression());
        }

        // Build reachability graph from first rule
        String startRule = rules.get(0).getName();
        buildReachableRules(startRule);

        // Check for unreachable rules
        for (EbnfRule rule : rules) {
            if (!_reachableRules.contains(rule.getName())) {
                _errors.add(new EbnfUnreachableRuleException(rule.getLine(), rule.getColumn(), rule.getName()));
            }
        }

        // Check for dead-end rules
        computeTerminalReachability(rules);
        for (EbnfRule rule : rules) {
            Boolean hasTerminal = _hasTerminal.get(rule.getName());
            if (hasTerminal == null || !hasTerminal) {
                _errors.add(new EbnfDeadEndRuleException(rule.getLine(), rule.getColumn(), rule.getName()));
            }
        }

        // If there are errors, throw the first one
        if (!_errors.isEmpty()) {
            throw _errors.get(0);
        }
    }

    /**
     * Get all validation errors.
     *
     * @return the list of validation errors.
     */
    public List<EbnfValidationException> getErrors() {
        return new ArrayList<>(_errors);
    }

    private void validateNode(final EbnfNode node) {
        if (node == null) {
            return;
        }

        if (node instanceof EbnfRuleReference) {
            validateRuleReference((EbnfRuleReference) node);
        } else if (node instanceof EbnfChoice) {
            validateChoice((EbnfChoice) node);
        } else if (node instanceof EbnfSequence) {
            validateSequence((EbnfSequence) node);
        } else if (node instanceof EbnfOptional) {
            validateOptional((EbnfOptional) node);
        } else if (node instanceof EbnfRepeat) {
            validateRepeat((EbnfRepeat) node);
        } else if (node instanceof EbnfExcept) {
            validateExcept((EbnfExcept) node);
        }
    }

    private void validateRuleReference(final EbnfRuleReference node) {
        String ruleName = node.getName();
        if (!_definedRules.containsKey(ruleName)) {
            _errors.add(new EbnfUndefinedRuleException(node.getLine(), node.getColumn(), ruleName));
        }
    }

    private void validateChoice(final EbnfChoice node) {
        for (int i = 0; i < node.getCount(); i++) {
            validateNode(node.getExpression(i));
        }
    }

    private void validateSequence(final EbnfSequence node) {
        for (int i = 0; i < node.getCount(); i++) {
            validateNode(node.getExpression(i));
        }
    }

    private void validateOptional(final EbnfOptional node) {
        validateNode(node.getExpression());
    }

    private void validateRepeat(final EbnfRepeat node) {
        validateNode(node.getExpression());
    }

    private void validateExcept(final EbnfExcept node) {
        validateNode(node.getBase());
        validateNode(node.getException());
    }

    private void buildReachableRules(final String ruleName) {
        if (_reachableRules.contains(ruleName)) {
            return;
        }
        _reachableRules.add(ruleName);

        EbnfRule rule = _definedRules.get(ruleName);
        if (rule != null) {
            collectReferencedRules(rule.getExpression());
        }
    }

    private void collectReferencedRules(final EbnfNode node) {
        if (node == null) {
            return;
        }

        if (node instanceof EbnfRuleReference) {
            String referencedRule = ((EbnfRuleReference) node).getName();
            buildReachableRules(referencedRule);
        } else if (node instanceof EbnfChoice) {
            EbnfChoice choice = (EbnfChoice) node;
            for (int i = 0; i < choice.getCount(); i++) {
                collectReferencedRules(choice.getExpression(i));
            }
        } else if (node instanceof EbnfSequence) {
            EbnfSequence sequence = (EbnfSequence) node;
            for (int i = 0; i < sequence.getCount(); i++) {
                collectReferencedRules(sequence.getExpression(i));
            }
        } else if (node instanceof EbnfOptional) {
            collectReferencedRules(((EbnfOptional) node).getExpression());
        } else if (node instanceof EbnfRepeat) {
            collectReferencedRules(((EbnfRepeat) node).getExpression());
        } else if (node instanceof EbnfExcept) {
            collectReferencedRules(((EbnfExcept) node).getBase());
            collectReferencedRules(((EbnfExcept) node).getException());
        }
    }

    private void computeTerminalReachability(final List<EbnfRule> rules) {
        boolean changed = true;
        while (changed) {
            changed = false;
            for (EbnfRule rule : rules) {
                String ruleName = rule.getName();
                Boolean hasTerminal = _hasTerminal.get(ruleName);
                if (hasTerminal == null || !hasTerminal) {
                    if (nodeHasTerminal(rule.getExpression())) {
                        _hasTerminal.put(ruleName, true);
                        changed = true;
                    }
                }
            }
        }
    }

    private boolean nodeHasTerminal(final EbnfNode node) {
        if (node == null) {
            return false;
        }

        if (node instanceof EbnfTerminal) {
            return true;
        }
        if (node instanceof EbnfSpecialSequence) {
            return true;
        }
        if (node instanceof EbnfRuleReference) {
            String ruleName = ((EbnfRuleReference) node).getName();
            Boolean hasTerminal = _hasTerminal.get(ruleName);
            return hasTerminal != null && hasTerminal;
        }
        if (node instanceof EbnfChoice) {
            EbnfChoice choice = (EbnfChoice) node;
            for (int i = 0; i < choice.getCount(); i++) {
                if (nodeHasTerminal(choice.getExpression(i))) {
                    return true;
                }
            }
            return false;
        }
        if (node instanceof EbnfSequence) {
            EbnfSequence sequence = (EbnfSequence) node;
            for (int i = 0; i < sequence.getCount(); i++) {
                if (!nodeHasTerminal(sequence.getExpression(i))) {
                    return false;
                }
            }
            return true;
        }
        if (node instanceof EbnfOptional) {
            return true;
        }
        if (node instanceof EbnfRepeat) {
            return true;
        }
        if (node instanceof EbnfExcept) {
            return nodeHasTerminal(((EbnfExcept) node).getBase());
        }

        return false;
    }

}
