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

package com.danieleperuzzi.valid.core.validator.impl;

import android.os.Handler;
import android.os.Looper;

import com.danieleperuzzi.valid.core.validator.BaseValidator;
import com.danieleperuzzi.valid.core.validator.ValidatorAlgorithmFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PoolThreadValidator extends BaseValidator {

    private static int cpuNum = Runtime.getRuntime().availableProcessors();

    private static final ValidatorAlgorithmFactory factory = new ValidatorAlgorithmFactory();
    private static final Looper mainThreadLooper = Looper.getMainLooper();
    private static final Handler mainThreadHandler = new Handler(mainThreadLooper);

    public PoolThreadValidator() {
        this(Executors.newFixedThreadPool(cpuNum), factory, mainThreadHandler);
    }

    protected PoolThreadValidator(Executor executor, ValidatorAlgorithmFactory factory, Handler mainThreadHandler) {
        super(executor, factory, mainThreadHandler);
    }
}
