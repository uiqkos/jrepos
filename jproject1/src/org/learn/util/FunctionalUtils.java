package org.learn.util;

import java.util.function.BiFunction;
import java.util.function.Function;

public class FunctionalUtils {
    public static <T, U, R> Function<U, R> partial(BiFunction<T, U, R> f, T x) {
        return (y) -> f.apply(x, y);
    }



}
