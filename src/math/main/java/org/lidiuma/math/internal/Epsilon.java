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

package org.lidiuma.math.internal;

public final class Epsilon {

    public static final float EPSILON_F32 = 1e-6f;
    public static final double EPSILON_F64 = 1e-12d;

    public static float clamp(float value) {
        return Math.abs(value) < EPSILON_F32 ? 0f : value;
    }

    public static double clamp(double value) {
        return Math.abs(value) < EPSILON_F64 ? 0d : value;
    }
}
