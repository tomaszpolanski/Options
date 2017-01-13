package polanski.option;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import polanski.option.function.Action0;


/**
 * Provides an implementation of a Unit Type.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Unit_type">
 * https://en.wikipedia.org/wiki/Unit_type
 * </a>
 */
public enum Unit {
    DEFAULT;

    /**
     * Runs an action and returns a unit.
     *
     * @param action to be executed
     * @return Unit
     */
    @NotNull
    public static Unit from(@NotNull final Action0 action) {
        action.call();
        return DEFAULT;
    }

    /**
     * Ignores an action and returns a unit.
     *
     * @param ignored to be ignored
     * @return Unit
     */
    @NotNull
    public static Unit asUnit(@Nullable final Object ignored) {
        return DEFAULT;
    }

}
