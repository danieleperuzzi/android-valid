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

package com.danieleperuzzi.valid.core.constraint;

import android.support.annotation.Nullable;

import com.danieleperuzzi.valid.core.ValidableStatus;

/**
 * This Class holds the result of a validation done by a single {@link Constraint}
 *
 * <p>The error message is never provided when the validation succeeded, otherwise
 * is not mandatory to attach a message after a failure, it also can be null</p>
 */
public class ConstraintResult {

    public ValidableStatus status;

    @Nullable
    public String constraintError;

    private ConstraintResult() {
    }

    public ConstraintResult(ValidableStatus status, @Nullable String constraintError) {
        this.status = status;

        if (status == ValidableStatus.NOT_VALID) {
            this.constraintError = constraintError;
        } else {
            this.constraintError = null;
        }
    }
}
