package com.architecture.data.exception;


public class NetException extends Exception {
    public NetException() {
        super();
    }

    public NetException(String message) {
        super(message);
    }

    public NetException(Throwable cause) {
        super(cause);
    }
}
