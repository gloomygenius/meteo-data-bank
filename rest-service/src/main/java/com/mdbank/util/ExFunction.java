package com.mdbank.util;

import java.util.function.Function;

public interface ExFunction<T1, T2> extends Function<T1, T2>{
    @Override
    default T2 apply(T1 t1) {
        try {
            return convert(t1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    T2 convert(T1 t1) throws Exception;
}
