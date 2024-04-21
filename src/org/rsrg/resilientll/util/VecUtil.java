package org.rsrg.resilientll.util;

import io.vavr.collection.Vector;

public final class VecUtil {

    private VecUtil() {
    }

    public static <A> Vector<A> push(Vector<A> vec, A item) {
        return vec.append(item);
    }

    public static <A> Pair<A, Vector<A>> pop(Vector<A> vec) {
        if (vec.isEmpty()) {
            throw new IllegalStateException("Cannot pop from an empty vector");
        }
        // The last element
        A lastElement = vec.last();
        // Remove the last element to get the new vector
        Vector<A> remainingVector = vec.removeAt(vec.size() - 1);
        return new Pair<>(lastElement, remainingVector);
    }

}
