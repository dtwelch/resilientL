package org.rsrg.resilientll;

import org.rsrg.resilientll.util.Pair;

import java.util.ArrayList;

/** Encapsulates lexer related procedures and types. */
public final class Lexer {

    private Lexer() {
    }

    public enum TokenKind {
        ErrorToken, Eof,

        LParen, RParen, LCurly, RCurly, Eq, Semi, Comma, Colon, Arrow, Plus, Minus, Star, Slash,

        FnKeyword, LetKeyword, ReturnKeyword, TrueKeyword, FalseKeyword,

        Name, Int
    }

    public enum TreeKind {
        ErrorTree, File, Fn, TypeExpr, ParamList, Param, Block, StmtLet, StmtReturn, StmtExpr, ExprLiteral, ExprName,
        ExprParen, ExprBinary, ExprCall, ArgList, Arg
    }

    public record Token(TokenKind kind, String text) {
    }

    public record Tree(TreeKind kind, ArrayList<Child> children) {
    }

    public sealed interface Child {
    }

    public record CToken(Token t) implements Child {
    }

    public record CTree(Tree t) implements Child {
    }

    public static Tree parse(String text) {
        throw new UnsupportedOperationException("not done");
    }

    public static ArrayList<Token> lex(String text) {
        return lex(new StringBuilder(text));
    }

    private static ArrayList<Token> lex(StringBuilder text) {
        var punctuation = new Pair<>("( ) { } = ; , : -> + - * /", new TokenKind[]{TokenKind.LParen, TokenKind.RParen
                , TokenKind.LCurly, TokenKind.RCurly, TokenKind.Eq, TokenKind.Semi, TokenKind.Comma, TokenKind.Colon,
                TokenKind.Arrow, TokenKind.Plus, TokenKind.Minus, TokenKind.Slash});

        var keywords = new Pair<>("fn let return true false", new TokenKind[]{TokenKind.FnKeyword,
                TokenKind.LetKeyword, TokenKind.ReturnKeyword, TokenKind.TrueKeyword, TokenKind.FalseKeyword});

        var result = new ArrayList<Token>();

        while (!text.isEmpty()) {
            
        }

    }
}
