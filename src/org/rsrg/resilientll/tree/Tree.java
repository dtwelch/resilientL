package org.rsrg.resilientll.tree;

import io.vavr.collection.Vector;

public record Tree(TreeKind kind, Vector<Child> children) {

    /** Returns a version of {@code this} with child {@code c} appended. */
    public Tree withChild(Child c) {
        return new Tree(kind, children.append(c));
    }

    private void print(StringBuilder buf, int level) {
        var indent = " ".repeat(level);
        buf.append(indent).append(kind.getClass().getSimpleName()).append("\n");

        for (Child child : children) {
            switch (child) {
                case Child.CToken(var token) -> buf.append(indent).append("  '").append(token.text()).append("'\n");
                case Child.CTree(var tree) -> tree.print(buf, level + 1);
            }
        }
    }

    /*@Override public String toString() {
        var sb = new StringBuilder();
        print(sb, 0);
        return sb.toString();
    }*/
}
