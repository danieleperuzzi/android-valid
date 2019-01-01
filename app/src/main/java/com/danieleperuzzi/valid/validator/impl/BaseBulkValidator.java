/*
 * Copyright 2019 Daniele Peruzzi. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
