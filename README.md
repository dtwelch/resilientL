# resilientL

Some experimentation with a resilient lexer and LL parser. The parser and lexer are designed to be "IDE Amenable" --
targeting a small language called ***L***.

Follows notes from a rust-analyzer
developer documented [here]( https://matklad.github.io/2023/05/21/resilient-ll-parsing-tutorial.html).
Original rust implementation [here]( https://github.com/matklad/resilient-ll-parsing/tree/master).

> Update - 04/24/24: checkpointing this for now; lexer is pretty much there; parser code minimal; handles fn defns with no params and basic blocks consisting of a named statement expr

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

