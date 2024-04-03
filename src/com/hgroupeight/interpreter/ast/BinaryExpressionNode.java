//package com.hgroupeight.interpreter.ast;
//
//import com.hgroupeight.interpreter.symboltable.SymbolTable;
//
//public class BinaryExpressionNode extends ExpressionNode {
//    public ExpressionNode left, right;
//    public BinaryOperator operator;
//
//    public enum BinaryOperator {
//        ADDITION("+"),
//        SUBTRACTION("-"),
//        MULTIPLICATION("*"),
//        DIVISION("/");
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
//    public BinaryExpressionNode(ExpressionType expressionType) {
//        super(expressionType);
//    }
//
//
//    @Override
//    public Object evaluate(SymbolTable symbolTable) throws Exception {
//        Object leftValue = left.evaluate(symbolTable);
//        Object rightValue = right.evaluate(symbolTable);
//
//        if (!(leftValue instanceof Integer) || !(rightValue instanceof Integer)) {
//            throw new Exception("Operands for binary expressions must be integers");
//        }
//
//        int leftInt = (Integer) leftValue;
//        int rightInt = (Integer) rightValue;
//
//        int result;
//        switch (operator) {
//            case ADDITION:
//                result = leftInt + rightInt;
//                break;
//            case SUBTRACTION:
//                result = leftInt - rightInt;
//                break;
//            case MULTIPLICATION:
//                result = leftInt * rightInt;
//                break;
//            case DIVISION:
//                if (rightInt == 0) {
//                    throw new Exception("Division by zero");
//                }
//                result = leftInt / rightInt;
//                break;
//            default:
//                throw new Exception("Unsupported binary operator: " + operator);
//        }
//
//        return result;
//    }
//}


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
        LOGICAL_OR("OR");

        private final String symbol;

        BinaryOperator(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }

    public BinaryExpressionNode(ExpressionType expressionType) {
        super(expressionType);
    }

    @Override
    public Object evaluate(SymbolTable symbolTable) throws Exception {
        Object leftValue = left.evaluate(symbolTable);
        Object rightValue = right.evaluate(symbolTable);

        if (leftValue instanceof Integer && rightValue instanceof Integer) {
            int leftInt = (Integer) leftValue;
            int rightInt = (Integer) rightValue;
            switch (operator) {
                case ADDITION:
                    return leftInt + rightInt;
                case SUBTRACTION:
                    return leftInt - rightInt;
                case MULTIPLICATION:
                    return leftInt * rightInt;
                case DIVISION:
                    if (rightInt == 0) {
                        throw new Exception("Division by zero");
                    }
                    return leftInt / rightInt;
                case EQUAL:
                    return leftInt == rightInt;
                case NOT_EQUAL:
                    return leftInt != rightInt;
                case LESS_THAN:
                    return leftInt < rightInt;
                case LESS_THAN_EQUAL:
                    return leftInt <= rightInt;
                case GREATER_THAN:
                    return leftInt > rightInt;
                case GREATER_THAN_EQUAL:
                    return leftInt >= rightInt;
                default:
                    throw new Exception("Unsupported binary operator: " + operator);
            }
        } else if (leftValue instanceof Boolean && rightValue instanceof Boolean) {
            boolean leftBool = (Boolean) leftValue;
            boolean rightBool = (Boolean) rightValue;
            switch (operator) {
                case LOGICAL_AND:
                    return leftBool && rightBool;
                case LOGICAL_OR:
                    return leftBool || rightBool;
                default:
                    throw new Exception("Unsupported binary operator: " + operator);
            }
        } else {
            throw new Exception("Operands for binary expressions must be of the same type");
        }
    }
}
