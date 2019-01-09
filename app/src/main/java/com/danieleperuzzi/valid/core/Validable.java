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

import android.support.annotation.Nullable;

/**
 * An object used to hold the value that is going to be validated.
 *
 * <p>It is useful in order to mantain code more generic as possible</p>
 *
 * @param <V>   it can be anything, fromm String to complex Object
 */
public interface Validable<V> {

    /**
     * @return  the current value held by this validable
     */
    V getValue();

    /**
     * @return  a tag that identifies the value
     */
    String getTag();

    /**
     * Used to set in an atomic way the value held by this
     * validable and an optional tag to identify it
     *
     * @param value the value to assign to this validable
     * @param tag   the tag that specifies the value
     */
    void setValue(V value, @Nullable String tag);
}
