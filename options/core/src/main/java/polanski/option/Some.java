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

import static polanski.option.Unit.from;

/**
 * Represent option of existing value.
 *
 * @param <T> type of object that exists
 */
public final class Some<T> extends Option<T> {

    @NotNull
    private final T mValue;

    Some(@NotNull final T value) {
        mValue = value;
    }

    @Override
    public boolean isSome() {
        return true;
    }

    @Override
    public boolean isNone() {
        return false;
    }

    @Override
    public Option<T> ifSome(@NotNull final Action1<T> action) {
        action.call(mValue);
        return this;
    }

    @Override
    public Option<T> ifNone(@NotNull final Action0 action) {
        // Do nothing
        return this;
    }

    @NotNull
    @Override
    public <OUT> Option<OUT> map(@NotNull final Func1<T, OUT> f) {
        return ofObj(f.call(mValue));
    }

    @NotNull
    @Override
    public <OUT> Option<OUT> flatMap(@NotNull final Func1<T, Option<OUT>> f) {
        return f.call(mValue);
    }

    @NotNull
    @Override
    public Option<T> filter(@NotNull final Func1<T, Boolean> predicate) {
        return predicate.call(mValue) ? this : Option.<T>none();
    }

    @NotNull
    @Override
    public Option<T> orOption(@NotNull final Func0<Option<T>> f) {
        return this;
    }

    @NotNull
    @Override
    public T orDefault(@NotNull final Func0<T> def) {
        return mValue;
    }

    @NotNull
    @Override
    T getUnsafe() {
        return mValue;
    }

    @NotNull
    @Override
    public <OUT> Option<OUT> ofType(@NotNull final Class<OUT> type) {
        return type.isInstance(mValue) ? ofObj(type.cast(mValue)) : Option.<OUT>none();
    }

    @NotNull
    @Override
    public <OUT> OUT match(@NotNull final Func1<T, OUT> fSome,
                           @NotNull final Func0<OUT> fNone) {
        return fSome.call(mValue);
    }

    @NotNull
    @Override
    public polanski.option.Unit matchAction(@NotNull final Action1<T> fSome,
                                            @NotNull final Action0 fNone) {
        return from(new Action0() {
            @Override
            public void call() {
                fSome.call(mValue);
            }
        });
    }

    @Nullable
    @Override
    public <OUT> OUT matchUnsafe(@NotNull Func1<T, OUT> fSome, @NotNull Func0<OUT> fNone) {
        return fSome.call(mValue);
    }

    @NotNull
    @Override
    public <IN, OUT2> Option<OUT2> lift(@NotNull final Option<IN> option,
                                        @NotNull final Func2<T, IN, OUT2> f) {
        return option.map(new Func1<IN, OUT2>() {
            @Override
            public OUT2 call(final IN b) {
                return f.call(mValue, b);
            }
        });
    }

    @NotNull
    @Override
    public <IN1, IN2, OUT> Option<OUT> lift(@NotNull final Option<IN1> option1,
                                            @NotNull final Option<IN2> option2,
                                            @NotNull final Func3<T, IN1, IN2, OUT> f) {
        return option1.lift(option2, new Func2<IN1, IN2, OUT>() {
            @Override
            public OUT call(final IN1 o1, final IN2 o2) {
                return f.call(mValue, o1, o2);
            }
        });
    }

    @NotNull
    @Override
    public <IN1, IN2, IN3, OUT> Option<OUT> lift(@NotNull final Option<IN1> option1,
                                                 @NotNull final Option<IN2> option2,
                                                 @NotNull final Option<IN3> option3,
                                                 @NotNull final Func4<T, IN1, IN2, IN3, OUT> f) {
        return option1.lift(option2, option3, new Func3<IN1, IN2, IN3, OUT>() {
            @Override
            public OUT call(final IN1 o1, final IN2 o2, final IN3 o3) {
                return f.call(mValue, o1, o2, o3);
            }
        });
    }

    @NotNull
    @Override
    public <IN, OUT> Option<OUT> lift(@NotNull final List<Option<IN>> options,
                                      @NotNull final FuncN<OUT> f) {

        return options.size() == 1
                ? first(options).map(new Func1<IN, OUT>() {
            @Override
            public OUT call(final IN it) {
                return f.call(it, mValue);
            }
        })
                : first(options).lift(tail(options), new FuncN<OUT>() {
            @Override
            public OUT call(final Object... list) {
                return f.call(combine(mValue, list));
            }
        });
    }

    @NotNull
    private static <T> T first(@NotNull final List<T> options) {
        return options.get(0);
    }

    @NotNull
    private static <T> List<T> tail(@NotNull final List<T> options) {
        return options.subList(1, options.size());
    }

    @NotNull
    private static Object[] combine(@NotNull final Object[] a, @NotNull final Object[] b) {
        final int length = a.length + b.length;
        Object[] result = new Object[length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    @NotNull
    private static Object[] combine(@NotNull final Object a, @NotNull final Object[] b) {
        return combine(new Object[]{a}, b);
    }

    @Override
    public int hashCode() {
        return mValue.hashCode();
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(final Object o) {
        return ofObj(o)
                .ofType(Some.class)
                .filter(new Func1<Some, Boolean>() {
                    @Override
                    public Boolean call(final Some some) {
                        return some.getUnsafe().equals(mValue);
                    }
                }) != NONE;
    }

    @Override
    public String toString() {
        return mValue.toString();
    }
}
