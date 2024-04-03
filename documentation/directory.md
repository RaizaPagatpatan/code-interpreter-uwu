code-interpreter-uwu/
├── src/
│   ├── com/
│   │   └── hgroupeight/
│   │       └── interpreter/
│   │           ├── lexer/  
│   │           │   ├── Token.java
│   │           │   └── Lexer.java
│   │           ├── parser/  
│   │           │   ├── Token.java (reference from lexer)  // recycled token
│   │           │   └── Parser.java
│   │           ├── ast/      // abstraction tree for syntax idk 
│   │           │   ├── ProgramNode.java
│   │           │   ├── VariableNode.java
│   │           │   ├── StatementNode.java (abstraction node)
│   │           │   │   ├── AssignmentNode.java
│   │           │   │   ├── DisplayNode.java
│   │           │   │   // add more assignment nodes based on [Language Grammar Specs](lang-grammar.md) (if-else, loop, etc.)
│   │           │   └── ExpressionNode.java (abstraction node again) //expression node logic is in [Expressions](expression.md)
│   │           │       ├── BinaryExpressionNode.java // division | multiplication
│   │           │       ├── UnaryExpressionNode.java // addition | subtraction
│   │           │       ├── LiteralNode.java // literal expression xD literal AHHAHAHHHA fk
│   │           │       └── VariableNode (reference from ast)  // recycle variable node class
│   │           ├── symboltable/ // class for variable symbols
│   │           │   └── SymbolTable.java
│   │           └── interpreter/ // interpreting abstraction syntax
│   │               └── Interpreter.java
│   └── Main.java                // main code where you can put the String code


