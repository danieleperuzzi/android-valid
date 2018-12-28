package com.danieleperuzzi.valid.validator.impl;

import com.danieleperuzzi.valid.validator.ValidatorOptions;
import com.danieleperuzzi.valid.validator.ValidatorOptionsFactory;
import com.danieleperuzzi.valid.validator.ValidatorOptionsHolder;

import java.util.Map;

public abstract class BaseValidatorOptionsFactory implements ValidatorOptionsFactory {

    public abstract Map<String, ValidatorOptionsHolder> getValidatorOptionsMap();

    @Override
    public ValidatorOptions getOptionsByTag(String tag) {
        Map<String, ValidatorOptionsHolder> validatorOptionsMap = getValidatorOptionsMap();

        if (validatorOptionsMap == null) {
            return null;
        }

        for (Map.Entry<String, ValidatorOptionsHolder> validatorOptionsEntry : validatorOptionsMap.entrySet()) {
            if (validatorOptionsEntry.getKey().equals(tag)) {
                return validatorOptionsEntry.getValue().get();
            }
        }

        return null;
    }
}
