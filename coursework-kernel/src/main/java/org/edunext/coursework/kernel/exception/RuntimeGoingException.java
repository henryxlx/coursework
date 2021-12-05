package org.edunext.coursework.kernel.exception;

/**
 * @author xulixin
 */
public class RuntimeGoingException extends RuntimeException {

    private String stateCode;

    public String getStateCode() {
        return stateCode;
    }

    public RuntimeGoingException(String message) {
        super(message);
    }

    public RuntimeGoingException(String stateCode, String message) {
        super(message);
        stateCode = stateCode;
    }
}
