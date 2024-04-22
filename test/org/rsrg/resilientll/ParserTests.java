package org.rsrg.resilientll;

import org.junit.jupiter.api.Test;
import org.rsrg.resilientll.tree.TreeKind;

public class ParserTests {

    @Test public void smokeTest01() {
        var text = "fn f(){ x; }";
        var p = Parser.parse(text);
        System.out.println(p);
    }
}
