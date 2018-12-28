package com.danieleperuzzi.valid.validator;

import java.util.Map;

public interface ValidatorResultObserverCallback {
    void status(Map<Validable<?>, ValidatorResult> validableResults, ValidatorResultObserverStatus status);
}
