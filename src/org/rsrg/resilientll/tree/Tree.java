package org.rsrg.resilientll.tree;

import io.vavr.collection.List;
import io.vavr.collection.Vector;

public record Tree(TreeKind kind, Vector<Child> children){

}
