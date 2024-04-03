package com.hgroupeight.interpreter.lexer;

public class Token {

    private Type type;
    private String value;
    private int position;

    public Token(Type type, String value, int position) {
        this.type = type;
        this.value = value;
        this.position = position;
    }


    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", value='" + value + '\'' +
                ", position=" + position +
                '}';
    }

    //getter methods
    public Type getType() {
        return type;
    }
    public String getValue() {
        return value;
    }

    //setter ?_? i don't think needed na ni, pero ako lang gi alt insert
//    public void setType(Type type) {
//        this.type = type;
//    }
    public void setValue(String value) {
        this.value = value;
    }
    public static Token fromReservedWordOrIdentifier(String string) {
        return null;
    }

    public int getPosition() {
        return position; // Return the position of the token
    }


    public enum Type {
        BEGIN_CODE,
        END_CODE,
        CHAR,
        INTEGER,
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
        IF,
        BOOLEAN_LITERAL,
        AND,
        PRINT,
        OR,
        FLOAT,
        BOOLEAN,
        SCAN,
        COLON,
        COMMENT,
        FLOAT_LITERAL,
        NUM,
        NUM_FLOAT,
        IF_ELSE,
        ELSE,
        EOF,
        CONCATENATE,
        NEWLINE,
        COMMA;

        private Type() {
        }

    }


}
