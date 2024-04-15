package org.rsrg.resilientll;

import io.vavr.collection.Vector;
import org.rsrg.resilientll.util.Maybe;
import org.rsrg.resilientll.util.Pair;

import java.util.ArrayList;
import java.util.function.Predicate;

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

        // some static constants for (positive) tests; note: the flix compiler
        // does not have positive tests written the way we do here in this proj...
        // rather they do them through their own languages @test annotation; seems
        // like it requires one to be able to get all the way through the frontend + codegen
        // before any positive tests can be run? seems.... dunno. hard?
        static final Token Fn = new Token(TokenKind.FnKeyword, "fn");
        static final Token Let = new Token(TokenKind.LetKeyword, "let");
        static final Token Return = new Token(TokenKind.ReturnKeyword, "return");
        static final Token True = new Token(TokenKind.TrueKeyword, "true");
        static final Token False = new Token(TokenKind.FalseKeyword, "false");

        static final Token LParen = new Token(TokenKind.LParen, "(");
        static final Token RParen = new Token(TokenKind.RParen, ")");
        static final Token LCurly = new Token(TokenKind.LCurly, "{");
        static final Token RCurly = new Token(TokenKind.RCurly, "}");
        static final Token Eq = new Token(TokenKind.Eq, "=");
        static final Token Semi = new Token(TokenKind.Semi, ";");
        static final Token Comma = new Token(TokenKind.Comma, ",");
        static final Token Colon = new Token(TokenKind.Colon, ":");
        static final Token Arrow = new Token(TokenKind.Arrow, "->");
        static final Token Plus = new Token(TokenKind.Plus, "+");
        static final Token Minus = new Token(TokenKind.Minus, "-");
        static final Token Star = new Token(TokenKind.Star, "*");
        static final Token Slash = new Token(TokenKind.Slash, "/");

        static Token mkName(String name) {
            return new Token(TokenKind.Name, name);
        }

        static Token mkInt(String name) {
            return new Token(TokenKind.Int, name);
        }

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

    public static Vector<Token> lex(String text) {

        var punctuation = new Pair<>("( ) { } = ; , : -> + - * /", new TokenKind[]{TokenKind.LParen, TokenKind.RParen
                , TokenKind.LCurly, TokenKind.RCurly, TokenKind.Eq, TokenKind.Semi, TokenKind.Comma, TokenKind.Colon,
                TokenKind.Arrow, TokenKind.Plus, TokenKind.Minus, TokenKind.Slash});

        var keywords = new Pair<>("fn let return true false", new TokenKind[]{TokenKind.FnKeyword,
                TokenKind.LetKeyword, TokenKind.ReturnKeyword, TokenKind.TrueKeyword, TokenKind.FalseKeyword});

        var result = Vector.<Token>empty();

        while (!text.isEmpty()) {
            switch (trim(text, Character::isWhitespace)) {
                case Maybe.Some(var rest) -> {
                    text = rest;
                    continue;
                }
                default -> {}
            }
            var text_orig =  
        } return result;
    }

    private static Maybe<String> trim(String s, Predicate<Character> predicate) {
        int index = 0;
        while (index < s.length() && !predicate.test(s.charAt(index))) {
            index++;
        }
        if (index == 0) {
            return Maybe.none();
        } else {
            return Maybe.of(s.substring(index));
        }
    }
}
