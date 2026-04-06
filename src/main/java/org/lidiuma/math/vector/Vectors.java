/*
 * Copyright (c) 2026 Xasmedy
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

package org.lidiuma.math.vector;

public value class Vectors {

    private Vectors() {}

    public static Vector1F vec1F(float x) {
        return new Vector1F(x);
    }

    public static Vector1D vec1D(double x) {
        return new Vector1D(x);
    }

    public static Vector1I vec1I(int x) {
        return new Vector1I(x);
    }

    public static Vector1L vec1D(long x) {
        return new Vector1L(x);
    }
}
