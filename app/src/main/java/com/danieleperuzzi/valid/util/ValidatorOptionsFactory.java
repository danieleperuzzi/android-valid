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

package com.danieleperuzzi.valid.util;

import com.danieleperuzzi.valid.core.ValidatorOptions;

import java.util.Map;

/**
 * Useful to instantiate once all the {@link ValidatorOptions} of a collection
 * of validables and then retrieve them when needed.
 */
public class ValidatorOptionsFactory {

    private Map<String, ValidatorOptions> validatorOptionsMap;

    public ValidatorOptionsFactory(Map<String, ValidatorOptions> validatorOptionsMap) {
        this.validatorOptionsMap = validatorOptionsMap;
    }

    public ValidatorOptions getOptionsByTag(String tag) {
        if (validatorOptionsMap != null) {
            return validatorOptionsMap.get(tag);
        }

        return null;
    }
}
