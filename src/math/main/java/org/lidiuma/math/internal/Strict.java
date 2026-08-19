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

public final class Strict {

    public static final float EPSILON_F32 = 1e-6f;
    public static final float EPSILON_F64 = 1e-12f;

    public static float fix(float value) {
        return Math.abs(value) < EPSILON_F32 ? 0f : value;
    }

    public static double fix(double value) {
        return Math.abs(value) < EPSILON_F64 ? 0d : value;
    }

    public static double cos(double value) {
        return fix(Math.cos(value));
    }

    public static double sin(double value) {
        return fix(Math.sin(value));
    }

    public static float cos(float value) {
        return fix((float) Math.cos(value)); // If I cast after the cos(), the cast loses precision and goes bellow epsilon.
    }

    public static float sin(float value) {
        return fix((float) Math.sin(value)); // If I cast after the sin(), the cast loses precision and goes bellow epsilon.
    }
}
