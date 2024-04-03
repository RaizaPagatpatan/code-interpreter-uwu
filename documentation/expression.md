Parsing Logic for Expressions - Docs

call nodes via: node.execute(), value.execute()

Expression (Literal) = BE (<, =, >=, <=, AND, OR, NOT) | BE
Basic Expression = factor + factor, factor - factor // unary operators, subtraction & addition
Factor = term x term | term / term | term ; // binary operators, division & multiplication
Term = values | (expression) ; // check expression

1. check left node
2. check right node on factors, check expression | check expression -> binary, suspend node
3. 