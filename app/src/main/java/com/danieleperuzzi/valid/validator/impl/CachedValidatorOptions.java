package com.danieleperuzzi.valid.validator.impl;

import com.danieleperuzzi.valid.validator.ValidatorOptions;
import com.danieleperuzzi.valid.validator.ValidatorOptionsHolder;

public abstract class CachedValidatorOptions implements ValidatorOptionsHolder {

    public abstract ValidatorOptions getCachedValidatorOptions();

    public abstract ValidatorOptions getNewValidatorOptions();

    @Override
    public ValidatorOptions get() {
        ValidatorOptions validatorOptions = getCachedValidatorOptions();

        if (validatorOptions != null) {
            return validatorOptions;
        }

        return getNewValidatorOptions();
    }
}
