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

    /**
     * In order to observe the global status of a {@link Validable} collection every time
     * one of them is being validated we must know the initial {@link ValidableStatus} of
     * each one.
     *
     * <p>This is mandatory because is possible that, in a set of three validables, we
     * only check the first and never the other two.</p>
     *
     * <p>To achieve this the {@link Helper} Class is used, it performs a bulk validation
     * on the entire collection and give us back the initial result.</p>
     *
     * @param validables    the map used to track the set of {@link Validable} to observe
     *                      and to initialize the {@link Helper} Object
     * @param callback      {@link CollectionValidator.Callback} used to post the result
     */
    public ValidatorObserver(Map<Validable<?>, ValidatorOptions> validables, CollectionValidator.Callback callback) {
        Helper helper = new Helper(validables);

        this.validableResults = helper.getValidableResults();
        this.callback = callback;

        init();
    }

    /**
     * Just used to count the initial statuses of the {@link Validable} collection
     */
    private void init() {
        totalValidables = validableResults.size();

        for (Map.Entry<Validable<?>, ValidatorResult> entry : validableResults.entrySet()) {
            ValidatorResult initialResult = entry.getValue();

            if (initialResult.status == ValidableStatus.VALID) {
                validatedValidables++;
            } else {
                notValidatedValidables++;
            }
        }
    }

    /**
     * Update the global status of the collection only if the new status differs from
     * the previous and then put the new status in the {@link #validableResults} map.
     *
     * @param value             the {@link Validable} that has been validated
     * @param currentResult     the result given by the {@link Validator}
     * @param previousResult    the result held in the {@link #validableResults} map before
     *                          it may be updated
     */
    private void update(Validable<?> value, ValidatorResult currentResult, ValidatorResult previousResult) {
        if (!currentResult.equals(previousResult)) {
            if (currentResult.status == ValidableStatus.VALID) {
                validatedValidables++;
                notValidatedValidables--;
            } else {
                validatedValidables--;
                notValidatedValidables++;
            }

            validableResults.put(value, currentResult);
        }
    }

    /**
     * Invoke the {@link #callback} with the global {@link Validable}
     * collection status updated.
     */
    private void triggerListener() {
        if (callback != null) {
            ValidableCollectionStatus actualStatus;

            if (validatedValidables < totalValidables) {
                actualStatus = ValidableCollectionStatus.AT_LEAST_ONE_NOT_VALID;
            } else {
                actualStatus = ValidableCollectionStatus.ALL_VALID;
            }

            callback.status(validableResults, actualStatus);
        }
    }

    /**
     * Every time the {@link Validator} is instructed to post result of a validation
     * to this Class it calls this method.
     *
     * <p>It does simple check to ensure that the {@link Validable} that has been
     * validated is one of them that should be observed and if it does then
     * update the global status in atomic way</p>
     *
     * @param value     the {@link Validable} that has been validated
     * @param result    the result of the validation
     */
    void notify(Validable<?> value, ValidatorResult result) {
        if (validableResults != null && validableResults.containsKey(value)) {
            synchronized (this) {
                update(value, result, validableResults.get(value));
                triggerListener();
            }
        }
    }

    /**
     * Helper Class used to build {@link ValidatorObserver} with an initial
     * status that reflects the real status of the observed {@link Validable}
     * collection.
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
