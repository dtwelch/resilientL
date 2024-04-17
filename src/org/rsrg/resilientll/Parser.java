package org.rsrg.resilientll;

import io.vavr.collection.Iterator;
import io.vavr.collection.Vector;
import org.rsrg.resilientll.tree.Tree;
import org.rsrg.resilientll.tree.TreeKind;

public final class Parser {

    private final Vector<Lexer.Token> tokens;
    private int pos;

    // meant to signal runaway recursion in the parser
    // (thread safe if made an AtomicInteger?)
    private int fuel;

    private Vector<Event> events;

    record MarkOpened(int index) {}

    record MarkClosed(int index) {}

    public Parser(Vector<Lexer.Token> tokens) {
        this.tokens = tokens;
        this.pos = 0;
        this.fuel = 256;
        this.events = Vector.empty();
    }

    public Tree buildTree() {
        Iterator<Lexer.Token> tokens = this.tokens.iterator();
        Vector<Event> events = this.events;

        // !events.isEmpty() && events.get(events.size() - 1) == Event.Close
        Vector<Tree> stack = Vector.empty();

    }

    public static Tree parse(String text) {
        var tokens = Lexer.lex(text);
        var p = new Parser(tokens);
        throw new UnsupportedOperationException("not done");
    }
    // events

    public sealed interface Event {
    }

    record Open(TreeKind kind) implements Event {
    }

    enum Close implements Event {Instance}

    enum Advance implements Event {Instance}

}
