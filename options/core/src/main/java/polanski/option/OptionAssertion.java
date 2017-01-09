package polanski.option;

import android.support.annotation.NonNull;

import polanski.option.function.Func1;

public final class OptionAssertion<T> {

    @NonNull
    private final Option<T> actual;

    public OptionAssertion(@NonNull final Option<T> actual) {
        this.actual = actual;
    }

    public void assertIsNone() {
        if (!actual.isNone()) {
            fail("Option was not None");
        }
    }

    public OptionAssertion<T> assertIsSome() {
        if (!actual.isSome()) {
            fail("Option was not Some");
        }
        return this;
    }

    public OptionAssertion<T> assertValue(Func1<T, Boolean> predicate) {
        if (!actual.isSome()) {
            fail("Option was not Some");
        }

        if (!actual.filter(predicate).isSome()) {
            fail("Option value did not match predicate");
        }
        return this;
    }

    public OptionAssertion<T> assertValue(T expected) {
        if (!actual.isSome()) {
            fail("Option was not Some");
        }

        if (!actual.filter(v -> v.equals(expected)).isSome()) {
            fail(String.format("Option value <%s> did not equal expected value: <%s>",
                               OptionUnsafe.getUnsafe(actual),
                               expected));
        }
        return this;
    }

    private void fail(String message) {
        StringBuilder b = new StringBuilder();
        b.append(message);

        b.append(" (")
         .append("actual = ").append(actual.toString())
         .append(')');

        throw new AssertionError(b.toString());
    }
}
