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

/**
 * Every time we want to know the global status of a collection of
 * {@link Validable} we can expect only these two outputs: if at least one
 * of them does not pass the validation then the entire process fails.
 */
public enum ValidableCollectionStatus {
    ALL_VALID,
    AT_LEAST_ONE_NOT_VALID
}