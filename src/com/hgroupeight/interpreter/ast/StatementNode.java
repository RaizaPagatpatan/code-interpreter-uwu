package com.hgroupeight.interpreter.ast;

import com.hgroupeight.interpreter.symboltable.SymbolTable;

public abstract class StatementNode {
    public abstract void execute(SymbolTable symbolTable) throws  Exception; // exception handling
}

