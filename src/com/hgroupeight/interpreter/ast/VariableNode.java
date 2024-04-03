package com.hgroupeight.interpreter.ast;

public class VariableNode {
    public String name;
    public DataType type;
    public Object value;

    public VariableNode(String name, DataType type, ExpressionNode expression) {
        this.name = name;
        this.type = type;
        switch (type) {
            case INTEGER:
                value = 0;
                break;
            case CHAR:
                value = '\0';
                break;
            case BOOLEAN:
                value = false;
                break;
            case STRING:
                value = "";
                break;
            default:
                throw new IllegalArgumentException("Unsupported data type: " + type);
        }
    }

    public String getName() {
        return name;
    }

    public Object getValue() { return  value; }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Variable: " + name + ", Type: " + type + ", Value: " + value;
    }
}