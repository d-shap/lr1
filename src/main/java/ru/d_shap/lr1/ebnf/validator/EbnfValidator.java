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
package ru.d_shap.lr1.ebnf.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.d_shap.lr1.ebnf.EbnfValidationException;
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

    private final Set<String> _reachableRules;

    private final List<EbnfValidationException> _errors;

    private final Set<String> _visitingRules;

    private final Map<String, Boolean> _hasLeftRecursion;

    /**
     * Create new object.
     *
     * @param grammar the EBNF grammar.
     */
    public EbnfValidator(final EbnfGrammar grammar) {
        super();
        _grammar = grammar;
        _definedRules = new HashMap<>();
        _reachableRules = new HashSet<>();
        _errors = new ArrayList<>();
        _visitingRules = new HashSet<>();
        _hasLeftRecursion = new HashMap<>();
    }

    /**
     * Validate the grammar.
     *
     */
    public void validate() {
        _errors.clear();
        _definedRules.clear();
        _reachableRules.clear();
        _visitingRules.clear();
        _hasLeftRecursion.clear();

        List<EbnfRule> rules = _grammar.getRules();

        // Check for empty grammar
        if (rules.isEmpty()) {
            _errors.add(new EbnfEmptyGrammarException());
            throw _errors.get(0);
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
        _reachableRules.add(startRule); // Add start rule first
        collectReferencedRules(_definedRules.get(startRule).getExpression());

        // Check for unreachable rules (skip first rule as it's always reachable)
        for (int i = 1; i < rules.size(); i++) {
            EbnfRule rule = rules.get(i);
            if (!_reachableRules.contains(rule.getName())) {
                _errors.add(new EbnfUnreachableRuleException(rule.getLine(), rule.getColumn(), rule.getName()));
            }
        }

        // Check for left recursion (circular dependencies are allowed if they don't cause left recursion)
        for (EbnfRule rule : rules) {
            detectLeftRecursion(rule);
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

    /**
     * Collect direct references from a node.
     *
     * @param node the node.
     *
     * @return the list of directly referenced rule names.
     */
    private List<String> collectDirectReferences(final EbnfNode node) {
        List<String> references = new ArrayList<>();
        collectDirectReferencesHelper(node, references);
        return references;
    }

    private void collectDirectReferencesHelper(final EbnfNode node, final List<String> references) {
        if (node == null) {
            return;
        }

        if (node instanceof EbnfRuleReference) {
            String ruleName = ((EbnfRuleReference) node).getName();
            if (!references.contains(ruleName)) {
                references.add(ruleName);
            }
        } else if (node instanceof EbnfChoice) {
            EbnfChoice choice = (EbnfChoice) node;
            for (int i = 0; i < choice.getCount(); i++) {
                collectDirectReferencesHelper(choice.getExpression(i), references);
            }
        } else if (node instanceof EbnfSequence) {
            EbnfSequence sequence = (EbnfSequence) node;
            for (int i = 0; i < sequence.getCount(); i++) {
                collectDirectReferencesHelper(sequence.getExpression(i), references);
            }
        } else if (node instanceof EbnfOptional) {
            collectDirectReferencesHelper(((EbnfOptional) node).getExpression(), references);
        } else if (node instanceof EbnfRepeat) {
            collectDirectReferencesHelper(((EbnfRepeat) node).getExpression(), references);
        } else if (node instanceof EbnfExcept) {
            collectDirectReferencesHelper(((EbnfExcept) node).getBase(), references);
            collectDirectReferencesHelper(((EbnfExcept) node).getException(), references);
        }
    }

    /**
     * Detect left recursion in a rule.
     *
     * @param rule the rule to check.
     */
    private void detectLeftRecursion(final EbnfRule rule) {
        String ruleName = rule.getName();
        Boolean hasLeftRecursion = _hasLeftRecursion.get(ruleName);
        if (hasLeftRecursion != null) {
            if (hasLeftRecursion) {
                _errors.add(new EbnfLeftRecursionException(rule.getLine(), rule.getColumn(), ruleName));
            }
            return;
        }

        _visitingRules.clear();
        if (checkLeftRecursion(ruleName, rule.getExpression())) {
            _hasLeftRecursion.put(ruleName, true);
            _errors.add(new EbnfLeftRecursionException(rule.getLine(), rule.getColumn(), ruleName));
        } else {
            _hasLeftRecursion.put(ruleName, false);
        }
    }

    /**
     * Check if a node starts with a reference to the given rule (left recursion).
     *
     * @param ruleName the rule name to check for.
     * @param node     the node to analyze.
     *
     * @return true if left recursion is detected.
     */
    private boolean checkLeftRecursion(final String ruleName, final EbnfNode node) {
        if (node == null) {
            return false;
        }

        if (node instanceof EbnfTerminal || node instanceof EbnfSpecialSequence) {
            return false;
        }

        if (node instanceof EbnfRuleReference) {
            String referencedRule = ((EbnfRuleReference) node).getName();
            if (referencedRule.equals(ruleName)) {
                return true;
            }
            // Check if the referenced rule itself has left recursion
            if (!_visitingRules.contains(referencedRule)) {
                _visitingRules.add(referencedRule);
                EbnfRule refRule = _definedRules.get(referencedRule);
                if (refRule != null && checkLeftRecursion(ruleName, refRule.getExpression())) {
                    return true;
                }
                _visitingRules.remove(referencedRule);
            }
            return false;
        }

        if (node instanceof EbnfChoice) {
            EbnfChoice choice = (EbnfChoice) node;
            for (int i = 0; i < choice.getCount(); i++) {
                if (checkLeftRecursion(ruleName, choice.getExpression(i))) {
                    return true;
                }
            }
            return false;
        }

        if (node instanceof EbnfSequence) {
            EbnfSequence sequence = (EbnfSequence) node;
            for (int i = 0; i < sequence.getCount(); i++) {
                EbnfNode expr = sequence.getExpression(i);
                if (checkLeftRecursion(ruleName, expr)) {
                    return true;
                }
                // If current expression doesn't contain optional/repeat, stop checking
                if (!canBeEmpty(expr)) {
                    return false;
                }
            }
            return false;
        }

        if (node instanceof EbnfOptional || node instanceof EbnfRepeat) {
            // Optional and repeat don't cause left recursion at this position
            return false;
        }

        if (node instanceof EbnfExcept) {
            return checkLeftRecursion(ruleName, ((EbnfExcept) node).getBase());
        }

        return false;
    }

    /**
     * Check if a node can be empty (match zero symbols).
     * <p>
     * /**
     *
     * @param node the EBNF node.
     *
     * @return true if the node can be empty.
     */
    private boolean canBeEmpty(final EbnfNode node) {
        return canBeEmpty(node, new HashSet<String>());
    }

    /**
     * @param node          the EBNF node.
     * @param visitingRules the set of rules currently being visited to detect cycles.
     *
     * @return true if the node can be empty.
     */
    private boolean canBeEmpty(final EbnfNode node, final Set<String> visitingRules) {
        if (node == null) {
            return true;
        }

        if (node instanceof EbnfTerminal || node instanceof EbnfSpecialSequence) {
            return false;
        }

        if (node instanceof EbnfOptional) {
            return true;
        }

        if (node instanceof EbnfRepeat) {
            return true;
        }

        if (node instanceof EbnfChoice) {
            EbnfChoice choice = (EbnfChoice) node;
            for (int i = 0; i < choice.getCount(); i++) {
                if (canBeEmpty(choice.getExpression(i), visitingRules)) {
                    return true;
                }
            }
            return false;
        }

        if (node instanceof EbnfSequence) {
            EbnfSequence sequence = (EbnfSequence) node;
            for (int i = 0; i < sequence.getCount(); i++) {
                if (!canBeEmpty(sequence.getExpression(i), visitingRules)) {
                    return false;
                }
            }
            return true;
        }

        if (node instanceof EbnfRuleReference) {
            String ruleName = ((EbnfRuleReference) node).getName();
            if (visitingRules.contains(ruleName)) {
                // Cycle detected - treat as non-empty to break recursion
                return false;
            }
            EbnfRule rule = _definedRules.get(ruleName);
            if (rule != null) {
                visitingRules.add(ruleName);
                try {
                    return canBeEmpty(rule.getExpression(), visitingRules);
                } finally {
                    visitingRules.remove(ruleName);
                }
            }
            return false;
        }

        if (node instanceof EbnfExcept) {
            return canBeEmpty(((EbnfExcept) node).getBase(), visitingRules);
        }

        return false;
    }

}
