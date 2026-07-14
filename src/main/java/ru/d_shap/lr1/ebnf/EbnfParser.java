///////////////////////////////////////////////////////////////////////////////////////////////////
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
///////////////////////////////////////////////////////////////////////////////////////////////////
package ru.d_shap.lr1.ebnf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.d_shap.lr1.ebnf.model.EbnfChoice;
import ru.d_shap.lr1.ebnf.model.EbnfGrammar;
import ru.d_shap.lr1.ebnf.model.EbnfNode;
import ru.d_shap.lr1.ebnf.model.EbnfOptional;
import ru.d_shap.lr1.ebnf.model.EbnfRepeat;
import ru.d_shap.lr1.ebnf.model.EbnfRule;
import ru.d_shap.lr1.ebnf.model.EbnfRuleReference;
import ru.d_shap.lr1.ebnf.model.EbnfSequence;
import ru.d_shap.lr1.ebnf.model.EbnfTerminal;

/**
 * The EBNF parser.
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfParser implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<EbnfToken> _tokens;

    private int _position;

    private EbnfParser(final List<EbnfToken> tokens) {
        super();
        _tokens = tokens;
        _position = 0;
    }

    /**
     * Parse the EBNF tokens.
     *
     * @param tokens the EBNF tokens.
     *
     * @return the EBNF grammar.
     */
    public static EbnfGrammar parse(final List<EbnfToken> tokens) {
        EbnfParser parser = new EbnfParser(tokens);
        return parser.parse();
    }

    private EbnfGrammar parse() {
        List<EbnfRule> rules = new ArrayList<>();
        while (!isAtEnd()) {
            EbnfRule rule = parseRule();
            rules.add(rule);
        }
        return new EbnfGrammar(rules);
    }

    private EbnfRule parseRule() {
        EbnfToken token = expect(EbnfTokenType.IDENTIFIER);
        expect(EbnfTokenType.EQUALS);
        EbnfNode expression = parseExpression();
        expect(EbnfTokenType.SEMICOLON);
        String tokenText = token.getText();
        return new EbnfRule(tokenText, expression);
    }

    private EbnfNode parseExpression() {
        List<EbnfNode> expressions = new ArrayList<>();
        expressions.add(parseSequence());
        while (match(EbnfTokenType.PIPE)) {
            expressions.add(parseSequence());
        }
        if (expressions.size() == 1) {
            return expressions.get(0);
        } else {
            return new EbnfChoice(expressions);
        }
    }

    private EbnfNode parseSequence() {
        List<EbnfNode> expressions = new ArrayList<>();
        expressions.add(parseFactor());
        while (match(EbnfTokenType.COMMA)) {
            expressions.add(parseFactor());
        }
        if (expressions.size() == 1) {
            return expressions.get(0);
        } else {
            return new EbnfSequence(expressions);
        }
    }

    private EbnfNode parseFactor() {
        EbnfToken token = peek();
        EbnfTokenType tokenType = token.getType();
        String tokenText = token.getText();
        if (tokenType == EbnfTokenType.IDENTIFIER) {
            consume();
            return new EbnfRuleReference(tokenText);
        }
        if (tokenType == EbnfTokenType.STRING) {
            consume();
            return new EbnfTerminal(tokenText);
        }
        if (tokenType == EbnfTokenType.LPAREN) {
            consume();
            EbnfNode node = parseExpression();
            expect(EbnfTokenType.RPAREN);
            return node;
        }
        if (tokenType == EbnfTokenType.LBRACKET) {
            consume();
            EbnfNode node = parseExpression();
            expect(EbnfTokenType.RBRACKET);
            return new EbnfOptional(node);
        }
        if (tokenType == EbnfTokenType.LBRACE) {
            consume();
            EbnfNode node = parseExpression();
            expect(EbnfTokenType.RBRACE);
            return new EbnfRepeat(node);
        }
        throw new EbnfParseException("Unexpected token: " + tokenType + " (" + tokenText + ")");
    }

    private EbnfToken peek() {
        return _tokens.get(_position);
    }

    private EbnfToken consume() {
        EbnfToken token = peek();
        _position++;
        return token;
    }

    private boolean match(final EbnfTokenType expectedTokenType) {
        if (check(expectedTokenType)) {
            consume();
            return true;
        } else {
            return false;
        }
    }

    private EbnfToken expect(final EbnfTokenType expectedTokenType) {
        if (check(expectedTokenType)) {
            return consume();
        } else {
            EbnfToken token = peek();
            EbnfTokenType actualTokenType = token.getType();
            throw new EbnfParseException("Expected " + expectedTokenType + " but was " + actualTokenType);
        }
    }

    private boolean check(final EbnfTokenType expectedTokenType) {
        EbnfToken token = peek();
        EbnfTokenType actualTokenType = token.getType();
        return actualTokenType == expectedTokenType;
    }

    private boolean isAtEnd() {
        return check(EbnfTokenType.EOF);
    }

}
