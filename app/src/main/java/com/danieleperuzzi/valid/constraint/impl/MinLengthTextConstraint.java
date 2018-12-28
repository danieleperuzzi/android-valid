package com.danieleperuzzi.valid.constraint.impl;

import com.danieleperuzzi.valid.constraint.ConstraintErrorMap;
import com.danieleperuzzi.valid.validator.Validable;
import com.danieleperuzzi.valid.validator.ValidatorResult;
import com.danieleperuzzi.valid.validator.ValidatorStatus;

import java.util.ArrayList;
import java.util.List;

public class MinLengthTextConstraint extends BaseConstraint<String, Integer> {

    private final boolean shouldBrakeValidateChain = false;

    public MinLengthTextConstraint(Integer minLength, int evaluationPriority, ConstraintErrorMap errorMap) throws Exception {
        super(minLength, evaluationPriority, errorMap);
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

        if (text == null && getValidationConstraint() == 0) {
            result.status = ValidatorStatus.VALIDATED;
            result.validatorError = null;
            return result;
        }

        if (text == null && getValidationConstraint() > 0) {
            result.status = ValidatorStatus.NOT_VALIDATED;
            result.validatorError = getErrorMap().getErrorByKey("MIN_LENGTH_NOT_REACHED");
            return result;
        }

        if (text.length() < getValidationConstraint()) {
            result.status = ValidatorStatus.NOT_VALIDATED;
            result.validatorError = getErrorMap().getErrorByKey("MIN_LENGTH_NOT_REACHED");
        } else {
            result.status = ValidatorStatus.VALIDATED;
            result.validatorError = null;
        }

        return result;
    }

    @Override
    protected List<String> getErrorKeyList() {
        List<String> errorKeyList = new ArrayList<>();
        errorKeyList.add("MIN_LENGTH_NOT_REACHED");

        return errorKeyList;
    }
}
