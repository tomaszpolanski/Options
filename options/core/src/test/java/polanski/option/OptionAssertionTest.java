package polanski.option;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import polanski.option.function.Func1;

import static org.hamcrest.CoreMatchers.startsWith;

public class OptionAssertionTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    // assertIsNone

    @Test
    public void assertIsNone_doesNotThrowAssertionError_whenNone() {
        new OptionAssertion<Object>(Option.none()).assertIsNone();
    }

    @Test
    public void assertIsNone_throwAssertionError_whenSome() {
        thrown.expect(AssertionError.class);
        thrown.expectMessage(startsWith("Option was not None"));

        new OptionAssertion<String>(Option.ofObj("value")).assertIsNone();
    }

    // assertIsSome

    @Test
    public void assertIsSome_doesNotThrowAssertionError_whenSome() {
        new OptionAssertion<String>(Option.ofObj("value")).assertIsSome();
    }

    @Test
    public void assertIsSome_throwsAssertionError_whenNone() {
        thrown.expect(AssertionError.class);
        thrown.expectMessage(startsWith("Option was not Some"));

        new OptionAssertion<Object>(Option.none()).assertIsSome();
    }

    // assertValue (predicate)

    @Test
    public void assertValue_doesNotThrowAssertionError_whenPredicateTrue() {
        final String expected = "value";
        new OptionAssertion<String>(Option.ofObj("value"))
                .assertValue(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(final String actualValue) {
                        return actualValue.equals(expected);
                    }
                });
    }

    @Test
    public void assertValue_throwsAssertionError_whenPredicateFalse() {
        thrown.expect(AssertionError.class);
        thrown.expectMessage(startsWith("Actual Option value: <value> did not match predicate"));

        new OptionAssertion<String>(Option.ofObj("value"))
                .assertValue(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(final String actualValue) {
                        return actualValue.equals("different");
                    }
                });
    }

    @Test
    public void assertValue_throwsAssertionError_whenOptionNone() {
        thrown.expect(AssertionError.class);
        thrown.expectMessage(startsWith("Option was not Some"));

        new OptionAssertion<Object>(Option.none())
                .assertValue(new Func1<Object, Boolean>() {
                    @Override
                    public Boolean call(final Object actualValue) {
                        return actualValue.equals("expected");
                    }
                });
    }

    // assertValue (equals)

    @Test
    public void assertValue_doesNotThrowAssertionError_whenEqualTo() {
        String actual = "value";
        String expected = "value";
        new OptionAssertion<String>(Option.ofObj(actual))
                .assertValue(expected);
    }

    @Test
    public void assertValue_throwsAssertionError_whenNotEqualTo() {
        thrown.expect(AssertionError.class);
        thrown.expectMessage(
                startsWith(
                        "Actual Option value: <actual> did not equal expected value: <expected>"));

        String actual = "actual";

        new OptionAssertion<String>(Option.ofObj(actual))
                .assertValue("expected");
    }

    @Test
    public void assertValue_throwsAssertionError_whenOptionNone_notEqualTo() {
        thrown.expect(AssertionError.class);
        thrown.expectMessage(startsWith("Option was not Some"));

        new OptionAssertion<Object>(Option.none())
                .assertValue("expected");
    }

    // Preconditions

    @Test
    public void constructor_prohibitNull() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(startsWith("Option cannot be null"));

        //noinspection ConstantConditions
        new OptionAssertion<Object>(null);
    }

    @Test
    public void assertValue_prohibitsNullValue() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(startsWith("Expected value cannot be null: use assertNone instead"));

        //noinspection ConstantConditions
        new OptionAssertion<Object>(Option.none())
                .assertValue((Object) null);
    }

    @Test
    public void assertValue_prohibitsNullPredicateFunction() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(startsWith("Predicate function cannot be null"));

        //noinspection ConstantConditions
        new OptionAssertion<Object>(Option.none())
                .assertValue(null);
    }

}
