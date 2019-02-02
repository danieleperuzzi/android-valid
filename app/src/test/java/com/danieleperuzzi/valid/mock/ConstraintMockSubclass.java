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

package com.danieleperuzzi.valid.mock;

import java.util.Map;

public class ConstraintMockSubclass<V, C> extends ConstraintMock<V, C> {

    public ConstraintMockSubclass(C constraint, int evaluationPriority, String error) {
        super(constraint, evaluationPriority, error);
    }

    public ConstraintMockSubclass(C constraint, int evaluationPriority, Map<String, String> errorMap) {
        super(constraint, evaluationPriority, errorMap);
    }
}
