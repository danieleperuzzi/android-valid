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

package com.danieleperuzzi.valid.core.impl;

import android.support.annotation.MainThread;
import android.support.annotation.Nullable;

import com.danieleperuzzi.valid.core.BaseValidator;
import com.danieleperuzzi.valid.core.Validable;
import com.danieleperuzzi.valid.core.ValidatorObserver;
import com.danieleperuzzi.valid.core.ValidatorOptions;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SingleThreadValidator extends BaseValidator {

    private Executor executor;

    public SingleThreadValidator() {
        executor = Executors.newSingleThreadExecutor();
    }

    @Override
    @MainThread
    public void startValidation(Validable<?> value, ValidatorOptions options, @Nullable ValidatorObserver observer, Callback callback) {
        Runnable runnable = () -> {
          doValidation(value, options, observer, callback);
        };

        executor.execute(runnable);
    }
}