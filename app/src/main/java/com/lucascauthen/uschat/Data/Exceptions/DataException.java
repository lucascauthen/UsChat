package com.lucascauthen.uschat.Data.Exceptions;

/**
 * Created by lhc on 6/15/15.
 */
public class DataException extends Exception {
    private int code;
    public DataException(int theCode, String theMessage) {
        super(theMessage);
        this.code = theCode;
    }

    public DataException(int theCode, String message, Throwable cause) {
        super(message, cause);
        this.code = theCode;
    }

    public DataException(int cause) {
        super(cause);
        this.code = -1;
    }
}
