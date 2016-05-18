package polanski.option.function;

/**
 * Copy of Func1 from RxJava
 */
public interface Func1<T, R> extends Function {

    R call(T t);
}
