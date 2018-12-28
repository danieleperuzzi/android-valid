package com.danieleperuzzi.valid.validator;

import com.danieleperuzzi.valid.constraint.Constraint;
import com.danieleperuzzi.valid.constraint.impl.BaseConstraint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValidatorOptions {

    private List<Constraint> constraints = new ArrayList<>();

    public List<Constraint> getConstraints() {
        return constraints;
    }

    private ValidatorOptions() {
    }


    public static class Builder {

        private ValidatorOptions instance;

        public Builder() {
            this.instance = new ValidatorOptions();
        }

        public Builder addConstraint(BaseConstraint validator) {
            instance.constraints.add(validator);
            return this;
        }

        public ValidatorOptions build() {
            //must order constraints by priority, from lower to higher
            Collections.sort(instance.constraints);
            return instance;
        }
    }
}
