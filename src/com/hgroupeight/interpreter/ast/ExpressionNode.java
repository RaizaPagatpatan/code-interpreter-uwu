package com.hgroupeight.interpreter.ast;

import com.hgroupeight.interpreter.symboltable.SymbolTable;

public abstract class ExpressionNode {
    public enum ExpressionType {
        INTEGER,
        BINARY,
        UNARY,
        STRING,
        BOOLEAN,
        CHARACTER,
        IDENTIFIER,
        FLOAT
    }



    private final ExpressionType expressionType;

    public ExpressionNode(ExpressionType expressionType) {
        this.expressionType = expressionType;
    }

    public ExpressionType getExpressionType() {
        return expressionType;
    }

    public abstract Object evaluate(SymbolTable symbolTable) throws Exception;
}
