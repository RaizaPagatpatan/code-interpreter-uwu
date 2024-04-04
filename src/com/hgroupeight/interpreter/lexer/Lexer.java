
package com.hgroupeight.interpreter.lexer;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
public class Lexer {
    public String code;
    private int currentPos;
    private boolean inString;

    public Lexer(String code) {
        this.code = code;
        this.currentPos = 0;
        this.inString = false;
    }

    public List<Token> lex() throws ParseException {
        List<Token> tokens = new ArrayList<>();
        Token token = getNextToken();
        while (token.getType() != Token.Type.EOF) {
            tokens.add(token);
            token = getNextToken();
            if (token == null) {
                break;
            }
        }
        return tokens;
    }

    public Token getNextToken() {
        if (currentPos >= code.length()) {
            return new Token(Token.Type.EOF, null, currentPos);
        }

        char ch = code.charAt(currentPos);

        // Skip whitespace
        while (Character.isWhitespace(ch)) {
            currentPos++;
            if (currentPos >= code.length()) {
                return new Token(Token.Type.EOF, null, currentPos);
            }
            ch = code.charAt(currentPos);
        }

        //ESCAPE CHARA
        if (ch == '[') {
            StringBuilder charLiteralBuilder = new StringBuilder();
            char nextChar = code.charAt(currentPos + 1);

            if (currentPos + 1 < code.length() && isValidEscapeChar(nextChar)) { // I should add a validation if considered as escape character ni siya ... probably add a method?
                charLiteralBuilder.append(nextChar);
                currentPos++;

                if (currentPos + 1 < code.length() && code.charAt(currentPos + 1) == ']') {
                    String charLiteral = charLiteralBuilder.toString();
                    currentPos += 2;
                    return new Token(Token.Type.CHAR_LITERAL, charLiteral, currentPos);
                } else {
                    throw new RuntimeException("Unclosed or invalid character literal starting at position " + currentPos);
                }
            } else {
                throw new RuntimeException("Invalid character following '[' at position " + (currentPos + 1));
            }
        }


        // COMMENTS
        if (ch == '#') {
            while (currentPos < code.length() && code.charAt(currentPos) != '\n') {
                currentPos++;
            }
            return getNextToken(); //SKIP COMMENTS!
        }

        // Handle other token types
        // Identifiers and keywords
        // String literals
        if (ch == '"') {
            return handleStringLiteral();
        }
        else if (Character.isAlphabetic(ch) || ch == '_') {
            return handleIdentifierOrKeyword();
        }
        // Number literals
        else if (Character.isDigit(ch)) {
            return handleNumberLiteral();
        }
        // Character literals
        else if (ch == '\'') {
            return handleCharLiteral();
        }
        else if (ch == ':') {
            currentPos++;
            return new Token(Token.Type.COLON, ":", currentPos);
        }
        else {
            // OPERATORS
            switch (ch) {
                case '+':
                    currentPos++;
                    return new Token(Token.Type.PLUS, "+", currentPos);
                case '-':
                    currentPos++;
                    return new Token(Token.Type.MINUS,"-", currentPos);
                case '*':
                    currentPos++;
                    return new Token(Token.Type.MULTIPLY,"*", currentPos);
                case '/':
                    currentPos++;
                    return new Token(Token.Type.DIVIDE,"/", currentPos);
                case '%':
                    currentPos++;
                    return new Token(Token.Type.MODULO,"%", currentPos);
                case '(':
                    currentPos++;
                    return new Token(Token.Type.LEFT_PAREN,"(", currentPos);
                case ')':
                    currentPos++;
                    return new Token(Token.Type.RIGHT_PAREN,")", currentPos);
                case '=':

                    System.out.println("CHAR AT " + code.charAt(currentPos));
                    currentPos++;
                    if (currentPos < code.length() && code.charAt(currentPos) == '=') {
                        currentPos++;
                        return new Token(Token.Type.EQUAL,"==", currentPos);
                    } else {
                        return new Token(Token.Type.ASSIGN,"=", currentPos);
                    }
                case '>':
                    currentPos++;
                    if (currentPos < code.length() && code.charAt(currentPos) == '=') {
                        currentPos++;
                        return new Token(Token.Type.GREATER_THAN_EQUAL,">=", currentPos);
                    } else {
                        return new Token(Token.Type.GREATER_THAN,">", currentPos);
                    }
                case '<':
                    currentPos++;
                    if (currentPos < code.length() && code.charAt(currentPos) == '=') {
                        currentPos++;
                        return new Token(Token.Type.LESS_THAN_EQUAL,"<=", currentPos);
                    } else if (currentPos < code.length() && code.charAt(currentPos) == '>') {
                        currentPos++;
                        return new Token(Token.Type.NOT_EQUAL,"<>", currentPos);
                    } else {
                        return new Token(Token.Type.LESS_THAN,"<", currentPos);
                    }
                case '&':
                    currentPos++;
                    return new Token(Token.Type.CONCATENATE,"&", currentPos);
                case ',':
                    currentPos++;
                    return new Token(Token.Type.COMMA,",", currentPos);
//                case ';':
////                    currentPos++;
//                    currentPos++;
//                    return getNextToken();
////                    return new Token(Token.Type.SEMICOLON,";", currentPos);
                case '\n': // Treat line breaks as token separators
                    currentPos++;
                    return new Token(Token.Type.LINE_BREAK, "\n", currentPos);
                case ':':
                    currentPos++;
                    return new Token(Token.Type.COLON,":", currentPos);
                case '$':
                    currentPos++;
                    return new Token(Token.Type.NEWLINE,"$", currentPos);
                case '[':
                    currentPos++;
                    return new Token(Token.Type.LEFT_BRACE,"[", currentPos);
                case ']':
                    currentPos++;
                    return new Token(Token.Type.RIGHT_BRACE,"]", currentPos);
                default:
                    throw new RuntimeException("Unexpected character: " + ch);
            }
        }
    }

