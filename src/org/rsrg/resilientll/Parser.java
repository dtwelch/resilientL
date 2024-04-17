package org.rsrg.resilientll;

import io.vavr.collection.Vector;
import org.rsrg.resilientll.tree.Tree;
import org.rsrg.resilientll.tree.TreeKind;

import java.util.EnumSet;

public final class Parser {

    private final Vector<Lexer.Token> tokens;
    private int pos;

    // meant to signal runaway recursion in the parser
    // (thread safe if made an AtomicInteger?)
    private int fuel;

    private Vector<Event> events;

    public record MarkOpened(int index) {
    }

    public record MarkClosed(int index) {
    }

    public Parser(Vector<Lexer.Token> tokens) {
        this.tokens = tokens;
        this.pos = 0;
        this.fuel = 256;
        this.events = Vector.empty();
    }

    public Tree buildTree() {
        /*Iterator<Lexer.Token> tokens = this.tokens.iterator();
        Vector<Event> events = this.events;

        // !events.isEmpty() && events.get(events.size() - 1) == Event.Close
        List<Tree> stack = List.empty();
        for (Event event : events) {
            switch (event) {
                case Open(var kind) -> {
                    stack = stack.append(new Tree(kind, Vector.empty())) ;
                }
                case Close _ -> {
                    //stack = stack.append(new Tree())
                }
                case Advance _ -> {

                }
            }
        }*/
        throw new UnsupportedOperationException("not done");
    }

    public MarkOpened open() {
        var mark = new MarkOpened(events.length());
        events = events.append(new Event.Open(TreeKind.ErrorTree));
        return mark;
    }

    public MarkOpened openBefore(MarkClosed m) {
        var mark = new MarkOpened(m.index);
        events = events.insert(m.index, new Event.Open(TreeKind.ErrorTree));
        return mark;
    }

    public MarkClosed close(MarkOpened m, TreeKind kind) {
        events = events.update(m.index, new Event.Open(kind));
        events = events.append(Event.Close.Instance);
        return new MarkClosed(m.index);
    }

    public void advance() {
        // assert !eof()
        fuel = 256;
        events = events.append(new Event.Advance())
    }

    public boolean eof() {
        return pos == tokens.length();
    }

    /**
     * Returns the kind of the token at the current position plus the
     * {@code lookahead}. Defaults to Eof if the index is out of range.
     *
     * @throws IllegalStateException if no fuel remains.
     */
    public Lexer.TokenKind nth(int lookahead) {
        if (fuel == 0) {
            throw new IllegalStateException("parser is stuck");
        }
        fuel = fuel - 1;
        return tokens.getOption(this.pos + lookahead)
                .map(Lexer.Token::kind)
                .getOrElse(Lexer.TokenKind.Eof);
    }

    public boolean at(Lexer.TokenKind kind) {
        return this.nth(0) == kind;
    }

    // enumSet is a very efficient set implementation designed for storing
    // enum values... update: guess normal old bitfields are orders of magnitude
    // faster (according to): https://nullprogram.com/blog/2021/04/23/
    // update, bad benchmark in above it seems: https://nihathrael.github.io/blog/enumset-benchmark/
    public boolean atAny(EnumSet<Lexer.TokenKind> kinds) {
        return kinds.contains(this.nth(0));
    }

    public static Tree parse(String text) {
        var tokens = Lexer.lex(text);
        var p = new Parser(tokens);
        throw new UnsupportedOperationException("not done");
    }

    private static void file(Parser p) {
        MarkOpened m = p.open();
        while (!p.eof()) {
            if (p.)
        }
    }

    // events

    public sealed interface Event {
        enum Close implements Event {Instance}

        enum Advance implements Event {Instance}

        record Open(TreeKind kind) implements Event {
        }
    }

}
