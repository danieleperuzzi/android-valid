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

import android.support.annotation.Nullable;

import com.danieleperuzzi.valid.core.validator.impl.SimpleValidatorAlgorithm;
import com.danieleperuzzi.valid.core.Validable;
import com.danieleperuzzi.valid.core.Validator;
import com.danieleperuzzi.valid.core.validator.impl.ValidatorAlgorithm;

/**
 * This class encapsulates the result of the computation done by any
 * validator algorithm.
 *
 * <p>Currently are defined two algorithms:</p>
 *
 * <ul>
 *     <li>{@link SimpleValidatorAlgorithm}</li>
 *     <li>{@link ValidatorAlgorithm}</li>
 * </ul>
 */
public final class ValidatorAlgorithmResult {

    public Validable<?> value;
    public ValidatorResult result;
    @Nullable public ValidatorObserver observer;
    public Validator.Callback callback;

    /**
     * @param value     the {@link Validable} Object that has been validated
     * @param result    the {@link ValidatorResult} of the validation
     * @param observer  the optional {@link ValidatorObserver}
     * @param callback  {@link Validator.Callback} used to post the validation result
     */
    public ValidatorAlgorithmResult(Validable<?> value, ValidatorResult result, @Nullable ValidatorObserver observer, Validator.Callback callback) {
        this.value = value;
        this.result = result;
        this.observer = observer;
        this.callback = callback;
    }
}