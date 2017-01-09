package polanski.option;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.startsWith;

public class OptionAssertionTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    // assertIsNone

    @Test
    public void assertIsNone_doesNotThrowAssertionError_whenNone() {
        new OptionAssertion<>(Option.none()).assertIsNone();
    }

    @Test
    public void assertIsNone_throwAssertionError_whenSome() {
        thrown.expect(AssertionError.class);
        thrown.expectMessage(startsWith("Option was not None"));

        new OptionAssertion<>(Option.ofObj("value")).assertIsNone();
    }

    // assertIsSome

    @Test
    public void assertIsSome_doesNotThrowAssertionError_whenSome() {
        new OptionAssertion<>(Option.ofObj("value")).assertIsSome();
    }

    @Test
    public void assertIsSome_throwsAssertionError_whenNone() {
        thrown.expect(AssertionError.class);
        thrown.expectMessage(startsWith("Option was not Some"));

        new OptionAssertion<>(Option.none()).assertIsSome();
    }

    // assertValue (predicate)

    @Test
    public void assertValue_doesNotThrowAssertionError_whenPredicateTrue() {
        String actual = "value";
        String expected = "value";
        new OptionAssertion<>(Option.ofObj(actual))
                .assertValue(actualValue -> actualValue.equals(expected));
    }

    @Test
    public void assertValue_throwsAssertionError_whenPredicateFalse() {
        thrown.expect(AssertionError.class);
        thrown.expectMessage(startsWith("Option value did not match predicate"));

        String actual = "value";
        new OptionAssertion<>(Option.ofObj(actual))
                .assertValue(value -> value.equals("different"));
    }

    @Test
    public void assertValue_throwsAssertionError_whenOptionNone() {
        thrown.expect(AssertionError.class);
        thrown.expectMessage(startsWith("Option was not Some"));

        new OptionAssertion<>(Option.none())
                .assertValue(actual -> actual.equals("expected"));
    }

    // assertValue (equals)

    @Test
    public void assertValue_doesNotThrowAssertionError_whenEqualTo() {
        String actual = "value";
        String expected = "value";
        new OptionAssertion<>(Option.ofObj(actual))
                .assertValue(expected);
    }

    @Test
    public void assertValue_throwsAssertionError_whenNotEqualTo() {
        thrown.expect(AssertionError.class);
        thrown.expectMessage(
                startsWith("Option value: <actual> did not equal expected value: <expected>"));

        String actual = "actual";

        new OptionAssertion<>(Option.ofObj(actual))
                .assertValue("expected");
    }

    @Test
    public void assertValue_throwsAssertionError_whenOptionNone_notEqualTo() {
        thrown.expect(AssertionError.class);
        thrown.expectMessage(startsWith("Option was not Some"));

        new OptionAssertion<>(Option.none())
                .assertValue("expected");
    }

}
