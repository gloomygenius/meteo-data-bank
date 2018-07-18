package com.mdbank.exception;

public class NetCdfReadException extends RuntimeException {
    public NetCdfReadException(Exception e) {
        super(e);
    }

    public NetCdfReadException(String msg) {
        super(msg);
    }
}
