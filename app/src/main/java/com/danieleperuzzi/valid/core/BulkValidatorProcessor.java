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

import android.support.annotation.MainThread;

import java.util.HashMap;
import java.util.Map;

/**
 * This Class encapsulates the logic behind {@link BulkValidator} process.
 *
 * <p>Its main purpose is to do some calculations to eventually determine
 * if the set of {@link Validable} is all validated or there is at least one
 * {@link Validable} that is not validated</p>
 *
 * <p>The main advantage of keeping this logic in a separated Class is that
 * overlapped calls to the {@link BulkValidator#validateCollection(Map, CollectionValidator.Callback)}
 * never produce an inconsistent state over this calculations</p>
 *
 * <p>Notice that this Class implements {@link Validator} callback so it can
 * receive single validation result of all {@link Validable} in order to collect
 * them and make decisions</p>
 */
public final class BulkValidatorProcessor implements Validator.Callback {

    private int validableInstances;
    private CollectionValidator.Callback callback;

    private int validatedValidable = 0;
    private int notValidatedValidable = 0;

    private Map<Validable<?>, ValidatorResult> validableResults = new HashMap<>();

    /**
     * This Class is only used by the {@link BulkValidator} so package private
     * access fits perfectly.
     *
     * @param validableInstances    the number of {@link Validable} instances to
     *                              be validated
     * @param callback              the {@link CollectionValidator} callback used to post
     *                              the bulk validator process result
     */
    BulkValidatorProcessor(int validableInstances, CollectionValidator.Callback callback) {
        this.validableInstances = validableInstances;
        this.callback = callback;
    }

    /**
     * Here it happens the magic: this is the {@link Validator} callback and it is
     * invoked exactly {@link #validableInstances} times.
     *
     * <p>When the sum of the computed instances, intended as the number of validated
     * plus not validated {@link Validable}, is exactly {@link #validableInstances} times
     * we are allowed to check if all the {@link Validable} are validated or not.</p>
     *
     * <p>This is trivially done comparing the {@link #validatedValidable} and the
     * {@link #validableInstances}</p>
     *
     * @param value  the {@link Validable} Object that has been validated
     * @param result the {@link ValidatorResult}
     */
    @MainThread
    public void status(Validable<?> value, ValidatorResult result) {
        if (result.status == ValidableStatus.VALID) {
            validatedValidable++;
        } else {
            notValidatedValidable++;
        }

        validableResults.put(value, result);

        if ((validatedValidable + notValidatedValidable) == validableInstances) {
            if (validatedValidable == validableInstances) {
                triggerListener(validableResults, ValidableCollectionStatus.ALL_VALID);
                return;
            }

            if (validatedValidable < validableInstances) {
                triggerListener(validableResults, ValidableCollectionStatus.AT_LEAST_ONE_NOT_VALID);
                return;
            }
        }
    }

    /**
     * It only takes care of calling the {@link CollectionValidator.Callback}
     *
     * @param validableResults  map of <{@link Validable}, {@link ValidatorResult}>
     * @param status            the global status of the {@link Validable} set
     */
    @MainThread
    private void triggerListener(Map<Validable<?>, ValidatorResult> validableResults, ValidableCollectionStatus status) {
        if (callback != null) {
            callback.status(validableResults, status);
        }
    }
}
