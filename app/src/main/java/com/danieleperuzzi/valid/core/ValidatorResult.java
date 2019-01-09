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

import java.util.Objects;

/**
 * Simple Class used to hold the result of a validation. The informations that are
 * useful to us are the {@link ValidableStatus} and an error message.
 *
 * <p>The error message is never provided when the validation succeeded, otherwise
 * is not mandatory to attach a message after a failure, it also can be null</p>
 */
public final class ValidatorResult {

    public ValidableStatus status;

    @Nullable
    public String validatorError;

    private ValidatorResult() {
    }

    /**
     * Adapt the {@link Constraint#evaluate(Object)} output to a format used by
     * the {@link Validator}
     *
     * @param result    the {@link ConstraintResult} given by {@link Constraint#evaluate(Object)}
     */
    ValidatorResult(ConstraintResult result) {
        this.status = result.status;
        this.validatorError = result.constraintError;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidatorResult that = (ValidatorResult) o;
        return status == that.status &&
                Objects.equals(validatorError, that.validatorError);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, validatorError);
    }
}