    private Token handleIdentifierOrKeyword() {
        StringBuilder text = new StringBuilder();
        int tokenStartPos = currentPos;
        while (currentPos < code.length() && (Character.isLetterOrDigit(code.charAt(currentPos)) || code.charAt(currentPos) == '_')) {
            text.append(code.charAt(currentPos));
            currentPos++;
        }
        String identifier = text.toString(); // Convert to upper case for case-insensitive comparison
        Token.Type type = null;

        // Check if the identifier is "INT"
//        if (identifier.equals("INT")) {
//            System.out.println("Found INT keyword");
//            type = Token.Type.INTEGER_LITERAL; // Or any suitable type for representing "INT"
////            return new Token(type, identifier, tokenStartPos);
//        }

        // Handle keywords properly
        switch (identifier.toUpperCase()) {
            case "AND":
                type = Token.Type.AND;
                break;
            case "DISPLAY":
                if (currentPos < code.length() && code.charAt(currentPos) == ':') {
                    currentPos++;
                    type = Token.Type.DISPLAY;
                }
                break;
            case "OR":
                type = Token.Type.OR;
                break;
            case "NOT":
                type = Token.Type.NOT;
                break;
            case "INT":
                System.out.println("INT");
                type = Token.Type.INTEGER_LITERAL;
                break;
            case "CHAR":
                type = Token.Type.CHAR_LITERAL;
                break;
            case "BOOL":
                type = Token.Type.BOOLEAN_LITERAL;
                break;
            case "FLOAT":
                type = Token.Type.FLOAT;
                break;
            case "BEGIN":
                if (currentPos < code.length() && code.charAt(currentPos) == ' ' && currentPos + 4 < code.length() && code.substring(currentPos + 1, currentPos + 5).equals("CODE")) {
                    currentPos += 5;
                    return new Token(Token.Type.BEGIN_CODE, "BEGIN CODE", tokenStartPos);
                }
                type = Token.Type.BEGIN_CODE;
                break;
            case "END":
                if (currentPos < code.length() && code.charAt(currentPos) == ' ' && currentPos + 4 < code.length() && code.substring(currentPos + 1, currentPos + 5).equals("CODE")) {
                    currentPos += 5;
                    return new Token(Token.Type.END_CODE, "END CODE", tokenStartPos);
                }
                type = Token.Type.END_CODE;
                break;
            case "IF":
                type = Token.Type.IF;
                break;
            case "ELSE":
                if (currentPos < code.length() && code.charAt(currentPos) == ' ' && currentPos + 2 < code.length() && code.substring(currentPos + 1, currentPos + 3).equals("IF")) {
                    currentPos += 3;
                    return new Token(Token.Type.IF_ELSE, "ELSE IF", tokenStartPos);
                }
                type = Token.Type.ELSE;
                break;
            default:
                if (Character.isLetter(identifier.charAt(0)) || identifier.charAt(0) == '_') {
                    type = Token.Type.IDENTIFIER;
                } else {
                    throw new RuntimeException("Invalid identifier '" + identifier + "' at position " + currentPos);
                }
        }
        return new Token(type, identifier, tokenStartPos);
    }

    private Token handleNumberLiteral() {
        StringBuilder value = new StringBuilder();
        int tokenStartPos = currentPos;
        while (currentPos < code.length() && Character.isDigit(code.charAt(currentPos))) {
            value.append(code.charAt(currentPos));
            currentPos++;
        }
        System.out.println("NUMBER" + value);
        return new Token(Token.Type.INTEGER_LITERAL, value.toString(), tokenStartPos);
    }

    private Token handleCharLiteral() {
        StringBuilder value = new StringBuilder();
        int tokenStartPos = currentPos;
        currentPos++;
        if (currentPos < code.length()) {
            value.append(code.charAt(currentPos));
            currentPos++;
        }
        currentPos++;
        return new Token(Token.Type.CHAR_LITERAL, value.toString(), tokenStartPos);
    }

    private boolean isValidEscapeChar(char c) {
        return c == '$' || c == '&' || c == '[' || c == ']' || c == '\'' || c == '"' || c == '#';
    }

    private Token handleStringLiteral() {
        StringBuilder value = new StringBuilder();
        int tokenStartPos = currentPos;
        inString = !inString;
        if (!inString) {
            currentPos++;
            while (currentPos < code.length() && code.charAt(currentPos) != '"') {
                value.append(code.charAt(currentPos));
                currentPos++;
            }
            currentPos++;
        }
        return new Token(Token.Type.STRING_LITERAL, value.toString(), tokenStartPos);
    }

    public void consume(Token.Type type, String value) throws ParseException {
        Token token = getNextToken();
        if (token == null || token.getType() != type || !token.getValue().equals(value)) { // Check if token type and value match the expected ones
            throw new ParseException("Expected token '" + value + "' but found '" + (token != null ? token.getValue() : "null") + "'", 0); // Assuming 0 as position
        }
    }

    public Token peek() {
        int savedPos = currentPos;
        Token nextToken = getNextToken();
        currentPos = savedPos;
        return nextToken;
    }

    public int getCurrentPos() {
        return currentPos;
    }
}
