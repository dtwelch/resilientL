package org.rsrg.resilientll.tree;

import org.rsrg.resilientll.Lexer;

public sealed interface Child {

    record CToken(Lexer.Token t) implements Child {}

    record CTree(Tree t) implements Child {}
}
