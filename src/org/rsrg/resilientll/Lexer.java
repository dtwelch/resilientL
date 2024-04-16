package org.rsrg.resilientll;

import io.vavr.collection.Vector;
import org.rsrg.resilientll.util.Maybe;
import org.rsrg.resilientll.util.Pair;

import java.util.ArrayList;
import java.util.function.Predicate;

/** Encapsulates lexer related procedures and types. */
public final class Lexer {

    private Lexer() {
    }

    public enum TokenKind {
        ErrorToken, Eof,

        LParen, RParen, LCurly, RCurly, Eq, Semi, Comma, Colon, Arrow, Plus, Minus, Star, Slash,

        FnKeyword, LetKeyword, ReturnKeyword, TrueKeyword, FalseKeyword,

        Name, Int
    }

    public record Token(TokenKind kind, String text) {

        // static factories for certain kinds of tokens (errors, names and ints)
        static Token mkErr(String text) {
            return new Token(TokenKind.ErrorToken, text);
        }

        static Token mkName(String name) {
            return new Token(TokenKind.Name, name);
        }

        static Token mkInt(String name) {
            return new Token(TokenKind.Int, name);
        }
    }

    public static Vector<Token> lex(String text) {

        String[] punctuation = {"(", ")", "{", "}", "=", ";", ",", ":", "->", "+", "-", "*", "/"};
        TokenKind[] punctuationKinds = {TokenKind.LParen, TokenKind.RParen, TokenKind.LCurly, TokenKind.RCurly,
                TokenKind.Eq, TokenKind.Semi, TokenKind.Comma, TokenKind.Colon, TokenKind.Arrow, TokenKind.Plus,
                TokenKind.Minus, TokenKind.Star, TokenKind.Slash};

        String[] keywords = {"fn", "let", "return", "true", "false"};
        TokenKind[] keywordKinds = {TokenKind.FnKeyword, TokenKind.LetKeyword, TokenKind.ReturnKeyword,
                TokenKind.TrueKeyword, TokenKind.FalseKeyword};

        var result = Vector.<Token>empty();

        while (!text.isEmpty()) {

            // consume any leading whitespace
            switch (trim(text, Character::isWhitespace)) {
                case Maybe.Some(var rest) -> {
                    text = rest;
                    continue;
                }
                default -> {
                }
            }
            // captures the state of text before it is potentially modified by
            // recognizing tokens
            String textOrig = text;

            // (updated-text-with-match-elided , just-matched-tokenkind)
            Pair<String, TokenKind> p = matchNext(textOrig, punctuation, punctuationKinds);
            text = p.first();
            TokenKind kind = p.second();
            // assert invariant: text.length() < textOrig.length()

            String tokenText = textOrig.substring(0, textOrig.length() - text.length());

            // see if the string we just matched is actually a keyword (not just a user defined id/name)
            // if we leave this loop we assume the kind will stay just general purpose Name
            if (kind == TokenKind.Name) {
                for (int i = 0; i < keywords.length; i++) {
                    if (tokenText.equals(keywords[i])) {
                        kind = keywordKinds[i];
                        break;
                    }
                }
            }
            result = result.append(new Token(kind, tokenText));
        }

        return result;
    }

    private static Pair<String, TokenKind> matchNext(String text, String[] punctuationSymbols,
                                                     TokenKind[] punctuationKinds) {

        for (int i = 0; i < punctuationSymbols.length; i++) {
            switch (stripPrefix(text, punctuationSymbols[i])) {
                case Maybe.Some(var rest) -> {
                    return new Pair<>(rest, punctuationKinds[i]);
                }
                default -> {
                }
            }
        }

        // try to match an int digit
        switch (trim(text, Character::isDigit)) {
            case Maybe.Some(var rest) -> {
                return new Pair<>(rest, TokenKind.Int);
            }
            default -> {
            }
        }

        // try to match an alphanumeric identifier including _ (no whitespace) ...
        // note: if it were just a number like 909 it would've matched in the
        // above int switch case and returned already
        switch (trim(text, Lexer::isNameChar)) {
            case Maybe.Some(var rest) -> {
                return new Pair<>(rest, TokenKind.Name);
            }
            default -> {
            }
        }
        int errorIndex = text.indexOf(' ');
        if (errorIndex == -1) {
            errorIndex = text.length();
        }
        text = text.substring(errorIndex);
        return new Pair<>(text, TokenKind.ErrorToken);
    }

    private static boolean isNameChar(char c) {
        return switch (c) {
            case '_' -> true;
            default -> (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9');
        };
    }

    private static Maybe<String> stripPrefix(String text, String prefix) {
        if (text.startsWith(prefix)) {
            return Maybe.of(text.substring(prefix.length()));
        }
        return Maybe.none();
    }

    private static Maybe<String> trim(String s, Predicate<Character> predicate) {
        int index = 0;
        while (index < s.length() && predicate.test(s.charAt(index))) {
            index++;
        }
        if (index == 0) {
            return Maybe.none();
        } else {
            return Maybe.of(s.substring(index));
        }
    }
}
