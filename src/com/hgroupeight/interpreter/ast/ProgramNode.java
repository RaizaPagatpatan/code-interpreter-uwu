package com.hgroupeight.interpreter.ast;

import com.hgroupeight.interpreter.symboltable.SymbolTable;

import java.util.List;

public class ProgramNode {
    private List<VariableNode> declarations;
    private List<StatementNode> statements;

    public ProgramNode(List<VariableNode> declarations, List<StatementNode> statements) {
        this.declarations = declarations;
        this.statements = statements;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Declarations:\n");
        for (VariableNode declaration : declarations) {
            builder.append(declaration).append("\n");
        }
        builder.append("Statements:\n");
        for (StatementNode statement : statements) {
            builder.append(statement).append("\n");
        }
        return builder.toString();
    }

    public List<VariableNode> getDeclarations() {
        return declarations;
    }

    public List<StatementNode> getStatements() {
        return statements;
    }

    public void execute(SymbolTable symbolTable) throws Exception {
        for (VariableNode declaration : declarations) {
            symbolTable.addVariable(declaration.getName(), null);
        }
        for (StatementNode statement : statements) {
            statement.execute(symbolTable);
        }
    }
}
