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

package com.danieleperuzzi.valid.constraint.impl;

import com.danieleperuzzi.valid.constraint.ConstraintErrorMap;
import com.danieleperuzzi.valid.validator.Validable;
import com.danieleperuzzi.valid.validator.ValidatorResult;
import com.danieleperuzzi.valid.validator.ValidatorStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTextConstraint extends BaseConstraint<String, String> {

    private final boolean shouldBrakeValidateChain = false;

    public RegexTextConstraint(String regex, int evaluationPriority, ConstraintErrorMap errorMap) throws Exception {
        super(regex, evaluationPriority, errorMap);
    }

    @Override
    public Class<?> getValidableConcreteClassType(Validable<?> value) {
        if (value.getValue() == null) {
            return null;
        }
        return value.getValue().getClass();
    }

    @Override
    public boolean shouldBreakValidateChain(String text) {
        return shouldBrakeValidateChain;
    }

    @Override
    public ValidatorResult evaluate(String text) {
        ValidatorResult result = new ValidatorResult();

        if (!satisfyRegex(text)) {
            result.status = ValidatorStatus.NOT_VALIDATED;
            result.validatorError = getErrorMap().getErrorByKey("REGEX_NOT_SATISFIED");
        } else {
            result.status = ValidatorStatus.VALIDATED;
            result.validatorError = null;
        }

        return result;
    }

    @Override
    protected List<String> getErrorKeyList() {
        List<String> errorKeyList = new ArrayList<>();
        errorKeyList.add("REGEX_NOT_SATISFIED");

        return errorKeyList;
    }

    private boolean satisfyRegex(String text) {
        if (text == null) {
            return false;
        }

        if (getValidationConstraint() == null) {
            return true;
        }

        Pattern pattern = Pattern.compile(getValidationConstraint());
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }
}
