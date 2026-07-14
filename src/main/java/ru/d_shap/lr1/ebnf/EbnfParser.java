package ru.d_shap.lr1.ebnf;

import java.util.ArrayList;
import java.util.List;

import ru.d_shap.lr1.ebnf.model.EbnfChoice;
import ru.d_shap.lr1.ebnf.model.EbnfNode;
import ru.d_shap.lr1.ebnf.model.EbnfOptional;
import ru.d_shap.lr1.ebnf.model.EbnfRepeat;
import ru.d_shap.lr1.ebnf.model.EbnfRule;
import ru.d_shap.lr1.ebnf.model.EbnfRuleReference;
import ru.d_shap.lr1.ebnf.model.EbnfSequence;
import ru.d_shap.lr1.ebnf.model.EbnfTerminal;

public final class EbnfParser {

    private final List<EbnfToken> tokens;

    private int position = 0;

    public EbnfParser(final List<EbnfToken> tokens) {
        super();
        this.tokens = tokens;
    }

    public List<EbnfRule> parseGrammar() {
        List<EbnfRule> rules = new ArrayList<>();

        while (!isAtEnd()) {
            rules.add(parseRule());
        }

        return rules;
    }

    private EbnfRule parseRule() {

        EbnfToken name = expect(EbnfTokenType.IDENTIFIER);

        expect(EbnfTokenType.EQUALS);

        EbnfNode expression = parseExpression();

        expect(EbnfTokenType.SEMICOLON);

        return new EbnfRule(
                name.getText(),
                expression
        );
    }

    private EbnfNode parseExpression() {

        List<EbnfNode> alternatives = new ArrayList<>();

        alternatives.add(parseSequence());

        while (match(EbnfTokenType.PIPE)) {
            alternatives.add(parseSequence());
        }

        if (alternatives.size() == 1) {
            return alternatives.get(0);
        }

        return new EbnfChoice(alternatives);
    }

    private EbnfNode parseSequence() {

        List<EbnfNode> elements = new ArrayList<>();

        elements.add(parseFactor());

        while (match(EbnfTokenType.COMMA)) {
            elements.add(parseFactor());
        }

        if (elements.size() == 1) {
            return elements.get(0);
        }

        return new EbnfSequence(elements);
    }

    private EbnfNode parseFactor() {
        EbnfToken token = peek();
        EbnfTokenType tokenType = token.getType();
        if (tokenType == EbnfTokenType.IDENTIFIER) {
            consume();
            return new EbnfRuleReference(token.getText());
        }
        if (tokenType == EbnfTokenType.STRING) {
            consume();
            return new EbnfTerminal(token.getText());
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
        throw error(
                "Unexpected token: "
                        + token.getType()
                        + " (" + token.getText() + ")"
        );
    }

    private boolean match(final EbnfTokenType type) {
        if (check(type)) {
            consume();
            return true;
        }
        return false;
    }

    private EbnfToken expect(final EbnfTokenType type) {
        if (!check(type)) {
            throw error(
                    "Expected "
                            + type
                            + " but was "
                            + peek().getType()
            );
        }

        return consume();
    }

    private boolean check(final EbnfTokenType type) {
        if (isAtEnd()) {
            return type == EbnfTokenType.EOF;
        }
        return peek().getType() == type;
    }

    private EbnfToken consume() {
        EbnfToken token = peek();
        position++;
        return token;
    }

    private EbnfToken peek() {
        return tokens.get(position);
    }

    private boolean isAtEnd() {
        return peek().getType() == EbnfTokenType.EOF;
    }

    private RuntimeException error(final String message) {
        return new RuntimeException(message);
    }

}
