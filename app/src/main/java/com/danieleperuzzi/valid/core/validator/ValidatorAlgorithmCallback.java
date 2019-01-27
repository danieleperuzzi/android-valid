package com.danieleperuzzi.valid.core.validator;

/**
 * Simple interface used to post to the {@link BaseValidator} the result
 * of the validation done by any validator algorithm
 */
public interface ValidatorAlgorithmCallback {
    void postValidatorAlgorithmResult(ValidatorAlgorithmResult result);
}
