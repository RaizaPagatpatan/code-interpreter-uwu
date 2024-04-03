package com.hgroupeight.interpreter.parser;

class Token {

    public enum Type {
        BEGIN_CODE,
        END_CODE,
        KEYWORD,
        IDENTIFIER,
        DISPLAY,
        CHAR_LITERAL,
        STRING_LITERAL,
        INTEGER_LITERAL,
        OPERATOR,
        SEMICOLON,
        DOLLAR,
        AMPERSAND,
        LEFT_BRACE,
        RIGHT_BRACE,
        PLUS,
        MINUS,
        MULTIPLY,
        DIVIDE,
        MODULO,
        LEFT_PAREN,
        RIGHT_PAREN,
        EQUAL,
        ASSIGN,
        GREATER_THAN,
        GREATER_THAN_EQUAL,
        LESS_THAN,
        LESS_THAN_EQUAL,
        NOT_EQUAL,
        NOT,
        EOF
    }


    private Type type;
    private String value;

    // constructor
    public Token(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    // getter methods

    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    //setter ?_? i don't think needed na ni, pero ako lang gi alt insert

    public void setType(Type type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
