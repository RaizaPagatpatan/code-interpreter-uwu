package com.hgroupeight.interpreter.interpreter;

import com.hgroupeight.interpreter.ast.*;
import com.hgroupeight.interpreter.symboltable.SymbolTable;

import java.util.List;

public class Interpreter {
    public void interpret(ProgramNode program, SymbolTable symbolTable) throws Exception {
        // VAR Declare
        List<VariableNode> variableDeclarations = program.getDeclarations();

        for (VariableNode variable : variableDeclarations) {
            symbolTable.addVariable(variable.getName(), variable.getValue()); //null initial
        }

        // statement interpreter
        List<StatementNode> statements = program.getStatements();
        for (StatementNode statement : statements) {
            interpretStatement(statement, symbolTable);
        }
    }

    private void interpretStatement(StatementNode statement, SymbolTable symbolTable) throws Exception {
        if (statement instanceof DisplayNode) {
            DisplayNode displayNode = (DisplayNode) statement;
            interpretDisplay(displayNode, symbolTable);
        } else if (statement instanceof AssignmentNode) {
            AssignmentNode assignmentNode = (AssignmentNode) statement;
            interpretAssignment(assignmentNode, symbolTable);
        }
        //add more HERE!!!
    }

    private void interpretDisplay(DisplayNode displayNode, SymbolTable symbolTable) throws Exception {
        List<ExpressionNode> expressions = displayNode.getExpressions();

        StringBuilder output = new StringBuilder();
        for (ExpressionNode expression : expressions) {
            Object value = expression.evaluate(symbolTable);
            if (expression.getExpressionType() == ExpressionNode.ExpressionType.IDENTIFIER)
            {
                output.append(symbolTable.get(value.toString()));
                continue;
            }
            output.append(value);
        }
        System.out.println(output.toString());
    }

    private void interpretAssignment(AssignmentNode assignmentNode, SymbolTable symbolTable) throws Exception {
        String variableName = assignmentNode.variableName;
        Object value = assignmentNode.expression.evaluate(symbolTable);
        symbolTable.addVariable(variableName, value);
    }
}
