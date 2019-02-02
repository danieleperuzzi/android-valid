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

package com.danieleperuzzi.valid.core.validator;

import com.danieleperuzzi.valid.core.collectionvalidator.BulkValidator;
import com.danieleperuzzi.valid.core.CollectionValidator;
import com.danieleperuzzi.valid.core.constraint.SortedConstraintSet;
import com.danieleperuzzi.valid.core.Validable;
import com.danieleperuzzi.valid.core.ValidableCollectionStatus;
import com.danieleperuzzi.valid.core.ValidableStatus;
import com.danieleperuzzi.valid.core.Validator;
import com.danieleperuzzi.valid.core.validator.impl.MainThreadValidator;

import java.util.Map;

/**
 * This class receives updates about the result of a validation of
 * a particular {@link Validable}, checks if it should be tracked
 * and update the global status.
 */
public class ValidatorObserver {

    private Map<Validable<?>, ValidatorResult> validatorResultByValidableMap;
    private CollectionValidator.Callback callback;

    private int validableInstances = 0;
    private int validValidables = 0;
    private int notValidValidables = 0;

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
     * @param constraintSetByValidableMap   the map used to track the set of {@link Validable}
     *                                      to observe and to initialize the {@link Helper} Object
     * @param callback                      {@link CollectionValidator.Callback} used to post
     *                                      the result
     */
    public ValidatorObserver(Map<Validable<?>, SortedConstraintSet> constraintSetByValidableMap, CollectionValidator.Callback callback) {
        Helper helper = new Helper(constraintSetByValidableMap);

        this.validatorResultByValidableMap = helper.getInitialStatus();
        this.callback = callback;

        init();
    }

    /**
     * Just used to count the initial statuses of the {@link Validable} collection
     */
    private void init() {
        validableInstances = validatorResultByValidableMap.size();

        for (Map.Entry<Validable<?>, ValidatorResult> entry : validatorResultByValidableMap.entrySet()) {
            ValidatorResult initialResult = entry.getValue();

            if (initialResult.status == ValidableStatus.VALID) {
                validValidables++;
            } else {
                notValidValidables++;
            }
        }
    }

    /**
     * Update the global status of the collection only if the new status differs from
     * the previous and then put the new status in the {@link #validatorResultByValidableMap}
     *
     * @param value             the {@link Validable} that has been validated
     * @param currentResult     the result given by the {@link Validator}
     * @param previousResult    the result held in the {@link #validatorResultByValidableMap}
     *                          before it may be updated
     */
    private void update(Validable<?> value, ValidatorResult currentResult, ValidatorResult previousResult) {
        if (!currentResult.equals(previousResult)) {
            if (currentResult.status == ValidableStatus.VALID) {
                validValidables++;
                notValidValidables--;
            } else {
                validValidables--;
                notValidValidables++;
            }

            validatorResultByValidableMap.put(value, currentResult);
        }
    }

    /**
     * Invoke the {@link #callback} with the global {@link Validable}
     * collection status updated.
     */
    private void triggerListener() {
        if (callback != null) {
            ValidableCollectionStatus actualStatus;

            if (validValidables < validableInstances) {
                actualStatus = ValidableCollectionStatus.AT_LEAST_ONE_NOT_VALID;
            } else {
                actualStatus = ValidableCollectionStatus.ALL_VALID;
            }

            callback.status(validatorResultByValidableMap, actualStatus);
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
        if (validatorResultByValidableMap != null && validatorResultByValidableMap.containsKey(value)) {
            synchronized (this) {
                update(value, result, validatorResultByValidableMap.get(value));
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

        private CollectionValidator collectionValidator;

        private Map<Validable<?>, SortedConstraintSet> constraintSetByValidableMap;
        private Map<Validable<?>, ValidatorResult> validatorResultByValidableMap;

        private Map<Validable<?>, ValidatorResult> getInitialStatus() {
            return validatorResultByValidableMap;
        }

        /**
         * During object creation we do bulk validation on the set of {@link Validable}
         * on the main thread ensuring that {@link #validatorResultByValidableMap}
         * is assigned when done and any subsequent calls to {@link #getInitialStatus()}
         * returns the map needed by the {@link ValidatorObserver}
         *
         * @param constraintSetByValidableMap   the data used by {@link CollectionValidator}
         *                                      to operate
         */
        private Helper(Map<Validable<?>, SortedConstraintSet> constraintSetByValidableMap) {
            collectionValidator = new BulkValidator(new MainThreadValidator());
            this.constraintSetByValidableMap = constraintSetByValidableMap;

            init();
        }

        private void init() {
            collectionValidator.validateCollection(constraintSetByValidableMap, (validatorResultByValidableMap, status) -> {
                this.validatorResultByValidableMap = validatorResultByValidableMap;
            });
        }
    }
}
