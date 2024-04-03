package com.hgroupeight.interpreter.ast;

import com.hgroupeight.interpreter.lexer.Token;

public class VariableDeclarationNode implements Node {
    private Token.Type dataType;
    private String variableName;
    private ExpressionNode initialValue;

    public VariableDeclarationNode(Token.Type dataType, String variableName, ExpressionNode initialValue) {
        this.dataType = dataType;
        this.variableName = variableName;
        this.initialValue = initialValue;
    }

    // Getters for dataType, variableName, and initialValue

    @Override
    public String toString() {
        if (initialValue != null) {
            return dataType + " " + variableName + " = " + initialValue;
        } else {
            return dataType + " " + variableName;
        }
    }
}
