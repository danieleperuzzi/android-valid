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

import com.danieleperuzzi.valid.core.validator.impl.SimpleValidatorAlgorithm;
import com.danieleperuzzi.valid.core.validator.impl.ValidatorAlgorithm;

/**
 * Factory class that generates a validation algorithm based on the input data.
 *
 * <p>See:</p>
 *
 * <ul>
 *     <li>{@link ValidatorAlgorithm}</li>
 *     <li>{@link SimpleValidatorAlgorithm}</li>
 * </ul>
 */
public class ValidatorAlgorithmFactory {

    private final int SIMPLE_VALIDATOR_ALGORITHM = 0;
    private final int VALIDATOR_ALGORITHM = 1;

    BaseValidatorAlgorithm createValidatorAlgorithm(ValidatorAlgorithmData data, ValidatorAlgorithmCallback callback) {
        int algorithmType = validatorAlgorithmChooser(data);

        if (algorithmType == SIMPLE_VALIDATOR_ALGORITHM) {
            return new SimpleValidatorAlgorithm(data, callback);
        }

        if (algorithmType == VALIDATOR_ALGORITHM) {
            return new ValidatorAlgorithm(data, callback);
        }

        return null;
    }

    private int validatorAlgorithmChooser(ValidatorAlgorithmData data) {
        if (data.constraint != null && data.constraintSet == null) {
            return SIMPLE_VALIDATOR_ALGORITHM;
        }

        if (data.constraint == null && data.constraintSet != null) {
            return VALIDATOR_ALGORITHM;
        }

        return -1;
    }
}
