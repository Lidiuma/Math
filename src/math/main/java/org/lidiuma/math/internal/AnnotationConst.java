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

/// Internal non-exported class containing annotation output constants.\
/// Really useful to avoid mistakes that come from using strings directly.
public final class AnnotationConst {

    // Vector-related
    public static final String VECTOR_OUT = "Vectors";
    public static final String VEC1_FACTORY = "vec1";
    public static final String VEC2_FACTORY = "vec2";
    public static final String VEC3_FACTORY = "vec3";
    public static final String VEC4_FACTORY = "vec4";
    // Tuples-related
    public static final String TUPLES_OUT = "Tuples";
    public static final String DOUBLE1 = "double1";
    public static final String DOUBLE2 = "double2";
    public static final String DOUBLE3 = "double3";
    public static final String DOUBLE4 = "double4";
    public static final String FLOAT1 = "float1";
    public static final String FLOAT2 = "float2";
    public static final String FLOAT3 = "float3";
    public static final String FLOAT4 = "float4";
    public static final String INT1 = "int1";
    public static final String INT2 = "int2";
    public static final String INT3 = "int3";
    public static final String INT4 = "int4";
    public static final String LONG1 = "long1";
    public static final String LONG2 = "long2";
    public static final String LONG3 = "long3";
    public static final String LONG4 = "long4";
    // Points-related
    public static final String POINT_OUT = "Points";
    public static final String POINT1_FACTORY = "point1";
    public static final String POINT2_FACTORY = "point2";
    public static final String POINT3_FACTORY = "point3";
    public static final String POINT4_FACTORY = "point4";
    // Colors-related
    public static final String COLOR_OUT = "Colors";
    public static final String COLOR_FACTORY = "color";
    // Matrices-related
    public static final String MATRIX_OUT = "Matrices";
    public static final String AFFINE2_FACTORY = "affine2";
    public static final String AFFINE3_FACTORY = "affine3";
    public static final String MATRIX3_FACTORY = "matrix3";
    public static final String MATRIX4_FACTORY = "matrix4";
    public static final String UPPER_AFFINE2_FACTORY = "Affine2";
    public static final String UPPER_AFFINE3_FACTORY = "Affine3";
    public static final String UPPER_MATRIX3_FACTORY = "Matrix3";
    public static final String UPPER_MATRIX4_FACTORY = "Matrix4";
    // Rotations-related
    public static final String ROTATION_OUT = "Rotations";
    public static final String QUATERNION_FACTORY = "quaternion";
    public static final String AXIS_ANGLE_FACTORY = "axisAngle";
    public static final String SWING_TWIST_FACTORY = "swingTwist";
    public static final String ANGLE_FACTORY = "angle";
    // Re-used by multiple aliases.
    public static final String IDENTITY_FACTORY = "identity";
    public static final String ONE_FACTORY = "one";
    public static final String ZERO_FACTORY = "zero";

    // Explicit numeric types for re-use.
    public static final String I32 = "I32";
    public static final String I64 = "I64";
    public static final String F32 = "F32";
    public static final String F64 = "F64";
}
