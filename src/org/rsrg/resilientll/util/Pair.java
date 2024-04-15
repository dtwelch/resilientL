package org.rsrg.resilientll.util;

public record Pair<A, B>(A first, B second) {
    @Override public String toString() {
        return STR."(\{first}, \{second})";
    }
}
