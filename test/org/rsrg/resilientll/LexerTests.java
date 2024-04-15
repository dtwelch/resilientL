package org.rsrg.resilientll;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LexerTests {

    @Test public void test01() {
        Lexer.Token[] expected = {Lexer.Token.Fn, Lexer.Token.mkName("f"), Lexer.Token.LParen, Lexer.Token.RParen,
                Lexer.Token.LCurly, Lexer.Token.RCurly};
        var result = Lexer.lex("fn f() { }");
        Assertions.assertEquals(expected, result);
    })
}
}
