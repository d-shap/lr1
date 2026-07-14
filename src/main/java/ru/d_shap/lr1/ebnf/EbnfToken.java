package ru.d_shap.lr1.ebnf;

final class EbnfToken {

    EbnfTokenType type;

    String text;

    int line;

    int column;

    EbnfToken(final EbnfTokenType type, final String text, final int line, final int column) {
        super();
        this.type = type;
        this.text = text;
        this.line = line;
        this.column = column;
    }

    public EbnfTokenType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

}
