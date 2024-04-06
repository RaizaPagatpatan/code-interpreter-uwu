package com.hgroupeight.interpreter.parser;

import com.hgroupeight.interpreter.ast.*;
import com.hgroupeight.interpreter.lexer.Lexer;
import com.hgroupeight.interpreter.lexer.Token;

import javax.xml.crypto.Data;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }
    List<StatementNode> statements = new ArrayList<>();
    List<VariableNode> variableDeclarations = new ArrayList<>();
    public ProgramNode parse() throws ParseException {
        lexer.consume(Token.Type.BEGIN_CODE, "BEGIN CODE");



        while (lexer.peek().getType() != Token.Type.END_CODE) {
            Token peekedToken = lexer.peek();
            if (peekedToken.getType() == Token.Type.INTEGER_LITERAL && peekedToken.getValue().equals("INT")) {
                variableDeclarations.addAll(parseVariableDeclarations("INT"));
            } else if (peekedToken.getType() == Token.Type.CHAR_LITERAL && peekedToken.getValue().equals("CHAR")) {
                variableDeclarations.addAll(parseVariableDeclarations("CHAR"));
            } else if (peekedToken.getType() == Token.Type.BOOLEAN_LITERAL && peekedToken.getValue().equals("BOOL")) {
                variableDeclarations.addAll(parseVariableDeclarations("BOOL"));
            } else if (peekedToken.getType() == Token.Type.FLOAT_LITERAL && peekedToken.getValue().equals("FLOAT")) {
                variableDeclarations.addAll(parseVariableDeclarations("FLOAT"));
            } else {
                statements.add(parseStatement());
            }
            lexer.line++;
        }
       // Print the variables
        lexer.consume(Token.Type.END_CODE, "END CODE");

        return new ProgramNode(variableDeclarations, statements);
    }

    private List<VariableNode> parseVariableDeclarations(String type) throws ParseException {
        List<VariableNode> declarations = new ArrayList<>();
        DataType dataType = null;

        switch (type) {
            case "INT":
                lexer.consume(Token.Type.INTEGER_LITERAL, "INT");
                dataType = DataType.INTEGER;
                break;
            case "CHAR":
                lexer.consume(Token.Type.CHAR_LITERAL, "CHAR");
                dataType = DataType.CHAR;
                break;
            case "BOOL":
                lexer.consume(Token.Type.BOOLEAN_LITERAL, "BOOL");
                dataType = DataType.BOOLEAN;
                break;
            case "FLOAT":
                lexer.consume(Token.Type.FLOAT_LITERAL, "FLOAT");
                dataType = DataType.FLOAT;
                break;
        }

        do {
            Token identifier = lexer.getNextToken();

            // Check if there's an assignment
            if (lexer.peek().getType() == Token.Type.ASSIGN) {
                lexer.consume(Token.Type.ASSIGN, "=");
                Object value = lexer.peek().getValue();
                System.out.println("TOKEN " + lexer.peek().getValue());
                ExpressionNode expression = parseExpression();

                declarations.add(new VariableNode(identifier.getValue(), dataType, expression, value));
            } else {
                // No assignment, add the variable declaration without expression
                declarations.add(new VariableNode(identifier.getValue(), dataType, null));
            }

            // Check for comma to continue with more declarations
            if (lexer.peek().getType() == Token.Type.COMMA) {
                lexer.consume(Token.Type.COMMA, ",");
            } else {
                break;
            }
        } while (true);

        lexer.consume(Token.Type.SEMICOLON, ";");
        return declarations;
    }

    private List<StatementNode> parseStatements() throws ParseException {
        List<StatementNode> statements = new ArrayList<>();
        while (lexer.peek().getType() != Token.Type.END_CODE) {
            statements.add(parseStatement());
        }
        return statements;
    }

    private StatementNode parseStatement() throws ParseException {
        if (lexer.peek().getType() == Token.Type.DISPLAY) {
            return parseDisplayStatement();
        } else {
            return parseAssignmentStatement();
        }
    }

    private AssignmentNode parseAssignmentStatement() throws ParseException {
        Token identifier = lexer.getNextToken();

        lexer.consume(Token.Type.ASSIGN, "=");
        System.out.println("LEXER PEEK " + lexer.peek().getValue());
        System.out.println("LEXER PEEK " + lexer.peek().getType());
        if (lexer.peek().getType() == Token.Type.IDENTIFIER) {
            boolean found = false;
            for (VariableNode varNode : variableDeclarations) {

                System.out.println("VAR NAME " + varNode.getName() + "LEXER VALUE " + lexer.peek().getValue());
                if (varNode.getName().equals(lexer.peek().getValue())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new ParseException("LINE: " + ++lexer.line + " Undeclared variable " + lexer.peek().getValue(), lexer.line);
            }
        }
        // If identifier not found, throw ParseException

        ExpressionNode expression = parseExpression();
        lexer.consume(Token.Type.SEMICOLON, ";");
        return new AssignmentNode(identifier.getValue(), expression);
    }

    private DisplayNode parseDisplayStatement() throws ParseException {
        lexer.consume(Token.Type.DISPLAY, "DISPLAY");
        List<ExpressionNode> expressions = new ArrayList<>();

        ExpressionNode expression = parseExpression();
        expressions.add(expression);
        System.out.println("Parsed expression: " + expression); // Print the first parsed expression

        // Loop to handle concatenation
        while (lexer.peek().getType() == Token.Type.CONCATENATE) {
            lexer.consume(Token.Type.CONCATENATE, "&"); // Consume the CONCATENATE token
            expression = parseExpression();
            expressions.add(expression);
            System.out.println("Parsed expression: " + expression); // Print each parsed expression
        } while (lexer.peek().getType() == Token.Type.CONCATENATE);

        lexer.consume(Token.Type.SEMICOLON, ";");
        return new DisplayNode(expressions);
    }


    private ExpressionNode parseExpression() throws ParseException {
        Token currentToken = lexer.peek();
        System.out.println("CHECK " + currentToken.getType());
        System.out.println("IDENTIFIER DISPLAY " + currentToken);
        switch (currentToken.getType()) {
            case INTEGER_LITERAL:
            case CHAR_LITERAL:
            case STRING_LITERAL:
            case IDENTIFIER:
            case FLOAT_LITERAL:
                return parseLiteralExpression();
            case KEYWORD:
                return parseKeywordExpression();
//            case CONCATENATE:
//                return parseConcatExpression();
            default:
                throw new ParseException("Unexpected token: " + currentToken.getValue(), lexer.peek().getPosition());
        }
    }

//    private ExpressionNode parseConcatExpression() throws ParseException {
//        List<ExpressionNode> expressions = new ArrayList<>();
//        int count = 0;
////        do {
////            count++;
////            expressions.add(parseExpression());
////            if (lexer.peek().getType() == Token.Type.CONCATENATE) {
////                lexer.getNextToken();
////            } else {
////                break;
////            }
////        } while (count <= 5);
//
//        return buildConcatenation(expressions);
//    }
//
//    private ExpressionNode buildConcatenation(List<ExpressionNode> expressions) {
//        // Assuming a simple left-associative concatenation
//        if (expressions.size() == 1) {
//            return expressions.get(0);
//        } else {
//            ExpressionNode result = expressions.get(0);
//            for (int i = 1; i < expressions.size(); i++) {
//                result = new BinaryExpressionNode(result, expressions.get(i), BinaryExpressionNode.BinaryOperator.CONCATENATE);
//            }
//            return result;
//        }
//    }
    private LiteralNode parseLiteralExpression() throws ParseException {
        Token token = lexer.getNextToken();
        ExpressionNode.ExpressionType expressionType;
        switch (token.getType()) {
            case INTEGER_LITERAL:
                expressionType = ExpressionNode.ExpressionType.INTEGER;
                break;
            case IDENTIFIER:
                expressionType = ExpressionNode.ExpressionType.IDENTIFIER;
                break;
            case CHAR_LITERAL:
                expressionType = ExpressionNode.ExpressionType.CHARACTER;
                break;
            case STRING_LITERAL:
                expressionType = ExpressionNode.ExpressionType.STRING;
                break;
            case FLOAT_LITERAL:
                expressionType = ExpressionNode.ExpressionType.FLOAT;
                break;
            default:
                throw new ParseException("Invalid literal expression", lexer.getCurrentPos());
        }
        return new LiteralNode(expressionType, token.getValue());
    }

    private ExpressionNode parseKeywordExpression() throws ParseException {
        Token token = lexer.getNextToken();
        switch (token.getValue()) {
            case "TRUE":
            case "FALSE":
                return new LiteralNode(ExpressionNode.ExpressionType.BOOLEAN, token.getValue());
            default:
                throw new ParseException("Invalid keyword expression: " + token.getValue(), lexer.peek().getPosition());
        }
    }
}
