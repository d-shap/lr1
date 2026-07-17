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
public final class EbnfParser {

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
        String tokenText = token.getTokenText();
        expect(EbnfTokenType.EQUALS);
        EbnfNode node = parseExpression();
        expect(EbnfTokenType.SEMICOLON);
        return new EbnfRule(tokenText, node);
    }

    private EbnfNode parseExpression() {
        List<EbnfNode> nodes = new ArrayList<>();
        do {
            EbnfNode node = parseSequence();
            nodes.add(node);
        } while (match(EbnfTokenType.PIPE));
        if (nodes.size() == 1) {
            return nodes.get(0);
        } else {
            return new EbnfChoice(nodes);
        }
    }

    private EbnfNode parseSequence() {
        List<EbnfNode> nodes = new ArrayList<>();
        do {
            EbnfNode node = parseFactor();
            nodes.add(node);
        } while (match(EbnfTokenType.COMMA));
        if (nodes.size() == 1) {
            return nodes.get(0);
        } else {
            return new EbnfSequence(nodes);
        }
    }

    private EbnfNode parseFactor() {
        EbnfToken token = peek();
        EbnfTokenType tokenType = token.getTokenType();
        String tokenText = token.getTokenText();
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
            EbnfTokenType actualTokenType = token.getTokenType();
            throw new EbnfParseException("Expected " + expectedTokenType + " but was " + actualTokenType);
        }
    }

    private boolean isAtEnd() {
        if (check(EbnfTokenType.EOF)) {
            consume();
            return true;
        } else {
            return false;
        }
    }

    private boolean check(final EbnfTokenType expectedTokenType) {
        EbnfToken token = peek();
        EbnfTokenType actualTokenType = token.getTokenType();
        return actualTokenType == expectedTokenType;
    }

}
