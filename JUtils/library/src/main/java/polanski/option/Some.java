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
        //noinspection unchecked
        return predicate.call(mValue) ? this : NONE;
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
        //noinspection unchecked
        return type.isInstance(mValue) ? ofObj(type.cast(mValue)) : NONE;
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
        return polanski.option.Unit.from(new Action0() {
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
