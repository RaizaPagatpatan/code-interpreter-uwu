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

    public ProgramNode parse() throws ParseException {
        lexer.consume(Token.Type.BEGIN_CODE, "BEGIN CODE");

        List<VariableNode> variableDeclarations = new ArrayList<>();
        List<StatementNode> statements = new ArrayList<>();

        while (lexer.peek().getType() != Token.Type.END_CODE) {
            if (lexer.peek().getType() == Token.Type.INTEGER_LITERAL && lexer.peek().getValue().equals("INT")) {
                variableDeclarations.addAll(parseVariableDeclarations("INT"));
            } else if (lexer.peek().getType() == Token.Type.CHAR_LITERAL && lexer.peek().getValue().equals("CHAR")) {
                variableDeclarations.addAll(parseVariableDeclarations("CHAR"));
            } else if (lexer.peek().getType() == Token.Type.BOOLEAN_LITERAL && lexer.peek().getValue().equals("BOOL")) {
                variableDeclarations.addAll(parseVariableDeclarations("BOOL"));
            } else if (lexer.peek().getType() == Token.Type.FLOAT_LITERAL && lexer.peek().getValue().equals("FLOAT")) {
                variableDeclarations.addAll(parseVariableDeclarations("FLOAT"));
            }else {
                statements.add(parseStatement());
            }
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
            ExpressionNode expression = null;
            if (lexer.peek().getType() == Token.Type.EQUAL) {
                lexer.consume(Token.Type.EQUAL, "==");
                expression = parseExpression();
                System.out.println("EXPRESSION " + expression);
                declarations.add(new VariableNode(identifier.getValue(), dataType, expression));
            } else {
                declarations.add(new VariableNode(identifier.getValue(), dataType, expression));
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
        ExpressionNode expression = parseExpression();
        lexer.consume(Token.Type.SEMICOLON, ";");
        return new AssignmentNode(identifier.getValue(), expression);
    }

    private DisplayNode parseDisplayStatement() throws ParseException {
        lexer.consume(Token.Type.DISPLAY, "DISPLAY");
        List<ExpressionNode> expressions = new ArrayList<>();

        do {
            ExpressionNode expression = parseExpression();
            expressions.add(expression);
        } while (lexer.peek().getType() == Token.Type.CONCATENATE);

        lexer.consume(Token.Type.SEMICOLON, ";");
        return new DisplayNode(expressions);
    }


    private ExpressionNode parseExpression() throws ParseException {
        Token currentToken = lexer.peek();
        switch (currentToken.getType()) {
            case INTEGER_LITERAL:
            case CHAR_LITERAL:
            case STRING_LITERAL:
            case IDENTIFIER:
            case FLOAT_LITERAL:
                return parseLiteralExpression();
            case KEYWORD:
                return parseKeywordExpression();
            default:
                throw new ParseException("Unexpected token: " + currentToken.getValue(), lexer.peek().getPosition());
        }
    }

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
