package org.rsrg.resilientll;

public enum TreeKind {
    ErrorTree, File,
    Fn, TypeExpr,
    ParamList, Param,
    Block,
    StmtLet, StmtReturn, StmtExpr,
    ExprLiteral, ExprName, ExprParen, ExprBinary, ExprCall,
    ArgList, Arg
}
