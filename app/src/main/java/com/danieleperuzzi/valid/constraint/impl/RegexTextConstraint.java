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
