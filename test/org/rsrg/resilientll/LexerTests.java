package org.rsrg.resilientll;

import io.vavr.collection.Vector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LexerTests {

    // some static constants for (positive) tests; note: the flix compiler
    // does not have positive tests written the way we do here in this proj...
    // rather they do them through their own languages @test annotation; seems
    // like it requires one to be able to get all the way through the frontend + codegen
    // before any positive tests can be run? seems.... dunno. hard?
    private final Lexer.Token Fn = new Lexer.Token(Lexer.TokenKind.FnKeyword, "fn");
    private final Lexer.Token Let = new Lexer.Token(Lexer.TokenKind.LetKeyword, "let");
    private final Lexer.Token Return = new Lexer.Token(Lexer.TokenKind.ReturnKeyword, "return");
    private final Lexer.Token True = new Lexer.Token(Lexer.TokenKind.TrueKeyword, "true");
    private final Lexer.Token False = new Lexer.Token(Lexer.TokenKind.FalseKeyword, "false");

    private final Lexer.Token LParen = new Lexer.Token(Lexer.TokenKind.LParen, "(");
    private final Lexer.Token RParen = new Lexer.Token(Lexer.TokenKind.RParen, ")");
    private final Lexer.Token LCurly = new Lexer.Token(Lexer.TokenKind.LCurly, "{");
    private final Lexer.Token RCurly = new Lexer.Token(Lexer.TokenKind.RCurly, "}");
    private final Lexer.Token Eq = new Lexer.Token(Lexer.TokenKind.Eq, "=");
    private final Lexer.Token Semi = new Lexer.Token(Lexer.TokenKind.Semi, ";");
    private final Lexer.Token Comma = new Lexer.Token(Lexer.TokenKind.Comma, ",");
    private final Lexer.Token Colon = new Lexer.Token(Lexer.TokenKind.Colon, ":");
    private final Lexer.Token Arrow = new Lexer.Token(Lexer.TokenKind.Arrow, "->");
    private final Lexer.Token Plus = new Lexer.Token(Lexer.TokenKind.Plus, "+");
    private final Lexer.Token Minus = new Lexer.Token(Lexer.TokenKind.Minus, "-");
    private final Lexer.Token Star = new Lexer.Token(Lexer.TokenKind.Star, "*");
    private final Lexer.Token Slash = new Lexer.Token(Lexer.TokenKind.Slash, "/");

    @Test public void testBasicFnHeader() {
        var expected = Vector.of(Fn, Lexer.Token.mkName("f"), LParen, RParen, LCurly, RCurly);
        var result = Lexer.lex("fn f() { }");
        assertEquals(expected, result);
    }

    @Test public void testEmptyInput() {
        var expected = Vector.empty();
        var result = Lexer.lex("");
        assertEquals(expected, result);
    }

    @Test public void testOnlyWhitespace() {
        var expected = Vector.empty();
        var result = Lexer.lex("   \t\n   ");
        assertEquals(expected, result);
    }

    @Test public void testSingleTokens() {
        var expected = Vector.of(LParen, RParen, LCurly, RCurly);
        var result = Lexer.lex("(){}");
        assertEquals(expected, result);
    }

    @Test public void testFunctionDeclaration() {
        var expected = Vector.of(Fn, Lexer.Token.mkName("main"), LParen, RParen, LCurly, RCurly);
        var result = Lexer.lex("fn main() {}");
        assertEquals(expected, result);
    }

    @Test public void testVariablesAndOperations() {
        var expected = Vector.of(Lexer.Token.mkName("x"), Eq, Lexer.Token.mkInt("10"), Semi, Lexer.Token.mkName("y"),
                Eq, Lexer.Token.mkName("x"), Plus, Lexer.Token.mkInt("20"), Semi);
        var result = Lexer.lex("x = 10; y = x + 20;");
        assertEquals(expected, result);
    }

    @Test public void testKeywordsAndIdentifiers() {
        var expected = Vector.of(Let, Lexer.Token.mkName("value"), Eq, True, Semi);
        var result = Lexer.lex("let value = true;");
        assertEquals(expected, result);
    }

    @Test public void testErrorTokens() {
        var expected = Vector.of(Lexer.Token.mkName("validName"), Lexer.Token.mkErr("#$%^"));
        var result = Lexer.lex("validName #$%^");
        assertEquals(expected, result);
    }
}
