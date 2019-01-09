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

import java.util.HashMap;
import java.util.Map;

/**
 * Helper Class that hold the error message map in the form of a String key
 * and a String Message.
 *
 * <p>It is intended tu use in conjunction with the {@link Constraint} Class
 * to ensure that when you instantiates it you will be easily able to pass
 * a custom error map with all the messages localized in your language or simply
 * provide whatever you want</p>
 *
 * <p>This Class is created upon the assumption that every {@link Constraint}
 * may return more than one error message depending on which case you may encounter</p>
 */
public final class ConstraintErrorMap {

    private Map<String, String> errorMap = new HashMap<>();

    Map<String, String> getErrorMap() {
        return errorMap;
    }

    private ConstraintErrorMap() {
    }

    public String getErrorByKey(String key) {
        return errorMap.get(key);
    }

    public final static class Builder {

        private ConstraintErrorMap instance;

        public Builder() {
            instance = new ConstraintErrorMap();
        }

        public Builder addErrorMessage(String key, String errorMessage) {
            instance.errorMap.put(key, errorMessage);
            return this;
        }

        public ConstraintErrorMap build() {
            return instance;
        }
    }
}
