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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * The EBNF tokenizer.
 *
 * @author Dmitry Shapovalov
 */
public final class EbnfTokenizer {

    private final Reader _reader;

    private int _line;

    private int _column;

    private int _currentChar;

    private int _nextChar;

    private EbnfTokenizer(final Reader reader) {
        super();
        _reader = reader;
        _line = 1;
        _column = 1;
        readNextChar();
        readNextChar();
    }

    /**
     * Tokenize the string.
     *
     * @param string the string.
     *
     * @return the EBNF tokens.
     */
    public static List<EbnfToken> tokenize(final String string) {
        Reader reader = new StringReader(string);
        return tokenize(reader);
    }

    /**
     * Tokenize the input stream.
     *
     * @param inputStream the input stream.
     *
     * @return the EBNF tokens.
     */
    public static List<EbnfToken> tokenize(final InputStream inputStream) {
        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        return tokenize(reader);
    }

    /**
     * Tokenize the reader.
     *
     * @param reader the reader.
     *
     * @return the EBNF tokens.
     */
    public static List<EbnfToken> tokenize(final Reader reader) {
        EbnfTokenizer tokenizer = new EbnfTokenizer(reader);
        return tokenizer.tokenize();
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
        EbnfToken token = new EbnfToken(EbnfTokenType.EOF, "", _line, _column);
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
            return new EbnfToken(EbnfTokenType.EQUALS, "=", startLine, startColumn);
        }
        if (currentChar == ',') {
            advance();
            return new EbnfToken(EbnfTokenType.COMMA, ",", startLine, startColumn);
        }
        if (currentChar == '|') {
            advance();
            return new EbnfToken(EbnfTokenType.PIPE, "|", startLine, startColumn);
        }
        if (currentChar == ';') {
            advance();
            return new EbnfToken(EbnfTokenType.SEMICOLON, ";", startLine, startColumn);
        }
        if (currentChar == '(') {
            advance();
            return new EbnfToken(EbnfTokenType.LPAREN, "(", startLine, startColumn);
        }
        if (currentChar == ')') {
            advance();
            return new EbnfToken(EbnfTokenType.RPAREN, ")", startLine, startColumn);
        }
        if (currentChar == '[') {
            advance();
            return new EbnfToken(EbnfTokenType.LBRACKET, "[", startLine, startColumn);
        }
        if (currentChar == ']') {
            advance();
            return new EbnfToken(EbnfTokenType.RBRACKET, "]", startLine, startColumn);
        }
        if (currentChar == '{') {
            advance();
            return new EbnfToken(EbnfTokenType.LBRACE, "{", startLine, startColumn);
        }
        if (currentChar == '}') {
            advance();
            return new EbnfToken(EbnfTokenType.RBRACE, "}", startLine, startColumn);
        }
        if (currentChar == '?') {
            advance();
            return new EbnfToken(EbnfTokenType.QUESTION, "?", startLine, startColumn);
        }
        if (currentChar == '+') {
            advance();
            return new EbnfToken(EbnfTokenType.PLUS, "+", startLine, startColumn);
        }
        if (currentChar == '-') {
            advance();
            return new EbnfToken(EbnfTokenType.PLUS, "-", startLine, startColumn);
        }
        if (currentChar == '*') {
            advance();
            return new EbnfToken(EbnfTokenType.ASTERISK, "*", startLine, startColumn);
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
        return new EbnfToken(EbnfTokenType.IDENTIFIER, text.toString(), startLine, startColumn);
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
        return new EbnfToken(EbnfTokenType.STRING, text.toString(), startLine, startColumn);
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

}
