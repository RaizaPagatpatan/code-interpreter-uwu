package com.hgroupeight.interpreter.ast;

import com.hgroupeight.interpreter.symboltable.SymbolTable;

import java.util.List;

public class DisplayNode extends StatementNode {
    public List<ExpressionNode> expressions;

    public List<ExpressionNode> getExpressions() {
        return expressions;
    }

    public DisplayNode(List<ExpressionNode> expressions) {
        this.expressions = expressions;
    }

    @Override
    public void execute(SymbolTable symbolTable) throws Exception {
        StringBuilder output = new StringBuilder();
        for (ExpressionNode expression : expressions) {
            Object value = expression.evaluate(symbolTable);
            output.append(value);
        }
        System.out.println(output.toString());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DisplayNode: ");
        for (ExpressionNode expression : expressions) {
            builder.append(expression).append(", ");
        }
        return builder.toString();
    }
}