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
        //CODE - Program Structure
        BEGIN_CODE,
        END_CODE,
        DISPLAY,
        CONCATENATE,
        LINE_BREAK,
        NEWLINE,
        LEFT_BRACE, RIGHT_BRACE, // Escape Code
        COMMA, COLON, // for Variable Declaration and Statement Declaration
        // Assignment Operator -> '='
        ASSIGN,

        // CODE - Arithmetic Operators
        PLUS, MINUS, MULTIPLY,DIVIDE, MODULO, LEFT_PAREN, RIGHT_PAREN, GREATER_THAN, LESS_THAN, GREATER_THAN_EQUAL, LESS_THAN_EQUAL, EQUAL, NOT_EQUAL,
        // CODE - Logical Operators
        AND, OR, NOT,
        // CODE - Unary Operator
        POSITIVE, NEGATIVE,
        // CODE -Identifier/ Keywords
        INTEGER_LITERAL,
        CHAR_LITERAL,
        BOOLEAN_LITERAL,
        STRING_LITERAL,
        FLOAT_LITERAL,
        FLOAT,
        IF, ELSE, IF_ELSE, IDENTIFIER, KEYWORD,
        // End of File
        EOF,
        CHAR,
        INTEGER,
        OPERATOR,

        PRINT,
        BOOLEAN,
        SCAN,
        COMMENT,

        NUM,
        NUM_FLOAT;

        private Type() {
        }

    }


}
