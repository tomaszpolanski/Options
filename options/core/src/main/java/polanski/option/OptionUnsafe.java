package polanski.option;

import android.support.annotation.NonNull;

/**
 * Helper class allowing unsafe operations on Option
 */
public final class OptionUnsafe {

    OptionUnsafe() {
        throw new AssertionError("Must not create an instance");
    }

    /**
     * ATTENTION: Only use it when you know what you are doing!
     *
     * Returns inner value of option if it is Some, otherwise will throw uncatchable exception
     *
     * @param option Option that will be unwrapped
     * @param <T>    Wrapped type
     * @return Value of Some orResult if None, throws exception
     */
    @NonNull
    public static <T> T getUnsafe(@NonNull final Option<T> option) {
        return option.getUnsafe();
    }

    /**
     * ATTENTION: Only use it when you know what you are doing!
     *
     * Returns inner value of option if it is Some, otherwise will throw give RuntimeException
     *
     * @param option    Option that will be unwrapped
     * @param exception Exception to be thrown
     * @param <T>       Wrapped type
     * @return Value of Some orResult if None, throws exception
     */
    @NonNull
    public static <T> T orThrowUnsafe(@NonNull final Option<T> option,
                                      @NonNull final RuntimeException exception) {
        if (option.isSome()) {
            return option.getUnsafe();
        } else {
            throw exception;
        }
    }
}
