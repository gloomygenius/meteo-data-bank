package com.mdbank.util;

import java.util.function.Supplier;

@FunctionalInterface
public interface ExSupplier<T> extends Supplier<T> {
    default T get() {
        try {
            return apply();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    T apply() throws Exception;
}
