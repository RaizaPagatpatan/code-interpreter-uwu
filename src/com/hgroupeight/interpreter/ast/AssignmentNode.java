package com.hgroupeight.interpreter.ast;

import com.hgroupeight.interpreter.symboltable.SymbolTable;

public class AssignmentNode extends StatementNode {
    public String variableName;
    public ExpressionNode expression;

    public AssignmentNode(String variableName, ExpressionNode expression) {
        this.variableName = variableName;
        this.expression = expression;
    }

    @Override
    public void execute(SymbolTable symbolTable) throws Exception {
        Object value = expression.evaluate(symbolTable);
        symbolTable.addVariable(variableName, value);
    }

    @Override
    public String toString() {
        return "Assignment: " + variableName + " = " + expression;
    }
}