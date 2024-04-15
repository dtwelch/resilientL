package org.rsrg.resilientll.util;

// todo: prefer this to vavr's option for pattern matching purposes..
//       will need to come up with some adaptor/utility fns for converting
//       between vavr option and this type (since other ADTs that are part of
//       vavr will still return the vavr.Option)

import io.vavr.control.Option;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A maybe/option type modeled after the one found in haskell.
 * <p>
 * We prefer this maybe class over vavr's {@link Option} as it
 * takes advantage of & provides clients the opportunity to use certain newer
 * parts of java (such as sealed interfaces, records, and pattern-matching).
 * <p>
 * Many of the vavr collections and apis make extensive use of Option. So this
 * sealed type functions then as a (hopefully temporary) 'real' option type
 * that supports pattern matching.
 * <p>
 * Users can call {@link Maybe#fromOpt(Option)} to convert a vavr option
 * instance to an equivalent {@link Maybe} instance.
 * <p>
 * Not sure what the future of the vavr library is (as of June 4, 2023) but it
 * doesn't seem to have an owner/maintainer currently..
 */
public sealed interface Maybe<A> {

    static <U> Maybe<U> fromOpt(Option<U> vavrOpt) {
        return vavrOpt.isDefined() ? of(vavrOpt.get()) : none();
    }

    // factory methods
    static <T> Maybe<T> of(T value) {
        return (value == null) ? none() : new Some<>(value);
    }

    @SuppressWarnings("unchecked") static <T> Maybe<T> none() {
        return (None<T>) None.INSTANCE;
    }

    default A getOrElse(A other) {
        return isEmpty() ? other : get();
    }

    default A getOrElse(Supplier<A> supplier) {
        return isEmpty() ? supplier.get() : get();
    }

    A get();

    default boolean isEmpty() {
        return this instanceof Maybe.None<A>;
    }

    default boolean nonEmpty() {
        return this instanceof Maybe.Some<A>;
    }

    default boolean isDefined() {
        return nonEmpty();
    }

    default boolean contains(A item) {
        return switch (this) {
            case Some(var x) -> x.equals(item);
            case None<A> _ -> false;
        };
    }

    final class None<A> implements Maybe<A> {
        public static final None<?> INSTANCE = new None<>();

        private None() {}

        @Override public A get() {
            throw new NoSuchElementException("option is empty");
        }

        @Override public boolean equals(Object o) {
            return o == this;
        }

        @Override public int hashCode() {
            return 1;
        }
    }

    record Some<A>(A value) implements Maybe<A> {
        @Override public A get() {
            return value;
        }

        @Override public boolean equals(Object o) {
            return o instanceof Maybe.Some<?> om &&
                    this.value.equals(om.value);
        }

        @Override public int hashCode() {
            return Objects.hashCode(value);
        }
    }

    // remember: fmap is functorMap (not flatMap -- which would return an obj)
    // really: since we're in java, would be better to probably just rename
    // this 'map' as we're not/can't model functors realistically here`
    default <B> Maybe<B> map(Function<A, B> f) {
        Objects.requireNonNull(f, "fn is null");

        return switch (this) {
            case Maybe.None<?> _ -> none();
            case Some(var x) -> new Some<>(f.apply(x));
        };
    }
}