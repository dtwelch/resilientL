package org.rsrg.resilientll.util;

import io.vavr.control.Option;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A maybe/option type modeled after the one found in haskell. Preferred this
 * one to vavr's due to use of records and the ability to deconstruct in match
 * expressions.
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

    default <B> Maybe<B> map(Function<A, B> f) {
        Objects.requireNonNull(f, "fn is null");

        return switch (this) {
            case Maybe.None<?> _ -> none();
            case Some(var x) -> new Some<>(f.apply(x));
        };
    }
}