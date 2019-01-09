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
 * If we need to validate more than one {@link Validable} Object at a time and, instead,
 * we need to validate them all together we can use this interface
 */
public interface CollectionValidator {

    /**
     * It starts the validation on a map of <{@link Validable}, {@link ValidatorOptions}> and then
     * post the result using the supplied callback.
     *
     * @param validableMap  map of <{@link Validable}, {@link ValidatorOptions}> to be validated
     * @param callback      callback used to post the validation result
     */
    void validateCollection(Map<Validable<?>, ValidatorOptions> validableMap, Callback callback);

    /**
     * This interface is the callback itself, invoked by the CollectionValidator
     * when the validation process ends.
     *
     * <p>It is also used in some situations when we want to be constantly informed
     * about the changes on a set of {@link Validable}, see {@link ValidatorObserver}
     * for more references.</p>
     */
    interface Callback {

        /**
         * The class that implements this method receives a reference to a
         * Map of <{@link Validable}, {@link ValidatorResult}> that holds
         * the precise result of the collection validation process. It also receives
         * a synthetic result in the form of {@link ValidableCollectionStatus}.
         *
         * <p>It is also invoked when we are observing a {@link Validable} set
         * to get informations about the global state every time one of them
         * is validated</p>
         *
         * @param validableResults  map of <{@link Validable}, {@link ValidatorResult}>
         * @param status            the global status of the {@link Validable} set
         */
        void status(Map<Validable<?>, ValidatorResult> validableResults, ValidableCollectionStatus status);
    }
}
