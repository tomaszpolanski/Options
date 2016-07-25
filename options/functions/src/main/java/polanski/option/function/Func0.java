package polanski.option.function;

import java.util.concurrent.Callable;

/**
 * Copy of Func0 from RxJava
 */
public interface Func0<R> extends Function, Callable<R> {

    @Override
    R call();
}
