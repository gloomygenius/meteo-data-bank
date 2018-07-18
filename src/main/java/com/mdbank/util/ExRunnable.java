package com.mdbank.util;

@FunctionalInterface
public interface ExRunnable extends Runnable {
    @Override
    default void run() {
        try {
            apply();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    void apply() throws Exception;
}
