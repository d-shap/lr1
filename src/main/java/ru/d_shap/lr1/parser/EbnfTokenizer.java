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

import java.io.Serializable;

import ru.d_shap.lr1.EbnfException;
import ru.d_shap.lr1.Position;
import ru.d_shap.lr1.source.CharConsumerEx;

/**
 * The EBNF tokenizer.
 *
 * @param <R> the generic type of the result.
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfTokenizer<R> implements CharConsumerEx<R> {

    private static final long serialVersionUID = 1L;

    private final EbnfTokenConsumer<R> _tokenConsumer;

    private final State _defaultState;

    private final State _commentStartState;

    private final State _commentState;

    private final State _commentEndState;

    private final State _stringQuotState;

    private final State _stringQuotEscapeState;

    private final State _stringAposState;

    private final State _stringAposEscapeState;

    private final State _identifierState;

    private State _currentState;

    private final StringBuilder _text;

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
        _commentStartState = new CommentStartState();
        _commentState = new CommentState();
        _commentEndState = new CommentEndState();
        _stringQuotState = new StringQuotState();
        _stringQuotEscapeState = new StringQuotEscapeState();
        _stringAposState = new StringAposState();
        _stringAposEscapeState = new StringAposEscapeState();
        _identifierState = new IdentifierState();

        _currentState = null;
        _text = new StringBuilder();
    }

    @Override
    public void reset() {
        _tokenConsumer.reset();
        _currentState = _defaultState;
        _text.setLength(0);
    }

    @Override
    public void accept(final int line, final int column, final int ch, final int next) {
        _currentState = _currentState.accept(line, column, ch, next);
    }

    @Override
    public R getResult() {
        return _tokenConsumer.getResult();
    }

    private boolean isIdentifierStart(final int ch) {
        return ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch == '_';
    }

    private boolean isIdentifierPart(final int ch) {
        return isIdentifierStart(ch) || ch >= '0' && ch <= '9';
    }

    abstract static class State implements Serializable {

        private static final long serialVersionUID = 1L;

        State() {
            super();
        }

        abstract State accept(int line, int column, int ch, int next);

    }

    final class DefaultState extends State {

        private static final long serialVersionUID = 1L;

        DefaultState() {
            super();
        }

        @Override
        State accept(final int line, final int column, final int ch, final int next) {
            if (ch < 0) {
                Position position = new Position(line, column);
                EbnfToken token = new EbnfToken(position, EbnfTokenType.EOF, "");
                _tokenConsumer.accept(token);
                return null;
            }

            if (Character.isWhitespace(ch)) {
                return _defaultState;
            }

            if (ch == '(' && next == '*') {
                return _commentStartState;
            }

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

            if (ch == '"') {
                _text.setLength(0);
                return _stringQuotState;
            }
            if (ch == '\'') {
                _text.setLength(0);
                return _stringAposState;
            }

            if (isIdentifierStart(ch)) {
                _text.setLength(0);
                _text.append((char) ch);
                if (isIdentifierPart(next)) {
                    return _identifierState;
                } else {
                    Position position = new Position(line, column);
                    String text = _text.toString();
                    _text.setLength(0);
                    EbnfToken token = new EbnfToken(position, EbnfTokenType.IDENTIFIER, text);
                    _tokenConsumer.accept(token);
                    return _defaultState;
                }
            }

            throw new EbnfException("Unexpected character: '" + ch + "' at line " + line + ", column " + column);
        }

    }

    final class CommentStartState extends State {

        private static final long serialVersionUID = 1L;

        CommentStartState() {
            super();
        }

        @Override
        State accept(final int line, final int column, final int ch, final int next) {
            if (ch == '*') {
                return _commentState;
            }

            throw new EbnfException("Unexpected character: '" + ch + "' at line " + line + ", column " + column);
        }

    }

    final class CommentState extends State {

        private static final long serialVersionUID = 1L;

        CommentState() {
            super();
        }

        @Override
        State accept(final int line, final int column, final int ch, final int next) {
            if (ch == '*' && next == ')') {
                return _commentEndState;
            }

            return _commentState;
        }

    }

    final class CommentEndState extends State {

        private static final long serialVersionUID = 1L;

        CommentEndState() {
            super();
        }

        @Override
        State accept(final int line, final int column, final int ch, final int next) {
            if (ch == ')') {
                return _defaultState;
            }

            throw new EbnfException("Unexpected character: '" + ch + "' at line " + line + ", column " + column);
        }

    }

    final class StringQuotState extends State {

        private static final long serialVersionUID = 1L;

        StringQuotState() {
            super();
        }

        @Override
        State accept(final int line, final int column, final int ch, final int next) {
            if (ch == '"') {
                Position position = new Position(line, column);
                String text = _text.toString();
                _text.setLength(0);
                EbnfToken token = new EbnfToken(position, EbnfTokenType.STRING, text);
                _tokenConsumer.accept(token);
                return _defaultState;
            }
            if (ch == '\\') {
                return _stringQuotEscapeState;
            }

            _text.append(ch);
            return _stringQuotState;
        }

    }

    final class StringQuotEscapeState extends State {

        private static final long serialVersionUID = 1L;

        StringQuotEscapeState() {
            super();
        }

        @Override
        State accept(final int line, final int column, final int ch, final int next) {
            if (ch == 't') {
                _text.append('\t');
                return _stringQuotState;
            }
            if (ch == 'r') {
                _text.append('\r');
                return _stringQuotState;
            }
            if (ch == 'n') {
                _text.append('\n');
                return _stringQuotState;
            }
            if (ch == '\\') {
                _text.append('\n');
                return _stringQuotState;
            }
            if (ch == '"') {
                _text.append('"');
                return _stringQuotState;
            }
            if (ch == '\'') {
                _text.append('\'');
                return _stringQuotState;
            }

            throw new EbnfException("Unexpected character: '" + ch + "' at line " + line + ", column " + column);
        }

    }

    final class StringAposState extends State {

        private static final long serialVersionUID = 1L;

        StringAposState() {
            super();
        }

        @Override
        State accept(final int line, final int column, final int ch, final int next) {
            if (ch == '\'') {
                Position position = new Position(line, column);
                String text = _text.toString();
                _text.setLength(0);
                EbnfToken token = new EbnfToken(position, EbnfTokenType.STRING, text);
                _tokenConsumer.accept(token);
                return _defaultState;
            }
            if (ch == '\\') {
                return _stringAposEscapeState;
            }

            _text.append((char) ch);
            return _stringAposState;
        }

    }

    final class StringAposEscapeState extends State {

        private static final long serialVersionUID = 1L;

        StringAposEscapeState() {
            super();
        }

        @Override
        State accept(final int line, final int column, final int ch, final int next) {
            if (ch == 't') {
                _text.append('\t');
                return _stringAposState;
            }
            if (ch == 'r') {
                _text.append('\r');
                return _stringAposState;
            }
            if (ch == 'n') {
                _text.append('\n');
                return _stringAposState;
            }
            if (ch == '\\') {
                _text.append('\\');
                return _stringAposState;
            }
            if (ch == '"') {
                _text.append('"');
                return _stringAposState;
            }
            if (ch == '\'') {
                _text.append('\'');
                return _stringAposState;
            }

            throw new EbnfException("Unexpected character: '" + ch + "' at line " + line + ", column " + column);
        }

    }

    final class IdentifierState extends State {

        private static final long serialVersionUID = 1L;

        IdentifierState() {
            super();
        }

        @Override
        State accept(final int line, final int column, final int ch, final int next) {
            if (isIdentifierPart(ch)) {
                _text.append((char) ch);
            }
            if (isIdentifierPart(next)) {
                return _identifierState;
            } else {
                Position position = new Position(line, column);
                String text = _text.toString();
                _text.setLength(0);
                EbnfToken token = new EbnfToken(position, EbnfTokenType.IDENTIFIER, text);
                _tokenConsumer.accept(token);
                return _defaultState;
            }
        }

    }

}
