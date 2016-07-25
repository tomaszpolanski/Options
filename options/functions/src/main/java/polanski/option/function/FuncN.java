package polanski.option.function;

/**
 * Copy of FuncN from RxJava
 */
public interface FuncN<R> extends Function {

    R call(Object... args);
}
