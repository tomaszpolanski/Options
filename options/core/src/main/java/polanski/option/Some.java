package polanski.option;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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

    @NonNull
    private final T mValue;

    Some(@NonNull final T value) {
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
    public Option<T> ifSome(@NonNull final Action1<T> action) {
        action.call(mValue);
        return this;
    }

    @Override
    public Option<T> ifNone(@NonNull final Action0 action) {
        // Do nothing
        return this;
    }

    @NonNull
    @Override
    public <OUT> Option<OUT> map(@NonNull final Func1<T, OUT> f) {
        return ofObj(f.call(mValue));
    }

    @NonNull
    @Override
    public <OUT> Option<OUT> flatMap(@NonNull final Func1<T, Option<OUT>> f) {
        return f.call(mValue);
    }

    @NonNull
    @Override
    public Option<T> filter(@NonNull final Func1<T, Boolean> predicate) {
        return predicate.call(mValue) ? this : Option.<T>none();
    }

    @NonNull
    @Override
    public Option<T> orOption(@NonNull final Func0<Option<T>> f) {
        return this;
    }

    @NonNull
    @Override
    public T orDefault(@NonNull final Func0<T> def) {
        return mValue;
    }

    @NonNull
    @Override
    T getUnsafe() {
        return mValue;
    }

    @NonNull
    @Override
    public <OUT> Option<OUT> ofType(@NonNull final Class<OUT> type) {
        return type.isInstance(mValue) ? ofObj(type.cast(mValue)) : Option.<OUT>none();
    }

    @NonNull
    @Override
    public <OUT> OUT match(@NonNull final Func1<T, OUT> fSome,
                           @NonNull final Func0<OUT> fNone) {
        return fSome.call(mValue);
    }

    @NonNull
    @Override
    public polanski.option.Unit matchAction(@NonNull final Action1<T> fSome,
                                            @NonNull final Action0 fNone) {
        return from(new Action0() {
            @Override
            public void call() {
                fSome.call(mValue);
            }
        });
    }

    @Nullable
    @Override
    public <OUT> OUT matchUnsafe(@NonNull Func1<T, OUT> fSome, @NonNull Func0<OUT> fNone) {
        return fSome.call(mValue);
    }

    @NonNull
    @Override
    public <IN, OUT2> Option<OUT2> lift(@NonNull final Option<IN> option,
                                        @NonNull final Func2<T, IN, OUT2> f) {
        return option.map(new Func1<IN, OUT2>() {
            @Override
            public OUT2 call(final IN b) {
                return f.call(mValue, b);
            }
        });
    }

    @NonNull
    @Override
    public <IN1, IN2, OUT> Option<OUT> lift(@NonNull final Option<IN1> option1,
                                            @NonNull final Option<IN2> option2,
                                            @NonNull final Func3<T, IN1, IN2, OUT> f) {
        return option1.lift(option2, new Func2<IN1, IN2, OUT>() {
            @Override
            public OUT call(final IN1 o1, final IN2 o2) {
                return f.call(mValue, o1, o2);
            }
        });
    }

    @NonNull
    @Override
    public <IN1, IN2, IN3, OUT> Option<OUT> lift(@NonNull final Option<IN1> option1,
                                                 @NonNull final Option<IN2> option2,
                                                 @NonNull final Option<IN3> option3,
                                                 @NonNull final Func4<T, IN1, IN2, IN3, OUT> f) {
        return option1.lift(option2, option3, new Func3<IN1, IN2, IN3, OUT>() {
            @Override
            public OUT call(final IN1 o1, final IN2 o2, final IN3 o3) {
                return f.call(mValue, o1, o2, o3);
            }
        });
    }

    @NonNull
    @Override
    public <IN, OUT> Option<OUT> lift(@NonNull final List<Option<IN>> options,
                                      @NonNull final FuncN<OUT> f) {

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

    @NonNull
    private static <T> T first(@NonNull final List<T> options) {
        return options.get(0);
    }

    @NonNull
    private static <T> List<T> tail(@NonNull final List<T> options) {
        return options.subList(1, options.size());
    }

    @NonNull
    private static Object[] combine(@NonNull final Object[] a, @NonNull final Object[] b) {
        final int length = a.length + b.length;
        Object[] result = new Object[length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    @NonNull
    private static Object[] combine(@NonNull final Object a, @NonNull final Object[] b) {
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
