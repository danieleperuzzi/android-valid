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

package com.danieleperuzzi.valid.core;

import java.util.Map;

/**
 * This class implements the {@link CollectionValidator} interface and aims to provide a
 * convenient way to validate a set of {@link Validable} in one shot.
 */
public final class BulkValidator implements CollectionValidator {

    private Validator validator;

    /**
     * In order to validate a set of {@link Validable} all together this Class
     * needs a {@link Validator} to perform validation operations
     *
     * @param validator     a reference to a Class that implements {@link Validator}
     *                      interface
     */
    public BulkValidator(Validator validator) {
        this.validator = validator;
    }

    /**
     * At this point we have references to all the {@link Validable} and their relatives
     * {@link ValidatorOptions} that should be processed in one shot, the only thing
     * we have to do is to invoke validation on everyone of them, passing to the validator
     * a reference to a new {@link BulkValidatorProcessor} instance that collects all the
     * calls to the validator callback.
     *
     * <p>{@link BulkValidatorProcessor} helps us to do some calculations to achieve
     * the goal of validating a set of {@link Validable}.</p>
     *
     * <p>First of all it encapsulates the main logic behind bulk validation and this
     * gives us the good side effect that, in case of overlapping validations, the logics
     * remain separated</p>
     *
     * @param validableMap map of <{@link Validable}, {@link ValidatorOptions}> to be validated
     * @param callback     callback used to post the validation result
     */
    @Override
    public void validateCollection(Map<Validable<?>, ValidatorOptions> validableMap, CollectionValidator.Callback callback) {
        int validableInstances = validableMap.size();

        BulkValidatorProcessor callbackHolder = new BulkValidatorProcessor(validableInstances, callback);

        for (Map.Entry<Validable<?>, ValidatorOptions> entry : validableMap.entrySet()) {
            Validable<?> validable = entry.getKey();
            ValidatorOptions options = entry.getValue();

            validator.validate(validable, options, callbackHolder);
        }
    }
}
