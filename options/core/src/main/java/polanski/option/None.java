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

/**
 * Represent missing value
 *
 * @param <T> Type of missing value
 */
public final class None<T> extends Option<T> {

    None() {
    }

    @Override
    public boolean isSome() {
        return false;
    }

    @Override
    public Option<T> ifSome(@NonNull final Action1<T> action) {
        // Do nothing
        return this;
    }

    @Override
    public Option<T> ifNone(@NonNull final Action0 action) {
        action.call();
        return this;
    }

    @NonNull
    @Override
    public <OUT> Option<OUT> map(@NonNull final Func1<T, OUT> f) {
        return none();
    }

    @NonNull
    @Override
    public <OUT> Option<OUT> flatMap(@NonNull final Func1<T, Option<OUT>> f) {
        return none();
    }

    @NonNull
    @Override
    public Option<T> filter(@NonNull final Func1<T, Boolean> predicate) {
        return none();
    }

    @NonNull
    @Override
    public Option<T> orOption(@NonNull final Func0<Option<T>> f) {
        return f.call();
    }

    @NonNull
    @Override
    public T orDefault(@NonNull final Func0<T> def) {
        return def.call();
    }

    @NonNull
    @Override
    T getUnsafe() {
        throw new IllegalStateException();
    }

    @NonNull
    @Override
    public <OUT> Option<OUT> ofType(@NonNull Class<OUT> type) {
        return none();
    }

    @NonNull
    @Override
    public <OUT> OUT match(@NonNull Func1<T, OUT> fSome,
                           @NonNull Func0<OUT> fNone) {
        return fNone.call();
    }

    @NonNull
    @Override
    public polanski.option.Unit matchAction(@NonNull Action1<T> fSome, @NonNull Action0 fNone) {
        return polanski.option.Unit.from(fNone);
    }

    @Nullable
    @Override
    public <OUT> OUT matchUnsafe(@NonNull Func1<T, OUT> fSome, @NonNull Func0<OUT> fNone) {
        return fNone.call();
    }

    @NonNull
    @Override
    public <IN, OUT> Option<OUT> lift(@NonNull final Option<IN> optionB,
                                      @NonNull final Func2<T, IN, OUT> f) {
        return none();
    }

    @NonNull
    @Override
    public <IN1, IN2, OUT> Option<OUT> lift(@NonNull Option<IN1> option1,
                                            @NonNull Option<IN2> option2,
                                            @NonNull Func3<T, IN1, IN2, OUT> f) {
        return none();
    }

    @NonNull
    @Override
    public <IN1, IN2, IN3, OUT> Option<OUT> lift(@NonNull Option<IN1> option1,
                                                 @NonNull Option<IN2> option2,
                                                 @NonNull Option<IN3> option3,
                                                 @NonNull Func4<T, IN1, IN2, IN3, OUT> f) {
        return none();
    }

    @NonNull
    @Override
    public <IN, OUT> Option<OUT> lift(@NonNull final List<? extends Option<IN>> options,
                                      @NonNull final FuncN<? extends OUT> f) {
        return none();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof None;
    }
}
