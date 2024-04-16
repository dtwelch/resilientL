package org.rsrg.resilientll.tree;

import io.vavr.collection.List;

public record Tree(TreeKind kind, List<Child> children){

}
