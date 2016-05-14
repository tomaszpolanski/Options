package polanski.option;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.functions.Func4;

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
    @NonNull
    public static final None NONE = new None();

    /**
     * Returns of non existing value without getting unchecked warning
     */
    @SuppressWarnings("unchecked")
    @NonNull
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
     * Runs the action on Option value if exists, otherwise does nothing
     *
     * @param action Action that is called on the inner value
     * @return this {@link Option}
     */
    public abstract Option<T> ifSome(@NonNull final Action1<T> action);

    /**
     * Runs the action on Option value if does not exist, otherwise does nothing
     *
     * @param action Action that is called
     * @return this {@link Option}
     */
    public abstract Option<T> ifNone(@NonNull final Action0 action);

    /**
     * Converts inner value with @selector if value exists, otherwise does nothing
     *
     * @param selector Function that converts inner value
     * @return If value exists, returns converted value otherwise does nothing
     */
    @NonNull
    public abstract <OUT> Option<OUT> map(@NonNull final Func1<T, OUT> selector);

    /**
     * Binds option to another option
     *
     * @param selector Function that returns option to be bound to
     * @return Bound option
     */
    @NonNull
    public abstract <OUT> Option<OUT> flatMap(@NonNull final Func1<T, Option<OUT>> selector);

    /**
     * Filters options fulfilling given @predicate
     *
     * @param predicate Function returning true if the parameter should be included
     * @return Some if the value checks the condition, otherwise None
     */
    @NonNull
    public abstract Option<T> filter(@NonNull final Func1<T, Boolean> predicate);

    /**
     * Returns option if current value is None
     *
     * @param f Function returning new Option
     * @return Option given by the function if current is None, otherwise returns current one
     */
    @NonNull
    public abstract Option<T> orOption(@NonNull final Func0<Option<T>> f);

    /**
     * Returns current inner value if it exists, otherwise the value supplied by @def
     *
     * @param def Function that returns default value
     * @return If value exists, then returns it, otherwise the default
     */
    @NonNull
    public abstract T orDefault(@NonNull final Func0<T> def);

    /**
     * Forcefully tries to unwrap the inner value.
     * <p>
     * Caution! Use this value only in special, justified cases!
     * Use @match instead.
     *
     * @return Value if exists, otherwise throws exception that shouldn't be caught
     */
    @NonNull
    abstract T getUnsafe();

    /**
     * Casts the inner value to given type
     *
     * @param type  Class of the new object
     * @param <OUT> Type the value should be cast to
     * @return Option of inner value cast to the @<OUT>, if not possible, then None
     */
    @NonNull
    public abstract <OUT> Option<OUT> ofType(@NonNull final Class<OUT> type);

    /**
     * Option created from given @value
     *
     * @param value Value that should be wrapped in an Option
     * @return Some of the @value if it is not null, otherwise None
     */
    @SuppressWarnings("unchecked")
    @NonNull
    public static <IN> Option<IN> ofObj(@Nullable final IN value) {
        return value == null ? Option.NONE : new Some(value);
    }

    /**
     * Option of value returned by the function
     *
     * @param f Function that returns a value, that function could throw an exception
     * @return Option of a value returned by @f, if @f threw an exception, then returns None
     */
    @NonNull
    public static <OUT> Option<OUT> tryAsOption(@NonNull final Func0<OUT> f) {
        try {
            return Option.ofObj(f.call());
        } catch (Exception e) {
            //noinspection unchecked
            return NONE;
        }
    }

    /**
     * Matches current optional to Some or None and returns appropriate value
     *
     * @param fSome Function that will be called if value exists
     * @param fNone Function that will be called if value does not exist
     * @return Value returned by either @fSome of @fNone
     */
    @NonNull
    public abstract <OUT> OUT match(@NonNull final Func1<T, OUT> fSome,
                                    @NonNull final Func0<OUT> fNone);

    /**
     * Matches current option to Some or None and returns unit
     *
     * @param fSome Action that will be called if value exists
     * @param fNone Action that will be called if value does not exist
     * @return Unit
     */
    @NonNull
    public abstract Unit matchAction(@NonNull final Action1<T> fSome,
                                                     @NonNull final Action0 fNone);

    /**
     * Matches current optional to Some orResult None and returns appropriate value
     *
     * @param fSome Function that will be called if value exists
     * @param fNone Function that will be called if value does not exist
     * @return Value returned by either @fSome of @fNone
     */
    @Nullable
    public abstract <OUT> OUT matchUnsafe(@NonNull final Func1<T, OUT> fSome,
                                          @NonNull final Func0<OUT> fNone);

    /**
     * Identity function
     *
     * @return Current option
     */
    @NonNull
    public Option<T> id() {
        return this;
    }

    /**
     * Combines given Options using @f
     *
     * @param option1 Option that should be combined with current option
     * @param f       Function that combines all inner values of the options into one value
     * @return Option of some if all the Options were Some, otherwise None
     */
    @NonNull
    public abstract <IN1, OUT> Option<OUT> lift(@NonNull final Option<IN1> option1,
                                                @NonNull final Func2<T, IN1, OUT> f);

    /**
     * Combines given Options using @f
     *
     * @param option1 Option that should be combined with current option
     * @param option2 Option that should be combined with current option
     * @param f       Function that combines all inner values of the options into one value
     * @return Option of some if all the Options were Some, otherwise None
     */
    @NonNull
    public abstract <IN1, IN2, OUT> Option<OUT> lift(@NonNull final Option<IN1> option1,
                                                     @NonNull final Option<IN2> option2,
                                                     @NonNull final Func3<T, IN1, IN2, OUT> f);

    /**
     * Combines given Options using @f
     *
     * @param option1 Option that should be combined with current option
     * @param option2 Option that should be combined with current option
     * @param option3 Option that should be combined with current option
     * @param f       Function that combines all inner values of the options into one value
     * @return Option of some if all the Options were Some, otherwise None
     */
    @NonNull
    public abstract <IN1, IN2, IN3, OUT> Option<OUT> lift(@NonNull final Option<IN1> option1,
                                                          @NonNull final Option<IN2> option2,
                                                          @NonNull final Option<IN3> option3,
                                                          @NonNull final Func4<T, IN1, IN2, IN3, OUT> f);

    /**
     * Logs the value of the Option via given logging function.
     *
     * @param logging Logging function
     * @return Unchanged option
     */
    @NonNull
    public Option<T> log(@NonNull final Action1<String> logging) {
        return log("", logging);
    }

    /**
     * Logs the value of the Option via given logging function.
     *
     * @param tag     Text to be prepended to the logging
     * @param logging Logging function
     * @return Unchanged option
     */
    @NonNull
    public Option<T> log(@NonNull String tag,
                         @NonNull final Action1<String> logging) {
        logging.call(tag.isEmpty()
                             ? this.toString()
                             : String.format("%s: %s", tag, this));
        return this;
    }

}

