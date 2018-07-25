package com.mdbank.exception;

public class DownloadException extends Exception {
    public DownloadException(String msg) {
        super(msg);
    }

    public DownloadException(String msg, Exception cause) {
        super(msg, cause);
    }


    public DownloadException(Exception cause) {
        super(cause);
    }
}
