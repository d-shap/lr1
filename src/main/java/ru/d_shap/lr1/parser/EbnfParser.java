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
import java.util.List;

import ru.d_shap.lr1.EbnfParseException;
import ru.d_shap.lr1.Position;
import ru.d_shap.lr1.ebnf.EbnfChoice;
import ru.d_shap.lr1.ebnf.EbnfExcept;
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
        int line = token.getPosition().getLine();
        int column = token.getPosition().getColumn();
        String tokenText = token.getTokenValue();
        expect(EbnfTokenType.EQUALS);
        EbnfNode node = parseExpression();
        expect(EbnfTokenType.SEMICOLON);
        return new EbnfRule(new Position(line, column), tokenText, node);
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
            EbnfNode node = nodes.get(0);
            Position position = node.getPosition();
            return new EbnfChoice(position, nodes);
        }
    }

    private EbnfNode parseSequence() {
        List<EbnfNode> nodes = new ArrayList<>();
        do {
            EbnfNode node = parseExcept();
            nodes.add(node);
        } while (match(EbnfTokenType.COMMA));
        if (nodes.size() == 1) {
            return nodes.get(0);
        } else {
            EbnfNode node = nodes.get(0);
            Position position = node.getPosition();
            return new EbnfSequence(position, nodes);
        }
    }

    private EbnfNode parseExcept() {
        EbnfNode node = parseRepeat();
        if (match(EbnfTokenType.MINUS)) {
            Position position = node.getPosition();
            EbnfNode exception = parseRepeat();
            return new EbnfExcept(position, node, exception);
        }
        return node;
    }

    private EbnfNode parseRepeat() {
        EbnfNode node = parseFactor();
        if (match(EbnfTokenType.ASTERISK)) {
            Position position = node.getPosition();
            return new EbnfRepeat(position, node, EbnfRepeatOperator.ZERO_OR_MANY);
        }
        if (match(EbnfTokenType.PLUS)) {
            Position position = node.getPosition();
            return new EbnfRepeat(position, node, EbnfRepeatOperator.ONE_OR_MANY);
        }
        if (match(EbnfTokenType.QUESTION)) {
            Position position = node.getPosition();
            return new EbnfRepeat(position, node, EbnfRepeatOperator.ZERO_OR_ONE);
        }
        return node;
    }

    private EbnfNode parseFactor() {
        EbnfToken token = peek();
        EbnfTokenType tokenType = token.getTokenType();
        String tokenText = token.getTokenValue();
        int line = token.getPosition().getLine();
        int column = token.getPosition().getColumn();
        if (tokenType == EbnfTokenType.IDENTIFIER) {
            consume();
            return new EbnfReference(new Position(line, column), tokenText);
        }
        if (tokenType == EbnfTokenType.STRING) {
            consume();
            return new EbnfTerminal(new Position(line, column), tokenText);
        }
        if (tokenType == EbnfTokenType.QUESTION) {
            consume();
            StringBuilder text = new StringBuilder();
            while (!isAtEnd() && !check(EbnfTokenType.QUESTION)) {
                text.append(peek().getTokenValue());
                consume();
            }
            expect(EbnfTokenType.QUESTION);
            return new EbnfSpecial(new Position(line, column), text.toString().trim());
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
            return new EbnfOptional(new Position(line, column), node);
        }
        if (tokenType == EbnfTokenType.LBRACE) {
            consume();
            EbnfNode node = parseExpression();
            expect(EbnfTokenType.RBRACE);
            return new EbnfRepeat(new Position(line, column), node, EbnfRepeatOperator.ZERO_OR_MANY);
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
