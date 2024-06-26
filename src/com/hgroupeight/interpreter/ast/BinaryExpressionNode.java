//package com.hgroupeight.interpreter.ast;
//
//import com.hgroupeight.interpreter.symboltable.SymbolTable;
//
//public class BinaryExpressionNode extends ExpressionNode {
//    public ExpressionNode left, right;
//    public BinaryOperator operator;
//    public BinaryExpressionNode(ExpressionNode left, ExpressionNode right, BinaryOperator operator) {
//        super(ExpressionType.BINARY);
//        this.left = left;
//        this.right = right;
//        this.operator = operator;
//    }
//
//
//    public enum BinaryOperator {
//        ADDITION("+"),
//        SUBTRACTION("-"),
//        MULTIPLICATION("*"),
//        DIVISION("/"),
//        EQUAL("=="),
//        NOT_EQUAL("<>"),
//        LESS_THAN("<"),
//        LESS_THAN_EQUAL("<="),
//        GREATER_THAN(">"),
//        GREATER_THAN_EQUAL(">="),
//        LOGICAL_AND("AND"),
//        LOGICAL_OR("OR"),
//        CONCATENATE("&");
//
//
//        private final String symbol;
//
//        BinaryOperator(String symbol) {
//            this.symbol = symbol;
//        }
//
//        public String getSymbol() {
//            return symbol;
//        }
//    }
//
//    public BinaryExpressionNode(ExpressionType expressionType) {
//        super(expressionType);
//    }
//
//    @Override
//    public Object evaluate(SymbolTable symbolTable) throws Exception {
//        Object leftValue = left.evaluate(symbolTable);
//        Object rightValue = right.evaluate(symbolTable);
//
//        if (operator.equals(BinaryOperator.CONCATENATE)) {
//            return leftValue.toString() + rightValue.toString();
//        }
//        if (leftValue instanceof Integer && rightValue instanceof Integer) {
//            int leftInt = (Integer) leftValue;
//            int rightInt = (Integer) rightValue;
//            switch (operator) {
//                case ADDITION:
//                    return leftInt + rightInt;
//                case SUBTRACTION:
//                    return leftInt - rightInt;
//                case MULTIPLICATION:
//                    return leftInt * rightInt;
//                case DIVISION:
//                    if (rightInt == 0) {
//                        throw new Exception("Division by zero");
//                    }
//                    return leftInt / rightInt;
//                case EQUAL:
//                    return leftInt == rightInt;
//                case NOT_EQUAL:
//                    return leftInt != rightInt;
//                case LESS_THAN:
//                    return leftInt < rightInt;
//                case LESS_THAN_EQUAL:
//                    return leftInt <= rightInt;
//                case GREATER_THAN:
//                    return leftInt > rightInt;
//                case GREATER_THAN_EQUAL:
//                    return leftInt >= rightInt;
//                default:
//                    throw new Exception("Unsupported binary operator: " + operator);
//            }
//        } else if (leftValue instanceof Boolean && rightValue instanceof Boolean) {
//            boolean leftBool = (Boolean) leftValue;
//            boolean rightBool = (Boolean) rightValue;
//            switch (operator) {
//                case LOGICAL_AND:
//                    return leftBool && rightBool;
//                case LOGICAL_OR:
//                    return leftBool || rightBool;
//                default:
//                    throw new Exception("Unsupported binary operator: " + operator);
//            }
//        } else {
//            throw new Exception("Operands for binary expressions must be of the same type");
//        }
//    }
//}


// BinaryExpressionNode.java
package com.hgroupeight.interpreter.ast;

import com.hgroupeight.interpreter.symboltable.SymbolTable;

public class BinaryExpressionNode extends ExpressionNode {
    public ExpressionNode left, right;
    public BinaryOperator operator;

    public BinaryExpressionNode(ExpressionNode left, ExpressionNode right, BinaryOperator operator) {
        super(ExpressionType.BINARY);
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public enum BinaryOperator {
        ADDITION("+"),
        SUBTRACTION("-"),
        MULTIPLICATION("*"),
        DIVISION("/"),
        EQUAL("=="),
        NOT_EQUAL("<>"),
        LESS_THAN("<"),
        LESS_THAN_EQUAL("<="),
        GREATER_THAN(">"),
        GREATER_THAN_EQUAL(">="),
        LOGICAL_AND("AND"),
        LOGICAL_OR("OR"),
        CONCATENATE("&"); // Add CONCATENATE for string concatenation

        private final String symbol;

        BinaryOperator(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }

    // Update the evaluate method to handle arithmetic operations
    @Override
    public Object evaluate(SymbolTable symbolTable) throws Exception {
        Object leftValue = left.evaluate(symbolTable);
        Object rightValue = right.evaluate(symbolTable);

        // Handle arithmetic operations based on the operator
        if (operator == BinaryOperator.ADDITION) {
            // Perform addition
            return (Integer) leftValue + (Integer) rightValue;
        } else if (operator == BinaryOperator.SUBTRACTION) {
            // Perform subtraction
            return (Integer) leftValue - (Integer) rightValue;
        } else if (operator == BinaryOperator.MULTIPLICATION) {
            // Perform multiplication
            return (Integer) leftValue * (Integer) rightValue;
        } else if (operator == BinaryOperator.DIVISION) {
            // Perform division
            if ((Integer) rightValue == 0) {
                throw new ArithmeticException("Division by zero");
            }
            return (Integer) leftValue / (Integer) rightValue;
        } else {
            throw new UnsupportedOperationException("Unsupported binary operator: " + operator);
        }
    }

//    @Override
//    public Object evaluate(SymbolTable symbolTable) throws Exception {
//        Object leftValue = left.evaluate(symbolTable);
//        Object rightValue = right.evaluate(symbolTable);
//
//        // Handle Concat
//        if (operator.equals(BinaryOperator.CONCATENATE)) {
//            return leftValue.toString() + rightValue.toString();
//        }
//
//        // arithmetic
//        if (leftValue instanceof Integer && rightValue instanceof Integer) {
//            int leftInt = (Integer) leftValue;
//            int rightInt = (Integer) rightValue;
//            switch (operator) {
//                case ADDITION:
//                    return leftInt + rightInt;
//                case SUBTRACTION:
//                    return leftInt - rightInt;
//                case MULTIPLICATION:
//                    return leftInt * rightInt;
//                case DIVISION:
//                    if (rightInt == 0) {
//                        throw new Exception("Division by zero");
//                    }
//                    return leftInt / rightInt;
//                case EQUAL:
//                    return leftInt == rightInt;
//                case NOT_EQUAL:
//                    return leftInt != rightInt;
//                case LESS_THAN:
//                    return leftInt < rightInt;
//                case LESS_THAN_EQUAL:
//                    return leftInt <= rightInt;
//                case GREATER_THAN:
//                    return leftInt > rightInt;
//                case GREATER_THAN_EQUAL:
//                    return leftInt >= rightInt;
//                default:
//                    throw new Exception("Unsupported binary operator: " + operator);
//            }
//        } else {
//            throw new Exception("Operands for binary expressions must be integers");
//        }
//    }
}
