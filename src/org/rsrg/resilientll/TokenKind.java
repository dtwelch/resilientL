package org.rsrg.resilientll;

public enum TokenKind {
    ErrorToken, Eof,

    LParen, RParen, LCurly, RCurly,
    Eq, Semi, Comma, Colon, Arrow,
    Plus, Minus, Star, Slash,

    FnKeyword, LetKeyword, ReturnKeyword,
    TrueKeyword, FalseKeyword,

    Name, Int
}
