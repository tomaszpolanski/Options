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
    public boolean isNone() {
        return true;
    }

    @Override
    public Option<T> ifSome(@NotNull final Action1<T> action) {
        // Do nothing
        return this;
    }

    @Override
    public Option<T> ifNone(@NotNull final Action0 action) {
        action.call();
        return this;
    }

    @NotNull
    @Override
    public <OUT> Option<OUT> map(@NotNull final Func1<T, OUT> f) {
        return none();
    }

    @NotNull
    @Override
    public <OUT> Option<OUT> flatMap(@NotNull final Func1<T, Option<OUT>> f) {
        return none();
    }

    @NotNull
    @Override
    public Option<T> filter(@NotNull final Func1<T, Boolean> predicate) {
        return none();
    }

    @NotNull
    @Override
    public Option<T> orOption(@NotNull final Func0<Option<T>> f) {
        return f.call();
    }

    @NotNull
    @Override
    public T orDefault(@NotNull final Func0<T> def) {
        return def.call();
    }

    @NotNull
    @Override
    T getUnsafe() {
        throw new IllegalStateException();
    }

    @NotNull
    @Override
    public <OUT> Option<OUT> ofType(@NotNull Class<OUT> type) {
        return none();
    }

    @NotNull
    @Override
    public <OUT> OUT match(@NotNull Func1<T, OUT> fSome,
                           @NotNull Func0<OUT> fNone) {
        return fNone.call();
    }

    @NotNull
    @Override
    public polanski.option.Unit matchAction(@NotNull Action1<T> fSome, @NotNull Action0 fNone) {
        return polanski.option.Unit.from(fNone);
    }

    @Nullable
    @Override
    public <OUT> OUT matchUnsafe(@NotNull Func1<T, OUT> fSome, @NotNull Func0<OUT> fNone) {
        return fNone.call();
    }

    @NotNull
    @Override
    public <IN, OUT> Option<OUT> lift(@NotNull final Option<IN> optionB,
                                      @NotNull final Func2<T, IN, OUT> f) {
        return none();
    }

    @NotNull
    @Override
    public <IN1, IN2, OUT> Option<OUT> lift(@NotNull Option<IN1> option1,
                                            @NotNull Option<IN2> option2,
                                            @NotNull Func3<T, IN1, IN2, OUT> f) {
        return none();
    }

    @NotNull
    @Override
    public <IN1, IN2, IN3, OUT> Option<OUT> lift(@NotNull Option<IN1> option1,
                                                 @NotNull Option<IN2> option2,
                                                 @NotNull Option<IN3> option3,
                                                 @NotNull Func4<T, IN1, IN2, IN3, OUT> f) {
        return none();
    }

    @NotNull
    @Override
    public <IN, OUT> Option<OUT> lift(@NotNull final List<Option<IN>> options,
                                      @NotNull final FuncN<OUT> f) {
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
