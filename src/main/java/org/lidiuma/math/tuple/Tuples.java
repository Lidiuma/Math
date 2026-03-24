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

package org.lidiuma.math.tuple;

public value final class Tuples {

    private Tuples() {}

    public static boolean epsilonEquals(float value1, float value2, float epsilon) {
        if (epsilon < 0) throw new IllegalArgumentException("Epsilon cannot be negative.");
        return Math.abs(value1 - value2) <= epsilon;
    }

    public static boolean epsilonEquals(double value1, double value2, double epsilon) {
        if (epsilon < 0) throw new IllegalArgumentException("Epsilon cannot be negative.");
        return Math.abs(value1 - value2) <= epsilon;
    }

    public static Float1 float1(float x) {
        return new Float1(x);
    }

    public static Float2 float2(float x, float y) {
        return new Float2(x, y);
    }

    public static Float2 float2(float value) {
        return float2(value, value);
    }

    public static Float3 float3(float x, float y, float z) {
        return new Float3(x, y, z);
    }

    public static Float3 float3(float value) {
        return float3(value, value, value);
    }

    public static Float4 float4(float x, float y, float z, float w) {
        return new Float4(x, y, z, w);
    }

    public static Float4 float4(float value) {
        return float4(value, value, value, value);
    }

    public static Double1 double1(double x) {
        return new Double1(x);
    }

    public static Double2 double2(double x, double y) {
        return new Double2(x, y);
    }

    public static Double2 double2(double value) {
        return double2(value, value);
    }

    public static Double3 double3(double x, double y, double z) {
        return new Double3(x, y, z);
    }

    public static Double3 double3(double value) {
        return double3(value, value, value);
    }

    public static Double4 double4(double x, double y, double z, double w) {
        return new Double4(x, y, z, w);
    }

    public static Double4 double4(double value) {
        return double4(value, value, value, value);
    }
}
