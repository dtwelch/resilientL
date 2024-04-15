# resilientL

Resilient LL parsing. This repo contains an "IDE Amenable" earley parser and lexer for a small
language called ***L***.

Follows [this](https://github.com/matklad/resilient-ll-parsing/tree/master)
original blog post: https://matklad.github.io/2023/05/21/resilient-ll-parsing-tutorial.html
(a rust analyzer developer)

### Ungrammar (regex-style BNF) for language *L*

```antlr
File = Fn*

Fn = 'fn' 'name' ParamList ('->' TypeExpr)? Block

ParamList = '(' Param* ')'
Param = 'name' ':' TypeExpr ','?

TypeExpr = 'name'

Block = '{' Stmt* '}'

Stmt =
  StmtLet
| StmtReturn
| StmtExpr

StmtLet    = 'let' 'name' '=' Expr ';'
StmtReturn = 'return' Expr ';'
StmtExpr   = Expr ';'

Expr =
  ExprLiteral
| ExprName
| ExprParen
| ExprBinary
| ExprCall

ExprLiteral = 'int' | 'true' | 'false'
ExprName    = 'name'
ExprParen   = '(' Expr ')'
ExprBinary  = Expr ('+' | '-' | '*' | '/') Expr
ExprCall    = Expr ArgList

ArgList = '(' Arg* ')'
Arg = Expr ','?
```

