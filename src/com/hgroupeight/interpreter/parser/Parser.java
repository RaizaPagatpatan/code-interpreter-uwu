package com.hgroupeight.interpreter.parser;

import com.hgroupeight.interpreter.ast.*;
import com.hgroupeight.interpreter.lexer.Lexer;
import com.hgroupeight.interpreter.lexer.Token;
import com.hgroupeight.interpreter.ast.BinaryExpressionNode.BinaryOperator;

import javax.xml.crypto.Data;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final Lexer lexer;
    private int currentLine = 1;

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
            } else {
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
        System.out.print(lexer.peek().getType() + " huh?");
        if (lexer.peek().getType() == Token.Type.DISPLAY) {
            return parseDisplayStatement();
        } else {
            return parseAssignmentStatement();
        }
    }

    private AssignmentNode parseAssignmentStatement() throws ParseException {
        System.out.println("Did i get here????");
        Token identifier = lexer.getNextToken();
        ExpressionNode expression;
        lexer.consume(Token.Type.ASSIGN, "=");
        expression = parseExpression();
        System.out.println(("???????"));
        if (lexer.peek().getType() == Token.Type.PLUS || lexer.peek().getType() == Token.Type.MINUS || lexer.peek().getType() == Token.Type.MULTIPLY || lexer.peek().getType() == Token.Type.DIVIDE){
            lexer.consume(lexer.peek().getType(), lexer.peek().getValue());
            System.out.println("Did i get here?");
            expression = parseArithmeticExpression();
            System.out.println(("1 "));
        }
        lexer.consume(Token.Type.SEMICOLON, ";");
        return new AssignmentNode(identifier.getValue(), expression);
    }

    private ExpressionNode parseArithmeticExpression() throws ParseException {
        System.out.println("Did i get here?");
        ExpressionNode left = term(); // Parse the left operand
        while (lexer.peek().getType() == Token.Type.PLUS || lexer.peek().getType() == Token.Type.MINUS) {
            Token operator = lexer.getNextToken(); // Get the operator token
            ExpressionNode right = term(); // Parse the right operand
            BinaryOperator op;
            if (operator.getType() == Token.Type.PLUS) {
                op = BinaryOperator.PLUS;
            } else {
                op = BinaryOperator.MINUS;
            }
            left = new BinaryExpressionNode(left, right, op); // Create the binary expression node
        }
        return left;
    }
    private DisplayNode parseDisplayStatement() throws ParseException {
        int displayLine = currentLine; // for catching line errors only
        lexer.consume(Token.Type.DISPLAY, "DISPLAY");
//        lexer.consume(Token.Type.COLON, ":");
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

    private ExpressionNode term() throws ParseException {
        ExpressionNode expr = factor();
        System.out.println("Did i get here too?");
        System.out.println("Wtf are u returning" + expr.getClass().getName());
        while (lexer.peek().getType() == Token.Type.PLUS || lexer.peek().getType() == Token.Type.MINUS) {
            BinaryOperator op = null;
            if (lexer.peek().getType() == Token.Type.PLUS) {
                op = BinaryOperator.PLUS;
                lexer.consume(Token.Type.PLUS, "+"); // Consume the PLUS token
                System.out.println("Should be here, right...");
            } else if (lexer.peek().getType() == Token.Type.MINUS) {
                op = BinaryOperator.MINUS;
                lexer.consume(Token.Type.MINUS, "-"); // Consume the MINUS token
            }

            System.out.println("Parsed expression: " + expr); // Print each parsed expression
            ExpressionNode right = factor();

            expr = new BinaryExpressionNode(expr, right, op);

        }

        return expr;
    }

        private ExpressionNode factor()  throws ParseException {
            System.out.println("Did i get here here here?");
            ExpressionNode expr =  parseExpression();

            while (lexer.peek().getType() == Token.Type.DIVIDE || lexer.peek().getType() == Token.Type.MULTIPLY) {
                BinaryOperator op = null;
                if (lexer.peek().getType() == Token.Type.DIVIDE) {
                    op = BinaryOperator.DIVIDE;
                    lexer.consume(Token.Type.DIVIDE, "/"); // Consume the PLUS token
                } else if (lexer.peek().getType() == Token.Type.MULTIPLY) {
                    op = BinaryOperator.MULTIPLY;
                    lexer.consume(Token.Type.MULTIPLY, "*"); // Consume the PLUS token
                }

                System.out.println("Parsed expression: " + expr); // Print each parsed expression
                ExpressionNode right = factor();

                expr = new BinaryExpressionNode(expr, right, op);

            }
            return expr;
        }


        }

