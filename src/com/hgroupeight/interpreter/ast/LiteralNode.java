package com.hgroupeight.interpreter.ast;

import com.hgroupeight.interpreter.symboltable.SymbolTable;

public class LiteralNode extends ExpressionNode {
    public Object value;

    public LiteralNode(ExpressionType expressionType, Object value) {
        super(expressionType);
        this.value = value;
    }

    @Override
    public Object evaluate(SymbolTable symbolTable) throws Exception {
        return value;
    }

    @Override
    public String toString() {
        return "" + value;
    }
}
