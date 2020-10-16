package org.george.exception;

public class XMLParseException extends RuntimeException{

    public XMLParseException() {
        super();
    }

    public XMLParseException(String message) {
        super(message);
    }

    public XMLParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public XMLParseException(Throwable cause) {
        super(cause);
    }

    protected XMLParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
