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

import com.danieleperuzzi.valid.core.impl.MainThreadValidator;

import java.util.Map;

/**
 * This class receives updates about the result of a validation of
 * a particular {@link Validable}, checks if it should be tracked
 * and update the global status.
 */
public final class ValidatorObserver {

    private Map<Validable<?>, ValidatorResult> validableResults;
    private CollectionValidator.Callback callback;

    private int totalValidables = 0;
    private int validatedValidables = 0;
    private int notValidatedValidables = 0;

    public ValidatorObserver(Map<Validable<?>, ValidatorOptions> validables, CollectionValidator.Callback callback) {
        Helper helper = new Helper(validables);

        this.validableResults = helper.getValidableResults();
        this.callback = callback;

        init();
    }

    private void init() {
        totalValidables = validableResults.size();

        for (Map.Entry<Validable<?>, ValidatorResult> entry : validableResults.entrySet()) {
            ValidatorResult initialResult = entry.getValue();

            if (initialResult.status == ValidableStatus.VALIDATED) {
                validatedValidables++;
            } else {
                notValidatedValidables++;
            }
        }
    }

    private void update(Validable<?> value, ValidatorResult currentResult, ValidatorResult previousResult) {
        if (!currentResult.equals(previousResult)) {
            if (currentResult.status == ValidableStatus.VALIDATED) {
                validatedValidables++;
                notValidatedValidables--;
            } else {
                validatedValidables--;
                notValidatedValidables++;
            }

            validableResults.put(value, currentResult);
        }
    }

    private void triggerListener() {
        if (callback != null) {
            ValidableCollectionStatus actualStatus;

            if (validatedValidables < totalValidables) {
                actualStatus = ValidableCollectionStatus.AT_LEAST_ONE_NOT_VALIDATED;
            } else {
                actualStatus = ValidableCollectionStatus.ALL_VALIDATED;
            }

            callback.status(validableResults, actualStatus);
        }
    }

    void notify(Validable<?> value, ValidatorResult result) {
        if (validableResults != null && validableResults.containsKey(value)) {
            update(value, result, validableResults.get(value));
            triggerListener();
        }
    }

    /**
     * Helper Class used to build {@link ValidatorObserver} with an initial
     * status that reflects the real status of the observed {@link Validable}.
     */
    private static class Helper {

        private CollectionValidator validator;

        private Map<Validable<?>, ValidatorOptions> validables;
        private Map<Validable<?>, ValidatorResult> validableResults;

        private Map<Validable<?>, ValidatorResult> getValidableResults() {
            return validableResults;
        }

        /**
         * During object creation we do bulk validation on the set of {@link Validable}
         * on the main thread ensuring that {@link #validableResults} is assigned
         * when done and any subsequent calls to {@link #getValidableResults()} returns
         * the map needed by the {@link ValidatorObserver}
         *
         * @param validables    the data used by {@link CollectionValidator} to operate
         */
        private Helper(Map<Validable<?>, ValidatorOptions> validables) {
            validator = new BulkValidator(new MainThreadValidator());
            this.validables = validables;

            init();
        }

        private void init() {
            validator.validateCollection(validables, (validableResults, status) -> {
                this.validableResults = validableResults;
            });
        }
    }
}