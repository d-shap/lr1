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
import java.util.List;
import java.util.Map;

import ru.d_shap.lr1.ebnf.model.EbnfChoice;
import ru.d_shap.lr1.ebnf.model.EbnfExcept;
import ru.d_shap.lr1.ebnf.model.EbnfGrammar;
import ru.d_shap.lr1.ebnf.model.EbnfNode;
import ru.d_shap.lr1.ebnf.model.EbnfOptional;
import ru.d_shap.lr1.ebnf.model.EbnfRepeat;
import ru.d_shap.lr1.ebnf.model.EbnfRule;
import ru.d_shap.lr1.ebnf.model.EbnfRuleReference;
import ru.d_shap.lr1.ebnf.model.EbnfSequence;

/**
 * The EBNF validator.
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfValidator {

    private final EbnfGrammar _grammar;

    private final Map<String, Boolean> _definedRules;

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
        _errors = new ArrayList<>();
    }

    /**
     * Validate the grammar.
     *
     */
    public void validate() {
        _errors.clear();
        _definedRules.clear();

        // Collect all defined rules
        for (EbnfRule rule : _grammar.getRules()) {
            _definedRules.put(rule.getName(), true);
        }

        // Check all rule references
        for (EbnfRule rule : _grammar.getRules()) {
            validateNode(rule.getExpression());
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

}
