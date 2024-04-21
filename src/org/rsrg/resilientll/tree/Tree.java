package org.rsrg.resilientll.tree;

import io.vavr.collection.Vector;

public record Tree(TreeKind kind, Vector<Child> children){

    /** Returns a version of {@code this} with child {@code c} appended. */
    public Tree withChild(Child c) {
        return new Tree(kind, children.append(c));
    }
}
