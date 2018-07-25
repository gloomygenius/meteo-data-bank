package com.mdbank.util;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Exceptional {
    public static <T> Supplier<T> supplier(ExSupplier<T> exSupplier) {
        return exSupplier;
    }

    public static <T1, T2> Function<T1, T2> function(ExFunction<T1, T2> exFunction) {
        return exFunction;
    }

    public static <E> E tryCatch(ExSupplier<E> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
