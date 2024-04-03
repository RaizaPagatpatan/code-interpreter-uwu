Parsing Logic for Expressions - Docs

call nodes via: node.execute(), value.execute()

Expression (Literal) = BE (<, =, >=, <=, AND, OR, NOT) | BE
Basic Expression = factor + factor, factor - factor // unary operators, subtraction & addition
Factor = term x term | term / term | term ; // binary operators, division & multiplication
Term = values | (expression) ; // check expression

1. check left node
2. check right node on factors, check expression | check expression -> binary, suspend node



    The following is a BNF grammar for the CODE programming language.

    EXPRESSION ::= TERM | TERM BINARY_OPERATOR TERM
    TERM ::= UNARY_OPERATOR TERM | VALUE | VARIABLE | FUNCTION
    BINARY_OPERATOR ::= "+" | "-" | "*" | "/" | "%" | "AND" | "OR" | "==" | ">" | ">=" | "<" | "<="
    UNARY_OPERATOR ::= "++" | "--" | "NOT"