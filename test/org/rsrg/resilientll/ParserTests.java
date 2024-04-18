package org.rsrg.resilientll;

import org.junit.jupiter.api.Test;

public class ParserTests {

    @Test public void smokeTest01() {
        var text = "fn f(){}";
        var p = Parser.parse(text);

    }
}
