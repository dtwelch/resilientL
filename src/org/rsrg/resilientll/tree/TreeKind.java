package org.rsrg.resilientll.tree;

/** Different kinds of syntax nodes in a Syntax Tree. */
public sealed interface TreeKind {

    enum ErrorTree implements TreeKind {Instance}

    enum File implements TreeKind {Instance}

    enum Fn implements TreeKind {Instance}

    enum TypeExpr implements TreeKind {Instance}

    enum ParamList implements TreeKind {Instance}

    enum Param implements TreeKind {Instance}

    enum Block implements TreeKind {Instance}

    enum StmtLet implements TreeKind {Instance}

    enum StmtReturn implements TreeKind {Instance}

    enum StmtExpr implements TreeKind {Instance}

    enum ExprLiteral implements TreeKind {Instance}

    enum ExprName implements TreeKind {Instance}

    enum ExprParen implements TreeKind {Instance}

    enum ExprBinary implements TreeKind {Instance}

    enum ExprCall implements TreeKind {Instance}

    enum ArgList implements TreeKind {Instance}

    enum Arg implements TreeKind {Instance}
}