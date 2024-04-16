package org.rsrg.resilientll;

import io.vavr.collection.Vector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LexerTests {

    @Test public void test01() {
        var expected = Vector.of(Lexer.Token.Fn, Lexer.Token.mkName("f"), Lexer.Token.LParen, Lexer.Token.RParen,
                Lexer.Token.LCurly, Lexer.Token.RCurly);
        var result = Lexer.lex("fn f() { }");
        Assertions.assertEquals(expected, result);
    }

}
