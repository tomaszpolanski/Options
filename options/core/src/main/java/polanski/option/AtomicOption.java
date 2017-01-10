package polanski.option;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

import static polanski.option.Option.ofObj;

/**
 * Atomic version of {@link Option}, can be used as final fields in classes.
 *
 * @param <T> Inner type of Option
 */
public final class AtomicOption<T> extends AtomicReference<Option<T>> {

    /**
     * Constructor, the inner value will be set to {@link Option#NONE}.
     */
    public AtomicOption() {
        super(Option.<T>none());
    }

    /**
     * Constructor, if the value is not null, then it will be set,
     * otherwise it will be set to {@link Option#NONE}.
     *
     * @param value Value to be set to the AtomicOption
     */
    public AtomicOption(@Nullable final T value) {
        super(ofObj(value));
    }

    /**
     * Atomically sets the value to None returns the old value.
     *
     * @return the previous value
     */
    @NotNull
    public Option<T> getAndClear() {
        return getAndSet(Option.<T>none());
    }

    /**
     * Replaces the previous value if it was {@link Option#NONE}.
     *
     * @param value Value to replace {@link Option#NONE}
     * @return True if the value was replaces, otherwise false
     */
    public boolean setIfNone(@Nullable final T value) {
        return compareAndSet(Option.<T>none(), ofObj(value));
    }
}
