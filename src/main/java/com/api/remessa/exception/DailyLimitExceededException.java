package com.api.remessa.exception;

public class DailyLimitExceededException extends BusinessException {

    public DailyLimitExceededException(String message) {
        super(message);
    }
}