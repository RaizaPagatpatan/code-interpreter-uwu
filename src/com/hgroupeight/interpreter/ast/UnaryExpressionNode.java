package com.hgroupeight.interpreter.ast;

import com.hgroupeight.interpreter.lexer.Token;
import com.hgroupeight.interpreter.symboltable.SymbolTable;

public class UnaryExpressionNode extends ExpressionNode {
    public ExpressionNode expression;
    public UnaryOperator operator; // Added for specifying unary operator

    public UnaryExpressionNode(UnaryOperator operator, ExpressionNode expression) {
        super(ExpressionType.UNARY);
        this.operator = operator;
        this.expression = expression;
    }



    public enum UnaryOperator {
        MINUS("-"); // Add more unary operators as needed

        private final String symbol;

        UnaryOperator(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }

    public UnaryExpressionNode(ExpressionType expressionType) {
        super(expressionType);
    }

    @Override
    public Object evaluate(SymbolTable symbolTable) throws Exception {
        Object value = expression.evaluate(symbolTable);

        if (!(value instanceof Integer)) {
            throw new Exception("Operand for unary minus must be an integer");
        }

        int intValue = (Integer) value;

        switch (operator) {
            case MINUS:
                return -intValue;
            default:
                throw new Exception("Unsupported unary operator: " + operator);
        }
    }
}
