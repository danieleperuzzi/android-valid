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

package com.danieleperuzzi.valid.core.collectionvalidator;

import com.danieleperuzzi.valid.core.CollectionValidator;
import com.danieleperuzzi.valid.core.constraint.SortedConstraintSet;
import com.danieleperuzzi.valid.core.Validable;
import com.danieleperuzzi.valid.core.Validator;

import java.util.Map;

/**
 * This class implements the {@link CollectionValidator} interface and aims to provide a
 * convenient way to validate a set of {@link Validable} in one shot.
 */
public class BulkValidator implements CollectionValidator {

    private Validator validator;
    private BulkValidatorProcessorFactory factory;

    /**
     * In order to validate a set of {@link Validable} all together this Class
     * needs a {@link Validator} to perform validation operations.
     *
     * <p>This class uses a {@link BulkValidatorProcessorFactory} to to get
     * new {@link BulkValidatorProcessor} every time the {@link #validateCollection(Map, Callback)}
     * is invoked.</p>
     *
     * <p>It is simpler to use directly {@link #BulkValidator(Validator)},
     * this constructor is intended for unit test purposes.</p>
     *
     * @param validator     a reference to a Class that implements {@link Validator}
     *                      interface
     * @param factory       factory that creates new instances of {@link BulkValidatorProcessor}
     */
    public BulkValidator(Validator validator, BulkValidatorProcessorFactory factory) {
        this.validator = validator;
        this.factory = factory;
    }

    /**
     * In order to validate a set of {@link Validable} all together this Class
     * needs a {@link Validator} to perform validation operations.
     *
     * @param validator     a reference to a Class that implements {@link Validator}
     *                      interface
     */
    public BulkValidator(Validator validator) {
        this.validator = validator;
        factory = new BulkValidatorProcessorFactory();
    }

    /**
     * At this point we have references to all the {@link Validable} and their relatives
     * {@link SortedConstraintSet} that should be processed in one shot, the only thing
     * we have to do is to invoke validation on everyone of them, passing to the validator
     * a reference to a new {@link BulkValidatorProcessor} instance that collects all the
     * calls to the validator callback.
     *
     * <p>{@link BulkValidatorProcessor} helps us to do some calculations to achieve
     * the goal of validating a set of {@link Validable}.</p>
     *
     * <p>First of all it encapsulates the main logic behind bulk validation and this
     * gives us the good side effect that, in case of overlapping validations, the logics
     * remain separated</p>
     *
     * @param constraintSetByValidableMap   map of <{@link Validable}, {@link SortedConstraintSet}>
     *                                      to be validated
     * @param callback                      callback used to post the validation result
     */
    @Override
    public void validateCollection(Map<Validable<?>, SortedConstraintSet> constraintSetByValidableMap, CollectionValidator.Callback callback) {
        int validableInstances = constraintSetByValidableMap.size();

        BulkValidatorProcessor callbackHolder = factory.createBulkValidatorProcessor(validableInstances, callback);

        for (Map.Entry<Validable<?>, SortedConstraintSet> entry : constraintSetByValidableMap.entrySet()) {
            Validable<?> validable = entry.getKey();
            SortedConstraintSet constraintSet = entry.getValue();

            validator.validate(validable, constraintSet, callbackHolder);
        }
    }
}
