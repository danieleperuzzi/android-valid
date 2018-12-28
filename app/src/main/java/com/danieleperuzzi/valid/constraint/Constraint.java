package com.danieleperuzzi.valid.constraint;

import android.support.annotation.NonNull;

import com.danieleperuzzi.valid.validator.Validable;
import com.danieleperuzzi.valid.validator.ValidatorResult;

public interface Constraint extends Comparable<Constraint> {
    boolean shouldBreakValidateChain(Validable<?> value) throws ClassCastException;
    ValidatorResult evaluate(Validable<?> value) throws ClassCastException;
    int getEvaluationPriority();
    int compareTo(@NonNull Constraint validator);
}
