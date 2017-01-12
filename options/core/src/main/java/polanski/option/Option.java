package polanski.option;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import polanski.option.function.Action0;
import polanski.option.function.Action1;
import polanski.option.function.Func0;
import polanski.option.function.Func1;
import polanski.option.function.Func2;
import polanski.option.function.Func3;
import polanski.option.function.Func4;
import polanski.option.function.FuncN;

/**
 * Represent possibility of value not existing,
 * which needs to be unwrapped before using
 *
 * @param <T> type of object that could possibly be missing
 */
public abstract class Option<T> {

    /**
     * Representation of non existing value
     */
    @NotNull
    public static final None NONE = new None();

    /**
     * Returns of non existing value without getting unchecked warning
     *
     * @param <T> Type wrapped in {@link Option}
     * @return NONE
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public static <T> Option<T> none() {
        return NONE;
    }

    /**
     * Indicates if option contains value
     *
     * @return true if Option is Some, otherwise false
     */
    public abstract boolean isSome();

    /**
     * Indicates if option does not contain a value
     *
     * @return true if Option is None, otherwise false
     */
    public abstract boolean isNone();

    /**
     * Runs the action on Option value if exists, otherwise does nothing
     *
     * @param action Action that is called on the inner value
     * @return this {@link Option}
     */
    public abstract Option<T> ifSome(@NotNull final Action1<T> action);

    /**
     * Runs the action on Option value if does not exist, otherwise does nothing
     *
     * @param action Action that is called
     * @return this {@link Option}
     */
    public abstract Option<T> ifNone(@NotNull final Action0 action);

    /**
     * Converts inner value with @selector if value exists, otherwise does nothing
     *
     * @param selector Function that converts inner value
     * @param <OUT>    Result type
     * @return If value exists, returns converted value otherwise does nothing
     */
    @NotNull
    public abstract <OUT> Option<OUT> map(@NotNull final Func1<T, OUT> selector);

    /**
     * Binds option to another option
     *
     * @param selector Function that returns option to be bound to
     * @param <OUT>    Result type
     * @return Bound option
     */
    @NotNull
    public abstract <OUT> Option<OUT> flatMap(@NotNull final Func1<T, Option<OUT>> selector);

    /**
     * Filters options fulfilling given @predicate
     *
     * @param predicate Function returning true if the parameter should be included
     * @return Some if the value checks the condition, otherwise None
     */
    @NotNull
    public abstract Option<T> filter(@NotNull final Func1<T, Boolean> predicate);

    /**
     * Returns option if current value is None
     *
     * @param f Function returning new Option
     * @return Option given by the function if current is None, otherwise returns current one
     */
    @NotNull
    public abstract Option<T> orOption(@NotNull final Func0<Option<T>> f);

    /**
     * Returns current inner value if it exists, otherwise the value supplied by @def
     *
     * @param def Function that returns default value
     * @return If value exists, then returns it, otherwise the default
     */
    @NotNull
    public abstract T orDefault(@NotNull final Func0<T> def);

    /**
     * Forcefully tries to unwrap the inner value.
     * <p/>
     * Caution! Use this value only in special, justified cases!
     * Use @match instead.
     *
     * @return Value if exists, otherwise throws exception that shouldn't be caught
     */
    @NotNull
    abstract T getUnsafe();

    /**
     * Casts the inner value to given type
     *
     * @param type  Class of the new object
     * @param <OUT> Type the value should be cast to
     * @return Option of inner value cast to the OUT, if not possible, then None
     */
    @NotNull
    public abstract <OUT> Option<OUT> ofType(@NotNull final Class<OUT> type);

    /**
     * Option created from given @value
     *
     * @param value Value that should be wrapped in an Option
     * @param <IN>  Input type
     * @return Some of the @value if it is not null, otherwise None
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public static <IN> Option<IN> ofObj(@Nullable final IN value) {
        return value == null ? Option.NONE : new Some(value);
    }

    /**
     * Option of value returned by the function
     *
     * @param f     Function that returns a value, that function could throw an exception
     * @param <OUT> Result type
     * @return Option of a value returned by @f, if @f threw an exception, then returns None
     */
    @NotNull
    public static <OUT> Option<OUT> tryAsOption(@NotNull final Func0<OUT> f) {
        try {
            return Option.ofObj(f.call());
        } catch (Exception e) {
            return none();
        }
    }

