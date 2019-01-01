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

import com.danieleperuzzi.valid.validator.Validator;
import com.danieleperuzzi.valid.validator.ValidatorStatus;

public class ValidableMetaDataHolder {

    private ValidatorStatus currentStatus;
    private boolean shouldContributeToValidatorResultObserver;
    private Validator.Callback originalCallback;


    public ValidatorStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(ValidatorStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public boolean shouldContributeToValidatorResultObserver() {
        return shouldContributeToValidatorResultObserver;
    }

    public Validator.Callback getOriginalCallback() {
        return originalCallback;
    }

    public ValidableMetaDataHolder(ValidatorStatus currentStatus, boolean shouldContributeToValidatorResultObserver, Validator.Callback originalCallback) {
        this.currentStatus = currentStatus;
        this.shouldContributeToValidatorResultObserver = shouldContributeToValidatorResultObserver;
        this.originalCallback = originalCallback;
    }
}
