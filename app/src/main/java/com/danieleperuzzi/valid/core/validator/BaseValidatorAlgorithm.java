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

import android.support.annotation.AnyThread;

/**
 * Simple skeleton class used to interact to the {@link BaseValidator}
 */
public abstract class BaseValidatorAlgorithm implements Runnable {

    protected ValidatorAlgorithmData data;
    private ValidatorAlgorithmCallback callback;

    protected BaseValidatorAlgorithm(ValidatorAlgorithmData data, ValidatorAlgorithmCallback callback) {
        this.data = data;
        this.callback = callback;
    }

    /**
     * Subclasses define here the validation algorithm
     */
    @AnyThread
    public abstract void run();

    /**
     * @param result    the result of the computation, see {@link ValidatorAlgorithmResult} for more
     *                  informations
     */
    protected final void postResult(ValidatorAlgorithmResult result) {
        if (callback != null) {
            callback.postValidatorAlgorithmResult(result);
        }
    }
}