    /**
     * Matches current optional to Some or None and returns appropriate value
     *
     * @param fSome Function that will be called if value exists
     * @param fNone Function that will be called if value does not exist
     * @param <OUT> Result type
     * @return Value returned by either @fSome of @fNone
     */
    @NotNull
    public abstract <OUT> OUT match(@NotNull final Func1<T, OUT> fSome,
                                    @NotNull final Func0<OUT> fNone);

    /**
     * Matches current option to Some or None and returns unit
     *
     * @param fSome Action that will be called if value exists
     * @param fNone Action that will be called if value does not exist
     * @return Unit
     */
    @NotNull
    public abstract Unit matchAction(@NotNull final Action1<T> fSome,
                                     @NotNull final Action0 fNone);

    /**
     * Matches current optional to Some orResult None and returns appropriate value
     *
     * @param fSome Function that will be called if value exists
     * @param fNone Function that will be called if value does not exist
     * @param <OUT> Result type
     * @return Value returned by either @fSome of @fNone
     */
    @Nullable
    public abstract <OUT> OUT matchUnsafe(@NotNull final Func1<T, OUT> fSome,
                                          @NotNull final Func0<OUT> fNone);

    /**
     * Identity function
     *
     * @return Current option
     */
    @NotNull
    public Option<T> id() {
        return this;
    }

    /**
     * Combines given Options using @f
     *
     * @param option1 Option that should be combined with current option
     * @param f       Function that combines all inner values of the options into one value
     * @param <IN1>   Input type
     * @param <OUT>   Result type
     * @return Option of some if all the Options were Some, otherwise None
     */
    @NotNull
    public abstract <IN1, OUT> Option<OUT> lift(@NotNull final Option<IN1> option1,
                                                @NotNull final Func2<T, IN1, OUT> f);

    /**
     * Combines given Options using @f
     *
     * @param option1 Option that should be combined with current option
     * @param option2 Option that should be combined with current option
     * @param f       Function that combines all inner values of the options into one value
     * @param <IN1>   Input type
     * @param <IN2>   Input type
     * @param <OUT>   Result type
     * @return Option of some if all the Options were Some, otherwise None
     */
    @NotNull
    public abstract <IN1, IN2, OUT> Option<OUT> lift(@NotNull final Option<IN1> option1,
                                                     @NotNull final Option<IN2> option2,
                                                     @NotNull final Func3<T, IN1, IN2, OUT> f);

    /**
     * Combines given Options using @f
     *
     * @param option1 Option that should be combined with current option
     * @param option2 Option that should be combined with current option
     * @param option3 Option that should be combined with current option
     * @param f       Function that combines all inner values of the options into one value
     * @param <IN1>   Input type
     * @param <IN2>   Input type
     * @param <IN3>   Input type
     * @param <OUT>   Result type
     * @return Option of some if all the Options were Some, otherwise None
     */
    @NotNull
    public abstract <IN1, IN2, IN3, OUT> Option<OUT> lift(@NotNull final Option<IN1> option1,
                                                          @NotNull final Option<IN2> option2,
                                                          @NotNull final Option<IN3> option3,
                                                          @NotNull final Func4<T, IN1, IN2, IN3, OUT> f);

    /**
     * Combines given Options using @f.
     *
     * @param options Options that should be combined with current option
     * @param f       Function that combines all inner values of the options into one value
     * @param <IN>    Input type
     * @param <OUT>   Result type
     * @return Option of some if all the Options were Some, otherwise None
     */
    @NotNull
    public abstract <IN, OUT> Option<OUT> lift(
            @NotNull final List<Option<IN>> options,
            @NotNull final FuncN<OUT> f);

    /**
     * Logs the value of the Option via given logging function.
     *
     * @param logging Logging function
     * @return Unchanged option
     */
    @NotNull
    public Option<T> log(@NotNull final Action1<String> logging) {
        return log("", logging);
    }

    /**
     * Logs the value of the Option via given logging function.
     *
     * @param tag     Text to be prepended to the logging
     * @param logging Logging function
     * @return Unchanged option
     */
    @NotNull
    public Option<T> log(@NotNull String tag,
                         @NotNull final Action1<String> logging) {
        logging.call(tag.isEmpty()
                ? this.toString()
                : String.format("%s: %s", tag, this));
        return this;
    }

    /**
     * Creates a {@link OptionAssertion} from this Option to provide set of assertions for testing.
     *
     * @return the new {@link OptionAssertion} instance.
     */
    @NotNull
    public OptionAssertion<T> test() {
        return new OptionAssertion<T>(this);
    }

}

