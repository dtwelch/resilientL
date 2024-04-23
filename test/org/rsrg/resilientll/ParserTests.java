package org.rsrg.resilientll;

import io.vavr.collection.Vector;
import jdk.jshell.Snippet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.rsrg.resilientll.tree.Child;
import org.rsrg.resilientll.tree.Tree;
import org.rsrg.resilientll.tree.TreeKind;

public class ParserTests {

    @Test public void smokeTest01() {
        var text = "fn f(){ x; }";
        var p = Parser.parse(text);
        Assertions.assertEquals("""
                File
                 Fn
                   'fn'
                   'f'
                  ParamList
                    '('
                    ')'
                  Block
                    '{'
                   StmtExpr
                    ExprName
                      'x'
                     ';'
                    '}'
                """, p.toString());
    }

}
