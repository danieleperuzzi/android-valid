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

import android.util.Log;

import com.danieleperuzzi.valid.validator.Validable;
import com.danieleperuzzi.valid.validator.Validator;
import com.danieleperuzzi.valid.validator.ValidatorResult;
import com.danieleperuzzi.valid.validator.ValidatorResultObserverCallback;
import com.danieleperuzzi.valid.validator.ValidatorResultObserverStatus;
import com.danieleperuzzi.valid.validator.ValidatorStatus;

import java.util.HashMap;
import java.util.Map;

public class BaseValidatorResultObserver implements Validator.Callback {

    private final String TAG = getClass().getSimpleName();

    private int totalValidables = 0;
    private int validatedValidables = 0;
    private int notValidatedValidables = 0;

    private Map<Validable<?>, ValidableMetaDataHolder> validableMetaData;
    private Map<Validable<?>, ValidatorResult> validableResults = new HashMap<>();

    private ValidatorResultObserverCallback callback;

    public BaseValidatorResultObserver(Map<Validable<?>, ValidableMetaDataHolder> validableMetaData, ValidatorResultObserverCallback callback) {
        this.validableMetaData = validableMetaData;
        this.callback = callback;

        try {
            checkValidableMetaDataMapAndInitializeValidableResultMap();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void status(Validable<?> value, ValidatorResult result) {
        ValidableMetaDataHolder previousMetaData = validableMetaData.get(value);

        //just ensure that the callback result is correctly dispatched regardless if it should contribute to the observer or not
        if (previousMetaData != null && previousMetaData.getOriginalCallback() != null) {
            previousMetaData.getOriginalCallback().status(value, result);
        }

        if (previousMetaData != null && previousMetaData.shouldContributeToValidatorResultObserver()) {

            updateStatusCounters(previousMetaData.getCurrentStatus(), result.status);
            previousMetaData.setCurrentStatus(result.status);
            validableResults.put(value, result);

            if (callback != null) {
                ValidatorResultObserverStatus generalStatus;

                if (validatedValidables < totalValidables) {
                    generalStatus = ValidatorResultObserverStatus.AT_LEAST_ONE_NOT_VALIDATED;
                } else {
                    generalStatus = ValidatorResultObserverStatus.ALL_VALIDATED;
                }
                callback.status(validableResults, generalStatus);
            }
        }
    }

    private void checkValidableMetaDataMapAndInitializeValidableResultMap() throws Exception {
        for (Map.Entry<Validable<?>, ValidableMetaDataHolder> entry : validableMetaData.entrySet()) {
            ValidableMetaDataHolder data = entry.getValue();

            if (data != null && data.shouldContributeToValidatorResultObserver()) {

                validableResults.put(entry.getKey(), new ValidatorResult());
                totalValidables++;

                if (data.getCurrentStatus() == ValidatorStatus.VALIDATED) {
                    validatedValidables++;
                }

                if (data.getCurrentStatus() == ValidatorStatus.NOT_VALIDATED) {
                    notValidatedValidables++;
                }
            }
        }

        if ((validatedValidables + notValidatedValidables) != totalValidables) {
            throw new Exception("There is at least one validable that is neither validated nor not validated");
        }
    }

    private void updateStatusCounters(ValidatorStatus previousStatus, ValidatorStatus status) {
        if (previousStatus != status) {
            if (status == ValidatorStatus.VALIDATED) {
                validatedValidables++;
                notValidatedValidables--;
            }

            if (status == ValidatorStatus.NOT_VALIDATED) {
                validatedValidables--;
                notValidatedValidables++;
            }
        }
    }
}
