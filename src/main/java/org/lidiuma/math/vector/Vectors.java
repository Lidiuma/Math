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

    public static Vec1F32 vec1(float x) {
        return new Vec1F32(x);
    }

    public static Vec1F64 vec1(double x) {
        return new Vec1F64(x);
    }

    public static Vec2F32 vec2(float x, float y) {
        return new Vec2F32(x, y);
    }

    public static Vec2F64 vec2(double x, double y) {
        return new Vec2F64(x, y);
    }

    public static Vec3F32 vec3(float x, float y, float z) {
        return new Vec3F32(x, y, z);
    }

    public static Vec3F64 vec3(double x, double y, double z) {
        return new Vec3F64(x, y, z);
    }

    public static Vec4F32 vec3(float x, float y, float z, float w) {
        return new Vec4F32(x, y, z, w);
    }

    public static Vec4F64 vec3(double x, double y, double z, double w) {
        return new Vec4F64(x, y, z, w);
    }
}
