package org.rsrg.resilientll;

import io.vavr.collection.Vector;
import org.rsrg.resilientll.tree.Tree;
import org.rsrg.resilientll.tree.TreeKind;
import org.rsrg.resilientll.util.Maybe;

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
        events = events.append(Event.Advance.Instance);
        pos += 1;
    }

    public void advanceWithError(String error) {
        MarkOpened m = this.open();
        // todo: error reporting
        System.err.println(error);
        this.advance();
        this.close(m, TreeKind.ErrorTree);
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
        return tokens.getOption(this.pos + lookahead).map(Lexer.Token::kind).getOrElse(Lexer.TokenKind.Eof);
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

    public boolean eat(Lexer.TokenKind kind) {
        if (at(kind)) {
            advance();
            return true;
        } else {
            return false;
        }
    }

    public void expect(Lexer.TokenKind kind) {
        if (eat(kind)) {
            return;
        }
        System.err.println(STR."expected \{kind}");
    }

    public static Tree parse(String text) {
        var tokens = Lexer.lex(text);
        var p = new Parser(tokens);
        file(p);
        throw new UnsupportedOperationException("not done");
    }

    private static void file(Parser p) {
        // signal document open:
        // File
        // (note we don't pass in the kind when we open ... sometimes it's
        // possible to decide on the type of syntax node only after it is
        // parsed)
        MarkOpened m = p.open();

        // next, now that the document (File rule in the ungrammar) is open,
        // we are free to consume all tokens until we reach EOF. BUT since the
        // ungrammar specifies/allows only Fn defs at the top level, we expect
        // only FnKeyWords to appear in the token stream, and error out otherwise
        // placing presumably some error event into the stream? or just skipping
        // past entirely.
        while (!p.eof()) {
            if (p.at(Lexer.TokenKind.FnKeyword)) {
                func(p);
            } else {
                p.advanceWithError("expected a function");
            }
        }
        p.close(m, TreeKind.File);
    }

    // rule for parsing function defs at the top level of a file
    // precondition: p.at(FnKeyword) == true
    private static void func(Parser p) {
        // assertion p.at(FnKeyword) != false

        MarkOpened m = p.open();
        p.expect(Lexer.TokenKind.FnKeyword);
        p.expect(Lexer.TokenKind.Name);
        if (p.at(Lexer.TokenKind.LParen)) {
            paramList(p);
        }
        if (p.eat(Lexer.TokenKind.Arrow)) {
            typeExpr(p);
        }
        p.close(m, TreeKind.Fn);
    }

    private static final EnumSet<Lexer.TokenKind> ParamListRecoverSet = EnumSet.of(Lexer.TokenKind.FnKeyword,
            Lexer.TokenKind.LCurly);

    private static void paramList(Parser p) {
        // assert p.at(LParen)
        MarkOpened m = p.open();
        p.expect(Lexer.TokenKind.LParen);
        while (!p.at(Lexer.TokenKind.RParen) && !p.eof()) {

            // if (p.at(Name)) {
            //      param(p)
            // } else {
            if (p.atAny(ParamListRecoverSet)) {
                break;
            }
            p.advanceWithError("expected parameter");
            // }
        }
        p.expect(Lexer.TokenKind.RParen);
        p.close(m, TreeKind.ParamList);
    }

    private static void param(Parser p) {
        // assert p.at(Name)
    }

    private static void typeExpr(Parser p) {
        MarkOpened m = p.open();
        p.expect(Lexer.TokenKind.Name);
        p.close(m, TreeKind.TypeExpr);
    }

    private static final EnumSet<Lexer.TokenKind> StmtRecover = EnumSet.of(Lexer.TokenKind.FnKeyword);

    private static final EnumSet<Lexer.TokenKind> ExprFirst = EnumSet.of(Lexer.TokenKind.Int,
            Lexer.TokenKind.TrueKeyword, Lexer.TokenKind.FalseKeyword, Lexer.TokenKind.Name, Lexer.TokenKind.LParen);

    private static void block(Parser p) {
        // assert p.at(LCurly)
        MarkOpened m = p.open();
        p.expect(Lexer.TokenKind.LCurly);

        /*
        Stmt =      StmtLet
                |   StmtReturn
                |   StmtExpr
         */
        while (!p.at(Lexer.TokenKind.RCurly) && !p.eof()) {
            switch (p.nth(0)) {
                case LetKeyword -> throw new UnsupportedOperationException("not done");
                case ReturnKeyword -> throw new UnsupportedOperationException("not done");
                default -> {
                    if (p.atAny(ExprFirst)) {
                        stmtExpr(p);
                    } else {
                        if (p.atAny(StmtRecover)) {
                            break;
                        }
                        p.advanceWithError("expected statement");
                    }
                }
            }
        }
        p.expect(Lexer.TokenKind.RCurly);
        p.close(m, TreeKind.Block);
    }

    // StmtExpr = Expr ';'
    public static void stmtExpr(Parser p) {
        MarkOpened m = p.open();
        expr(p);
        p.expect(Lexer.TokenKind.Semi);
        p.close(m, TreeKind.StmtExpr);
    }

    // just doing for now
    // Expr ::=
    //   LiteralExpr
    //   NameExpr
    private static void expr(Parser p) {
        exprRec(p, Lexer.TokenKind.Eof);
    }

    private static void exprRec(Parser p, Lexer.TokenKind left) {

        // gpt:
        // the exprDelimited function is used to recognize and handle different
        // kinds of atomic expressions that are the building blocks for more
        // complex expressions.
        //
        // These building blocks include literals (like integers or booleans),
        // variable names, and parenthesized expressions. This is essential
        // because these elements:
        // a) start new expressions.
        // b) can be part of larger expression structures when combined with
        //      operators or function calls.

        MarkClosed lhs = null;
        switch (exprDelimited(p)) {
            case Maybe.Some(var leftHand) -> {
               lhs = leftHand;
            }
            default -> {
                return;
            }
        }
        // todo (1): function call syntax handling

        // todo (2): associativity and precdence stuff

    }

    private static Maybe<MarkClosed> exprDelimited(Parser p) {
        Maybe<MarkClosed> resultMaybe = switch (p.nth(0)) {
            case TrueKeyword, FalseKeyword, Int -> {
                var m = p.open();
                p.advance();
                yield Maybe.of(p.close(m, TreeKind.ExprLiteral));
            }
            case Name -> {
                var m = p.open();
                p.advance();
                yield Maybe.of(p.close(m, TreeKind.ExprName));
            }
            case LParen -> {
                var m = p.open();
                p.expect(Lexer.TokenKind.LParen);
                expr(p);
                p.expect(Lexer.TokenKind.RParen);
                yield Maybe.of(p.close(m, TreeKind.ExprParen));
            }
            default -> Maybe.none();
        };
        return resultMaybe;
    }

    // events

    public sealed interface Event {
        enum Close implements Event {Instance}

        enum Advance implements Event {Instance}

        record Open(TreeKind kind) implements Event {
        }
    }

}
