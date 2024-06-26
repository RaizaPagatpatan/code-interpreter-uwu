    package com.hgroupeight.interpreter.parser;

import com.hgroupeight.interpreter.ast.*;
import com.hgroupeight.interpreter.lexer.Lexer;
import com.hgroupeight.interpreter.lexer.Token;
import com.hgroupeight.interpreter.symboltable.SymbolTable;
import com.sun.tools.jconsole.JConsoleContext;
    import com.hgroupeight.interpreter.ast.*;
    import com.hgroupeight.interpreter.lexer.Lexer;
    import com.hgroupeight.interpreter.lexer.Token;
    import com.sun.tools.jconsole.JConsoleContext;

    import javax.xml.crypto.Data;
    import java.text.ParseException;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Scanner;

    public class Parser {
        private final Lexer lexer;
        private int currentLine = 1;

        public Parser(Lexer lexer) {
            this.lexer = lexer;
        }
        List<StatementNode> statements = new ArrayList<>();
        List<VariableNode> variableDeclarations = new ArrayList<>();
        public ProgramNode parse() throws ParseException {
            lexer.consume(Token.Type.BEGIN_CODE, "BEGIN CODE");


            while (lexer.peek().getType() != Token.Type.END_CODE) {
                if (lexer.peek().getType() == Token.Type.INTEGER_LITERAL && lexer.peek().getValue().equals("INT")) {
                    variableDeclarations.addAll(parseVariableDeclarations("INT"));
                } else if (lexer.peek().getType() == Token.Type.CHAR_LITERAL && lexer.peek().getValue().equals("CHAR")) {
                    variableDeclarations.addAll(parseVariableDeclarations("CHAR"));
                } else if (lexer.peek().getType() == Token.Type.BOOLEAN_LITERAL && lexer.peek().getValue().equals("BOOL")) {
                    variableDeclarations.addAll(parseVariableDeclarations("BOOL"));
                } else if (lexer.peek().getType() == Token.Type.FLOAT_LITERAL && lexer.peek().getValue().equals("FLOAT")) {
                    variableDeclarations.addAll(parseVariableDeclarations("FLOAT"));
                } else if (lexer.peek().getType() == Token.Type.SCAN) {
                    parseScanStatement();
                } else if (lexer.peek().getType() == Token.Type.BEGIN_CODE) {
                    throw new ParseException("There must only be one BEGIN CODE and END CODE clause.", currentLine);
                } else {
                    statements.add(parseStatement());
                }
            }
           // Print the variables
            lexer.consume(Token.Type.END_CODE, "END CODE");

            if (lexer.peek().getType() == Token.Type.BEGIN_CODE || lexer.peek().getType() == Token.Type.END_CODE) {
                throw new ParseException("There must only be one BEGIN CODE and END CODE clause.", currentLine);
            }

            return new ProgramNode(variableDeclarations, statements);
        }

        private List<VariableNode> parseVariableDeclarations(String type) throws ParseException {
            List<VariableNode> declarations = new ArrayList<>();
            DataType dataType = null;

            switch (type) {
                case "INTEGER":
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
                default:
                    throw new ParseException("Invalid data type: " + dataType, lexer.getCurrentPos());
            }


            do {
                Token identifier = lexer.getNextToken();
                // Check if there's an assignment
                if (lexer.peek().getType() == Token.Type.ASSIGN) {
                    lexer.consume(Token.Type.ASSIGN, "=");
                    Object value = lexer.peek().getValue();

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

    //        lexer.consume(Token.Type.LINE_BREAK, "\n");
            return declarations;
        }

        private void parseScanStatement() throws ParseException {
            System.out.println("PARSE SCAN");
            lexer.consume(Token.Type.SCAN, "SCAN");
            lexer.consume(Token.Type.COLON, ":");

            // Scan the whole line and separate by comma
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            String[] inputs = input.split(",");
            int counter = 0;

            do {
                Token identifierToken = lexer.getNextToken();
                System.out.println("WHAT TOKEN " + identifierToken);
                String identifier = identifierToken.getValue();
                System.out.println("IDENTIFIER " + identifier);

                // Find the variable declaration with the given identifier
                VariableNode variableNode = null;

                for (VariableNode variable : variableDeclarations) {
                    if (variable.getName().equals(identifier)) {
                        variableNode = variable;
                        break;
                    }
                }

                if (variableNode != null) {
                    DataType dataType = variableNode.getType();
                    Object value;
                    // Parse input according to data type
                    switch (dataType) {
                        case INTEGER:
                            value = Integer.parseInt(inputs[counter]);
                            break;
                        case CHAR:
                            if (inputs[counter].length() != 1) {
                                throw new ParseException("Invalid input for CHAR type.", lexer.getCurrentPos());
                            }
                            value = inputs[counter];
                            break;
                        case BOOLEAN:
                            value = Boolean.parseBoolean(inputs[counter]);
                            break;
                        case FLOAT:
                            value = Float.parseFloat(inputs[counter]);
                            break;
                        default:
                            throw new ParseException("Unsupported data type.", lexer.getCurrentPos());
                    }

                    // Update the value of the VariableNode
                    variableNode.setValue(value);

                    // Update counter for the input list
                    counter++;

                    // Check for comma to continue with more values
                    if (lexer.peek().getType() == Token.Type.COMMA) {
                        lexer.consume(Token.Type.COMMA, ",");
                    } else {
                        break;
                    }
                } else {
                    throw new ParseException("Variable " + identifier + " not declared.", lexer.getCurrentPos());
                }
            } while (true);

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
            }
    //        else {
    //            return parseAssignmentStatement();
    //        }
            parseAssignmentStatement();
            System.out.println("STATEMENTS " + statements);
            return null;
        }

    private void parseAssignmentStatement() throws ParseException {
        ArrayList<String> varNames = new ArrayList<String>();
        int leftvalue = 0; // Initialize to default value
        int rightvalue = 0; // Initialize to default value
        boolean isLeftInt = false;
        boolean isRightInt = false;
//        SymbolTable symb = new SymbolTable();
        Token identifier = lexer.getNextToken();
//        System.out.println("IDENTIFIER TOKEN " + identifier);
        //adds first variable to varNames
//        symb.addVariable(identifier.getValue(), );
        if (identifier.getType() == Token.Type.IDENTIFIER) {
            System.out.println("WENT IN HERE" + identifier.getType() + " " + identifier.getValue());
            varNames.add(identifier.getValue());
        }

        lexer.consume(Token.Type.ASSIGN, "=");

    //        System.out.println("LEXER PEEK " + lexer.peek().getValue());
    //        System.out.println("LEXER PEEK " + lexer.peek().getType());


        while (lexer.peek().getType() == Token.Type.IDENTIFIER || lexer.peek().getType() == Token.Type.INTEGER_LITERAL) {



            boolean found = false;
            Token newToken = lexer.getNextToken();
//            System.out.println("NEW TOKEN " + newToken);
            if (newToken.getType() != Token.Type.IDENTIFIER && newToken.getType() != Token.Type.INTEGER_LITERAL ) {
                System.out.println("NEW TYPE " + newToken.getType());
                System.out.println("BREAK");
                break;
            }

            if (newToken.getType() == Token.Type.INTEGER_LITERAL) {
                System.out.println("ITS AN INT LEFT _____");
                isLeftInt = true;
                leftvalue = Integer.parseInt((String) newToken.getValue());
//                    lexer.getNextToken();
//                lexer.consume(Token.Type.INTEGER_LITERAL, lexer.peek().getValue());
            }
            //find if future var is Defined
            for (VariableNode varNode : variableDeclarations) {
//                System.out.println("VAR NAME " + varNode.getName() + " LEXER VALUE " + newToken.getValue());
                if (varNode.getName().equals(newToken.getValue())) {
                    System.out.println("VAR NODEEE " + varNode.getName());
                    varNames.add(varNode.getName());
                    found = true;
                    break;
                }
            }


//            System.out.println("LEXER TOKEN " + lexer.peek().getType());
//            else
            if (Token.Type.PLUS == lexer.peek().getType() || Token.Type.MINUS == lexer.peek().getType() || Token.Type.MULTIPLY == lexer.peek().getType() || Token.Type.DIVIDE == lexer.peek().getType())
            {
                String binOperation = "";
                if (Token.Type.PLUS == lexer.peek().getType()) {
                    binOperation = "PLUS";
                    lexer.consume(Token.Type.PLUS, "+");
                }
                else if (Token.Type.MINUS == lexer.peek().getType()) {
                    binOperation = "MINUS";
                    lexer.consume(Token.Type.MINUS, "-");
                }
                else if (Token.Type.MULTIPLY == lexer.peek().getType()) {
                    binOperation = "MULTIPLY";
                    lexer.consume(Token.Type.MULTIPLY, "*");
                }
                else if (Token.Type.DIVIDE == lexer.peek().getType()) {
                    binOperation = "DIVIDE";
                    lexer.consume(Token.Type.DIVIDE, "/");
                }

                System.out.println("ASDASD");
                System.out.println("LAST " + varNames.get(varNames.size()-1));

                if (lexer.peek().getType() != Token.Type.IDENTIFIER && lexer.peek().getType() != Token.Type.INTEGER_LITERAL) {
                    System.out.println("ERROR NEEDS IDENTIFIER BUT FOUND " + lexer.peek().getType());
                    return;
                }

                if ( lexer.peek().getType() == Token.Type.INTEGER_LITERAL) {
                    System.out.println("ITS AN INT RIGHTTTTTTTT");
                    rightvalue = Integer.parseInt((String) lexer.peek().getValue());
//                    lexer.getNextToken();
                    lexer.consume(Token.Type.INTEGER_LITERAL, lexer.peek().getValue());
                }
                // ASSIGN THE LEFT AND RIGHT VALUES
                for (VariableNode var : variableDeclarations) {
                    if (var.getName().equals(varNames.get(varNames.size()-1)) && !isLeftInt) {
                        leftvalue = Integer.parseInt((String) var.getValue());
                        System.out.println("LEFT VALUE " + leftvalue);
                    }

                    if (var.getName().equals(lexer.peek().getValue())) {
                        System.out.println("THE INT " + Integer.parseInt((String) var.getValue()));
                        rightvalue = Integer.parseInt((String) var.getValue());
                    }
                }

                for (VariableNode var : variableDeclarations) {
                    if (var.getName().equals(varNames.get(0))) {
                        System.out.println("SAW ASSIGN - - - - -- - - - - - - - " + var.getName());
                        System.out.println("LEFT VALUE = " + leftvalue + " RIGHT VALUE " + rightvalue);
                        if (binOperation.equals("PLUS"))
                            var.setValue(leftvalue + rightvalue); // No need to cast here, as they're already int
                        else if (binOperation.equals("MINUS"))
                            var.setValue(leftvalue - rightvalue); // No need to cast here, as they're already int
                        else if (binOperation.equals("MULTIPLY"))
                            var.setValue(leftvalue * rightvalue); // No need to cast here, as they're already int
                        else if (binOperation.equals("DIVIDE"))
                            var.setValue(leftvalue / rightvalue); // No need to cast here, as they're already int
                        System.out.println("VARR " + var.getValue());

                    }
                }

                System.out.println("IDENTIFIER CHECK " + lexer.peek().getType() + " VAR NAME " + lexer.peek().getValue() );
                for (String varName : varNames) {
                    System.out.println("VAR NAME " + varName);
                }
            }
            else if (Token.Type.DISPLAY == lexer.peek().getType()) {
                System.out.println("VAR NAMES " + varNames.get(0));
//                parseDisplayStatement();
                return;
            }
            else
                lexer.consume(Token.Type.ASSIGN, "=");

            if (!found && newToken.getType() != Token.Type.INTEGER_LITERAL) {
                throw new ParseException("LINE: " + currentLine + " Undeclared variable " + newToken.getValue(), currentLine);
            }
        }
        // If identifier not found, throw ParseException
        System.out.println("GET CURRENT VALUE " + lexer.peek());
        if (lexer.peek().getType() == Token.Type.DISPLAY) return;
        Object currentVal = lexer.peek().getValue();
        ExpressionNode expression = parseExpression();
        System.out.println("EXPRESSION NODE " + expression);
        for (String varName : varNames) {
            for (VariableNode varNode : variableDeclarations) {

    //                System.out.println("VAR NAME " + varNode.getName() + " LEXER VALUE " + newToken.getValue());
                    if (varNode.getName().equals(varName)) {
                        System.out.println("VAR NAME " + varNode.getName() + " VAR VALUE " + varName + "CURRENT VAL " + currentVal);
                        varNode.setValue(currentVal);
                        break;
                    }
                }
            }
    //        lexer.consume(Token.Type.LINE_BREAK, "\n");
            return;
    //        return new AssignmentNode(identifier.getValue(), expression);
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

    //        lexer.consume(Token.Type.LINE_BREAK, "\n");
            return new DisplayNode(expressions);
        }

    //    private ExpressionNode parseExpression() throws ParseException {
    //        Token currentToken = lexer.peek();
    //        System.out.println("CHECK " + currentToken.getType());
    //        System.out.println("IDENTIFIER DISPLAY " + currentToken);
    //        switch (currentToken.getType()) {
    //            case INTEGER_LITERAL:
    //            case CHAR_LITERAL:
    //            case STRING_LITERAL:
    //            case IDENTIFIER:
    //            case FLOAT_LITERAL:
    //                return parseLiteralExpression();
    //            case KEYWORD:
    //                return parseKeywordExpression();
    ////            case CONCATENATE:
    ////                return parseConcatExpression();
    //            default:
    //                throw new ParseException("Unexpected token: " + currentToken.getValue(), lexer.peek().getPosition());
    //        }
    //    }

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
                case BOOLEAN_LITERAL:
                    expressionType = ExpressionNode.ExpressionType.BOOLEAN;
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

        ExpressionNode.ExpressionType getExpressionType(DataType dataType) {
            switch (dataType) {
                case INTEGER:
                    return ExpressionNode.ExpressionType.INTEGER;
                case CHAR:
                    return ExpressionNode.ExpressionType.CHARACTER;
                case BOOLEAN:
                    return ExpressionNode.ExpressionType.BOOLEAN;
                case FLOAT:
                    return ExpressionNode.ExpressionType.FLOAT;
                default:
                    throw new IllegalArgumentException("Unsupported data type: " + dataType);
            }
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


        /// Parser for expression arithmetics
    private ExpressionNode parseExpression() throws ParseException {
        Token currentToken = lexer.peek();
//        System.out.println("CHECK " + currentToken.getType());
//        System.out.println("IDENTIFIER DISPLAY " + currentToken);
        switch (currentToken.getType()) {
            case INTEGER_LITERAL:
            case CHAR_LITERAL:
            case BOOLEAN_LITERAL:
            case STRING_LITERAL:
            case IDENTIFIER:
            case FLOAT_LITERAL:
                return parseLiteralExpression();
            case KEYWORD:
                return parseKeywordExpression();
            case PLUS:
            case MINUS:
            case MULTIPLY:
            case DIVIDE:
            case LEFT_PAREN:
                return parseArithmeticExpression(); // arithmetics parsing call
            default:
                throw new ParseException("Unexpected token: " + currentToken.getValue(), lexer.peek().getPosition());
        }
    }

        private ExpressionNode parseArithmeticExpression() throws ParseException {
            // start on left
            ExpressionNode leftOperand = parseTerm();

            // parse optional operands a.k.a the lower order
            while (lexer.peek().getType() == Token.Type.PLUS || lexer.peek().getType() == Token.Type.MINUS) {
                Token operatorToken = lexer.getNextToken();
                ExpressionNode rightOperand = parseTerm();
                // Create a BinaryExpressionNode for the arithmetic operation
                BinaryExpressionNode.BinaryOperator operator = operatorToken.getType() == Token.Type.PLUS ?
                        BinaryExpressionNode.BinaryOperator.ADDITION : BinaryExpressionNode.BinaryOperator.SUBTRACTION;
                leftOperand = new BinaryExpressionNode(leftOperand, rightOperand, operator);
            }
            return leftOperand;
        }

        // term x term , term, term / term
        private ExpressionNode parseTerm() throws ParseException {
            // parse left operand, check left first, check right , if optional, suspend.
            ExpressionNode leftOperand = parseFactor();

            // parse term x term, term / term
            while (lexer.peek().getType() == Token.Type.MULTIPLY || lexer.peek().getType() == Token.Type.DIVIDE) {
                Token operatorToken = lexer.getNextToken();
                ExpressionNode rightOperand = parseFactor();
                // binary node get
                BinaryExpressionNode.BinaryOperator operator = operatorToken.getType() == Token.Type.MULTIPLY ?
                        BinaryExpressionNode.BinaryOperator.MULTIPLICATION : BinaryExpressionNode.BinaryOperator.DIVISION;
                leftOperand = new BinaryExpressionNode(leftOperand, rightOperand, operator);
            }
            return leftOperand;
        }

        private ExpressionNode parseFactor() throws ParseException {
            Token currentToken = lexer.peek();
            if (currentToken.getType() == Token.Type.INTEGER_LITERAL ||
                    currentToken.getType() == Token.Type.FLOAT_LITERAL ||
                    currentToken.getType() == Token.Type.IDENTIFIER) {
                lexer.getNextToken(); // Consume the token
                // literal node Integer get
                return new LiteralNode(ExpressionNode.ExpressionType.INTEGER, currentToken.getValue());
            } else if (currentToken.getType() == Token.Type.LEFT_PAREN) {
                lexer.consume(Token.Type.LEFT_PAREN, "(");
                ExpressionNode expression = parseArithmeticExpression();
                lexer.consume(Token.Type.RIGHT_PAREN, ")");
                return expression;
            } else {
                throw new ParseException("Unexpected token: " + currentToken.getValue(), lexer.peek().getPosition());
            }
        }

    }
