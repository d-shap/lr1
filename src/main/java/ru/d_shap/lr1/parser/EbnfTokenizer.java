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
package ru.d_shap.lr1.parser;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.d_shap.lr1.EbnfException;
import ru.d_shap.lr1.EbnfParseException;
import ru.d_shap.lr1.Position;
import ru.d_shap.lr1.source.CharConsumerEx;

/**
 * The EBNF tokenizer.
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfTokenizer<R> implements CharConsumerEx<R> {

    private static final long serialVersionUID = 1L;

    private final EbnfTokenConsumer<R> _tokenConsumer;

    private final State _defaultState;

    private State _currentState;

    /**
     * Create new object.
     *
     * @param tokenConsumer the token consumer.
     */
    public EbnfTokenizer(final EbnfTokenConsumer<R> tokenConsumer) {
        super();
        if (tokenConsumer == null) {
            throw new NullPointerException("Token consumer should not be null");
        }
        _tokenConsumer = tokenConsumer;

        _defaultState = new DefaultState();
        _currentState = null;
    }

    @Override
    public void reset() {
        _tokenConsumer.reset();
        _currentState = _defaultState;
    }

    @Override
    public void accept(final int line, final int column, final int ch, final int next) {
        _currentState = _currentState.accept(line, column, ch, next);
    }

    @Override
    public R getResult() {
        return _tokenConsumer.getResult();
    }

    private List<EbnfToken> tokenize() {
        List<EbnfToken> tokens = new ArrayList<>();
        while (!isAtEnd()) {
            skipWhitespaceAndComments();
            if (isAtEnd()) {
                break;
            }
            EbnfToken token = nextToken();
            tokens.add(token);
        }
        EbnfToken token = new EbnfToken(new Position(_line, _column), EbnfTokenType.EOF, "");
        tokens.add(token);
        return tokens;
    }

    private void skipWhitespaceAndComments() {
        while (!isAtEnd()) {
            char currentChar = peek();
            if (currentChar == ' ' || currentChar == '\t' || currentChar == '\r') {
                advance();
            } else if (currentChar == '\n') {
                advance();
                _line++;
                _column = 1;
            } else if (currentChar == '(' && peekNext() == '*') {
                // Skip EBNF comment (* ... *)
                skipEbnfComment();
            } else {
                break;
            }
        }
    }

    private void skipEbnfComment() {
        advance(); // skip '('
        advance(); // skip '*'
        int depth = 1;
        while (!isAtEnd() && depth > 0) {
            if (peek() == '(' && peekNext() == '*') {
                // Nested comment start
                advance();
                advance();
                depth++;
            } else if (peek() == '*' && peekNext() == ')') {
                // Comment end
                advance();
                advance();
                depth--;
            } else {
                if (peek() == '\n') {
                    _line++;
                    _column = 1;
                }
                advance();
            }
        }
    }

    private EbnfToken nextToken() {
        int startLine = _line;
        int startColumn = _column;
        char currentChar = peek();

        // Single-character tokens
        if (currentChar == '=') {
            advance();
            return new EbnfToken(new Position(startLine, startColumn), EbnfTokenType.EQUALS, "");
        }
        if (currentChar == ',') {
            advance();
            return new EbnfToken(new Position(startLine, startColumn), EbnfTokenType.COMMA, "");
        }
        if (currentChar == '|') {
            advance();
            return new EbnfToken(new Position(startLine, startColumn), EbnfTokenType.PIPE, "");
        }
        if (currentChar == ';') {
            advance();
            return new EbnfToken(new Position(startLine, startColumn), EbnfTokenType.SEMICOLON, "");
        }
        if (currentChar == '(') {
            advance();
            return new EbnfToken(new Position(startLine, startColumn), EbnfTokenType.LPAREN, "");
        }
        if (currentChar == ')') {
            advance();
            return new EbnfToken(new Position(startLine, startColumn), EbnfTokenType.RPAREN, "");
        }
        if (currentChar == '[') {
            advance();
            return new EbnfToken(new Position(startLine, startColumn), EbnfTokenType.LBRACKET, "");
        }
        if (currentChar == ']') {
            advance();
            return new EbnfToken(new Position(startLine, startColumn), EbnfTokenType.RBRACKET, "");
        }
        if (currentChar == '{') {
            advance();
            return new EbnfToken(new Position(startLine, startColumn), EbnfTokenType.LBRACE, "");
        }
        if (currentChar == '}') {
            advance();
            return new EbnfToken(new Position(startLine, startColumn), EbnfTokenType.RBRACE, "");
        }
        if (currentChar == '?') {
            advance();
            return new EbnfToken(new Position(startLine, startColumn), EbnfTokenType.QUESTION, "");
        }
        if (currentChar == '+') {
            advance();
            return new EbnfToken(new Position(startLine, startColumn), EbnfTokenType.PLUS, "");
        }
        if (currentChar == '-') {
            advance();
            return new EbnfToken(new Position(startLine, startColumn), EbnfTokenType.PLUS, "");
        }
        if (currentChar == '*') {
            advance();
            return new EbnfToken(new Position(startLine, startColumn), EbnfTokenType.ASTERISK, "");
        }

        // String tokens (quoted with single or double quotes)
        if (currentChar == '"' || currentChar == '\'') {
            return parseString(currentChar, startLine, startColumn);
        }

        // Identifier tokens (letters, digits, underscores)
        if (isIdentifierStart(currentChar)) {
            return parseIdentifier(startLine, startColumn);
        }

        throw new EbnfException("Unexpected character: '" + currentChar + "' at line " + startLine + ", column " + startColumn);
    }

    private EbnfToken parseIdentifier(final int startLine, final int startColumn) {
        StringBuilder text = new StringBuilder();
        while (!isAtEnd() && isIdentifierPart(peek())) {
            text.append(peek());
            advance();
        }
        return new EbnfToken(new Position(startLine, startColumn), EbnfTokenType.IDENTIFIER, text.toString());
    }

    private boolean isIdentifierStart(final char ch) {
        return ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch == '_';
    }

    private boolean isIdentifierPart(final char ch) {
        return isIdentifierStart(ch) || ch >= '0' && ch <= '9';
    }

    private EbnfToken parseString(final char quoteChar, final int startLine, final int startColumn) {
        advance();
        StringBuilder text = new StringBuilder();
        while (!isAtEnd() && peek() != quoteChar) {
            if (peek() == '\\') {
                advance();
                if (!isAtEnd()) {
                    char escaped = peek();
                    switch (escaped) {
                        case 'n':
                            text.append('\n');
                            break;
                        case 't':
                            text.append('\t');
                            break;
                        case 'r':
                            text.append('\r');
                            break;
                        case '\\':
                            text.append('\\');
                            break;
                        case '"':
                            text.append('"');
                            break;
                        case '\'':
                            text.append('\'');
                            break;
                        default:
                            text.append(escaped);
                            break;
                    }
                    advance();
                }
            } else {
                text.append(peek());
                advance();
            }
        }
        if (isAtEnd()) {
            throw new EbnfException("Unterminated string at line " + startLine + ", column " + startColumn);
        }
        advance();
        return new EbnfToken(new Position(startLine, startColumn), EbnfTokenType.STRING, text.toString());
    }

    private char peek() {
        if (isAtEnd()) {
            return '\0';
        } else {
            return (char) _currentChar;
        }
    }

    private char peekNext() {
        if (isAtEnd()) {
            return '\0';
        } else {
            return (char) _nextChar;
        }
    }

    private void advance() {
        if (!isAtEnd()) {
            if (_currentChar == '\n') {
                _line++;
                _column = 1;
            } else {
                _column++;
            }
            readNextChar();
        }
    }

    private void readNextChar() {
        try {
            _currentChar = _nextChar;
            _nextChar = _reader.read();
        } catch (IOException ex) {
            throw new EbnfParseException("Read exception", ex);
        }
    }

    private boolean isAtEnd() {
        return _currentChar < 0;
    }

    private abstract static class State implements Serializable {

        private static final long serialVersionUID = 1L;

        State() {
            super();
        }

        abstract State accept(int line, int column, int ch, int next);

    }

    private final class DefaultState extends State {

        private static final long serialVersionUID = 1L;

        DefaultState() {
            super();
        }

        @Override
        State accept(final int line, final int column, final int ch, final int next) {
            // Single-character tokens
            if (ch == '=') {
                Position position = new Position(line, column);
                EbnfToken token = new EbnfToken(position, EbnfTokenType.EQUALS, "");
                _tokenConsumer.accept(token);
                return _defaultState;
            }
            if (ch == ',') {
                Position position = new Position(line, column);
                EbnfToken token = new EbnfToken(position, EbnfTokenType.COMMA, "");
                _tokenConsumer.accept(token);
                return _defaultState;
            }
            if (ch == '|') {
                Position position = new Position(line, column);
                EbnfToken token = new EbnfToken(position, EbnfTokenType.PIPE, "");
                _tokenConsumer.accept(token);
                return _defaultState;
            }
            if (ch == ';') {
                Position position = new Position(line, column);
                EbnfToken token = new EbnfToken(position, EbnfTokenType.SEMICOLON, "");
                _tokenConsumer.accept(token);
                return _defaultState;
            }
            if (ch == '(') {
                Position position = new Position(line, column);
                EbnfToken token = new EbnfToken(position, EbnfTokenType.LPAREN, "");
                _tokenConsumer.accept(token);
                return _defaultState;
            }
            if (ch == ')') {
                Position position = new Position(line, column);
                EbnfToken token = new EbnfToken(position, EbnfTokenType.RPAREN, "");
                _tokenConsumer.accept(token);
                return _defaultState;
            }
            if (ch == '[') {
                Position position = new Position(line, column);
                EbnfToken token = new EbnfToken(position, EbnfTokenType.LBRACKET, "");
                _tokenConsumer.accept(token);
                return _defaultState;
            }
            if (ch == ']') {
                Position position = new Position(line, column);
                EbnfToken token = new EbnfToken(position, EbnfTokenType.RBRACKET, "");
                _tokenConsumer.accept(token);
                return _defaultState;
            }
            if (ch == '{') {
                Position position = new Position(line, column);
                EbnfToken token = new EbnfToken(position, EbnfTokenType.LBRACE, "");
                _tokenConsumer.accept(token);
                return _defaultState;
            }
            if (ch == '}') {
                Position position = new Position(line, column);
                EbnfToken token = new EbnfToken(position, EbnfTokenType.RBRACE, "");
                _tokenConsumer.accept(token);
                return _defaultState;
            }
            if (ch == '?') {
                Position position = new Position(line, column);
                EbnfToken token = new EbnfToken(position, EbnfTokenType.QUESTION, "");
                _tokenConsumer.accept(token);
                return _defaultState;
            }
            if (ch == '+') {
                Position position = new Position(line, column);
                EbnfToken token = new EbnfToken(position, EbnfTokenType.PLUS, "");
                _tokenConsumer.accept(token);
                return _defaultState;
            }
            if (ch == '-') {
                Position position = new Position(line, column);
                EbnfToken token = new EbnfToken(position, EbnfTokenType.MINUS, "");
                _tokenConsumer.accept(token);
                return _defaultState;
            }
            if (ch == '*') {
                Position position = new Position(line, column);
                EbnfToken token = new EbnfToken(position, EbnfTokenType.ASTERISK, "");
                _tokenConsumer.accept(token);
                return _defaultState;
            }
        }

    }

}
