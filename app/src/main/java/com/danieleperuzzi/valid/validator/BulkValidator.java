package com.danieleperuzzi.valid.validator;

import java.util.Map;

public interface BulkValidator {
    void validateBulk(Map<Validable<?>, ValidatorOptions> validableMap, Callback callback);

    interface Callback {
        void status(Map<Validable<?>, ValidatorResult> validableResults, BulkValidatorStatus status);
    }
}
