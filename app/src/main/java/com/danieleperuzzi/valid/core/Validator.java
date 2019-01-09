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

import android.support.annotation.Nullable;

/**
 * This interface represents the entry point of the whole validation process.
 */
public interface Validator {

    /**
     * This method starts the validation on a {@link Validable} Object accordingly
     * to the {@link ValidatorOptions} provided and post the result using
     * the supplied {@link Callback}.
     *
     * @param value     the {@link Validable} Object that is going to be validated
     * @param options   the {@link ValidatorOptions} that the value should all match
     *                  to be positive validated
     * @param callback  {@link Callback} used to post the validation result
     */
    void validate(Validable<?> value, ValidatorOptions options, Callback callback);

    /**
     * This method accepts one more parameter respect the one above and it is an
     * optional {@link ValidatorObserver} that is constantly informed about the
     * validation output of a certain {@link Validable} in order to know the
     * global state of a set of {@link Validable}
     *
     * @param value     the {@link Validable} Object that is going to be validated
     * @param options   the {@link ValidatorOptions} that the value should all match
     *                  to be positive validated
     * @param observer  the optional {@link ValidatorObserver}
     * @param callback  {@link Callback} used to post the validation result
     */
    void validate(Validable<?> value, ValidatorOptions options, @Nullable ValidatorObserver observer, Callback callback);

    /**
     * This interface is the callback itself, invoked by the Validator
     * when the validation process ends.
     */
    interface Callback {

        /**
         * The class that implements this method receives a reference to
         * the {@link Validable} Object that has been validated
         * and the {@link ValidatorResult} of the validation.
         *
         * @param value     the {@link Validable} Object that has been validated
         * @param result    the {@link ValidatorResult}
         */
        void status(Validable<?> value, ValidatorResult result);
    }
}
