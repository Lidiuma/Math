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

    public static Vector1F vec1(float x) {
        return new Vector1F(x);
    }

    public static Vector1D vec1(double x) {
        return new Vector1D(x);
    }

    public static Vector2F vec2(float x, float y) {
        return new Vector2F(x, y);
    }

    public static Vector2D vec2(double x, double y) {
        return new Vector2D(x, y);
    }

    public static Vector3F vec3(float x, float y, float z) {
        return new Vector3F(x, y, z);
    }

    public static Vector3D vec3(double x, double y, double z) {
        return new Vector3D(x, y, z);
    }

    public static Vector4F vec3(float x, float y, float z, float w) {
        return new Vector4F(x, y, z, w);
    }

    public static Vector4D vec3(double x, double y, double z, double w) {
        return new Vector4D(x, y, z, w);
    }
}
