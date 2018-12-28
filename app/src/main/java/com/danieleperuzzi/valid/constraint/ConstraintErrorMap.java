package com.danieleperuzzi.valid.constraint;

import java.util.HashMap;
import java.util.Map;

public class ConstraintErrorMap {

    private Map<String, String> errorMap = new HashMap<>();

    public Map<String, String> getErrorMap() {
        return errorMap;
    }

    private ConstraintErrorMap() {
    }

    public String getErrorByKey(String key) {
        return errorMap.get(key);
    }

    public static class Builder {

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
