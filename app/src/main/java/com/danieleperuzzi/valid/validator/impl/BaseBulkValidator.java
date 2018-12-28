package com.danieleperuzzi.valid.validator.impl;

import com.danieleperuzzi.valid.validator.BulkValidator;
import com.danieleperuzzi.valid.validator.Validable;
import com.danieleperuzzi.valid.validator.Validator;
import com.danieleperuzzi.valid.validator.ValidatorOptions;

import java.util.Map;

public class BaseBulkValidator implements BulkValidator {

    private Validator validator;

    public BaseBulkValidator(Validator validator) {
        this.validator = validator;
    }

    @Override
    public void validateBulk(Map<Validable<?>, ValidatorOptions> validableMap, BulkValidator.Callback callback) {
        int validableInstances = validableMap.size();

        BulkValidatorCallbackHolder callbackHolder = new BulkValidatorCallbackHolder(validableInstances, callback);

        for (Map.Entry<Validable<?>, ValidatorOptions> entry : validableMap.entrySet()) {
            Validable<?> validable = entry.getKey();
            ValidatorOptions options = entry.getValue();

            validator.validate(validable, options, callbackHolder);
        }
    }
}
