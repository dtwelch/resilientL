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
        Assertions.assertEquals(p, buildSampleProg1());
    }

    /**
     * Courtesy of gpt when given default record toString for (handchecked)
     * parse {@link Tree} instance {@code p}.
     * <p>
     * Here's the originating program: {@code fn f() {}} and conversation...
     * so this (generated) method builds up and returns the tree structure
     * that (we hope) the parser would have produced.
     * <pre>{@code
     *  me> build me this hierarchy please ... put it in an Assertion.assertEquals(p, , ...
     *  specifically build me this instance:
     *   Tree[kind=Instance, children=Vector(CTree[t=Tree[kind=Instance,
     *          children=Vector(CToken[t=Token[kind=FnKeyword, text=fn]], CToken[t=Token[kind=Name, text=f]],
     *          CTree[t=Tree[kind=Instance, children=Vector(CToken[t=Token[kind=LParen, text=(]], CToken[t=Token[kind=RParen, text=)]])]], CTree[t=Tree[kind=Instance, children=Vector(CToken[t=Token[kind=LCurly, text={]], CTree[t=Tree[kind=Instance, children=Vector(CTree[t=Tree[kind=Instance, children=Vector(CToken[t=Token[kind=Name, text=x]])]], CToken[t=Token[kind=Semi, text=;]])]], CToken[t=Token[kind=RCurly, text=}]])]])]])]
     *  me> No, top level would be a Tree with a bunch of CTree and CToken nodes as the immediate children... also I
     *  have an enum for the treekind...
     *  gpt> Alright [...] here's how to construct the tree hierarchy you described with these corrections.
     *  gpt> *gives the right sol*
     * }</pre>
     */
    private Tree buildSampleProg1() {
        // create Tokens
        Lexer.Token fnKeyword = new Lexer.Token(Lexer.TokenKind.FnKeyword, "fn");
        Lexer.Token nameF = new Lexer.Token(Lexer.TokenKind.Name, "f");
        Lexer.Token lParen = new Lexer.Token(Lexer.TokenKind.LParen, "(");
        Lexer.Token rParen = new Lexer.Token(Lexer.TokenKind.RParen, ")");
        Lexer.Token lCurly = new Lexer.Token(Lexer.TokenKind.LCurly, "{");
        Lexer.Token rCurly = new Lexer.Token(Lexer.TokenKind.RCurly, "}");
        Lexer.Token semi = new Lexer.Token(Lexer.TokenKind.Semi, ";");
        Lexer.Token nameX = Lexer.Token.mkName("x");

        // create children from tokens
        Child.CToken fnToken = new Child.CToken(fnKeyword);
        Child.CToken fNameToken = new Child.CToken(nameF);
        Child.CToken lParenToken = new Child.CToken(lParen);
        Child.CToken rParenToken = new Child.CToken(rParen);
        Child.CToken lCurlyToken = new Child.CToken(lCurly);
        Child.CToken rCurlyToken = new Child.CToken(rCurly);
        Child.CToken semiToken = new Child.CToken(semi);
        Child.CToken xNameToken = new Child.CToken(nameX);

        // construct nested trees
        Tree paramsTree = new Tree(TreeKind.ParamList.Instance, Vector.of(lParenToken, rParenToken));
        Tree bodyTree = new Tree(TreeKind.Block.Instance, Vector.of(lCurlyToken, new Child.CTree(new Tree(TreeKind.StmtExpr.Instance, Vector.of(new Child.CTree(new Tree(TreeKind.ExprName.Instance, Vector.of(xNameToken, semiToken))), rCurlyToken)))));
        Tree functionTree = new Tree(TreeKind.Fn.Instance, Vector.of(fnToken, fNameToken, new Child.CTree(paramsTree), new Child.CTree(bodyTree)));

        // root tree
        Tree root = new Tree(TreeKind.File.Instance, Vector.of(new Child.CTree(functionTree)));

        return root;
    }
}
