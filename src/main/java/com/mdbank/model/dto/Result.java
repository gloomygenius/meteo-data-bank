package com.mdbank.model.dto;

public class Result<E> {
    private E load;
    private ResultStatus status;
    private String message;

    public Result(E load, ResultStatus status, String message) {
        this.load = load;
        this.status = status;
        this.message = message;
    }

    public E getLoad() {
        return load;
    }

    public void setLoad(E load) {
        this.load = load;
    }

    public ResultStatus getStatus() {
        return status;
    }

    public void setStatus(ResultStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public enum ResultStatus {
        SUCCESS,
        WARNING,
        ERROR;
    }
}
