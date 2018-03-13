package com.kmv.geological.exception.general;

import com.kmv.geological.exception.BusinessException;

/**
 * @author h.mohammadi
 */
public class NoSuchResourceException extends BusinessException {

    public NoSuchResourceException(String message) {
        super(message);
    }

    public NoSuchResourceException(String message, Throwable cause) {
        super(message, cause);
    }

}
