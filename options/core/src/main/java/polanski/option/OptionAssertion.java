package polanski.option;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import polanski.option.function.Func1;

/**
 * Assertions to test the nature of a wrapped {@link Option}.
 */
public final class OptionAssertion<T> {

    @NotNull
    private final Option<T> actual;

    OptionAssertion(@NotNull final Option<T> actual) {
        checkNotNull(actual, "Option cannot be null");

        this.actual = actual;
    }

    /**
     * Asserts that the {@link Option} is {@link Option#NONE}.
     */
    public void assertIsNone() {
        if (!actual.isNone()) {
            throw fail("Option was not None");
        }
    }

    /**
     * Asserts that the {@link Option} is {@link Some}.
     *
     * @return this.
     */
    @NotNull
    public OptionAssertion<T> assertIsSome() {
        if (!actual.isSome()) {
            throw fail("Option was not Some");
        }
        return this;
    }

    /**
     * Asserts that the {@link Option} is {@link Some} and that the wrapped value is equal to the
     * given value. Equality is determined using {@link Objects#equals(Object, Object)}.
     *
     * @param expected The expected value. May not be null.
     * @return this.
     */
    @NotNull
    public OptionAssertion<T> assertValue(@NotNull final T expected) {
        checkNotNull(expected, "Expected value cannot be null: use assertNone instead");

        if (!actual.isSome()) {
            throw fail("Option was not Some");
        }

        if (!matches(actual, equalsPredicate(expected))) {
            throw fail(String.format("Actual Option value: <%s> did not equal expected value: <%s>",
                                     OptionUnsafe.getUnsafe(actual),
                                     expected));
        }
        return this;
    }

    /**
     * Asserts that the {@link Option} is {@link Some} and that the wrapped value matches given
     * predicate function.
     *
     * @param predicate The predicate function. May not be null.
     * @return this.
     */
    @NotNull
    public OptionAssertion<T> assertValue(@NotNull final Func1<T, Boolean> predicate) {
        checkNotNull(predicate, "Predicate function cannot be null");

        if (!actual.isSome()) {
            throw fail("Option was not Some");
        }

        if (!matches(actual, predicate)) {
            throw fail(String.format("Actual Option value: <%s> did not match predicate", actual));
        }
        return this;
    }

    @NotNull
    private static <T> Func1<T, Boolean> equalsPredicate(@NotNull final T expected) {
        return new Func1<T, Boolean>() {
            @Override
            public Boolean call(final T t) {
                return Objects.equals(t, expected);
            }
        };
    }

    private static <T> boolean matches(@NotNull final Option<T> actual,
                                       @NotNull final Func1<T, Boolean> predicate) {
        return actual.filter(predicate).isSome();
    }

    @NotNull
    private AssertionError fail(@Nullable final String message) {
        StringBuilder b = new StringBuilder();
        b.append(message);

        b.append(" (")
         .append("Actual = ").append(actual.toString())
         .append(')');

        return new AssertionError(b.toString());
    }

    private static <T> void checkNotNull(@Nullable final T value,
                                         @NotNull final String msg) {
        if (value == null) {
            throw new IllegalArgumentException(msg);
        }
    }
}
