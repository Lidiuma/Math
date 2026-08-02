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

package org.lidiuma.math.matrix;

import jdk.internal.vm.annotation.NullRestricted;
import org.lidiuma.math.api.matrix.Matrix3;
import jdk.internal.vm.annotation.LooselyConsistentValue;
import org.lidiuma.math.api.traits.matrix.Matrix3Ops;
import org.lidiuma.math.api.traits.vector.Vector3Ops;
import org.lidiuma.math.numerics.FloatNumeric;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.Alias;
import org.lidiuma.math.processor.NamedAlias;
import org.lidiuma.math.vector.Vec3F32;

/// @see Matrix3
@LooselyConsistentValue
@NamedAlias(methodName = "matrix3", outputClass = "Matrices")
public value record Matrix3F32(
        @NullRestricted Float m00, @NullRestricted Float m01, @NullRestricted Float m02,
        @NullRestricted Float m10, @NullRestricted Float m11, @NullRestricted Float m12,
        @NullRestricted Float m20, @NullRestricted Float m21, @NullRestricted Float m22
) implements Matrix3<Float> {

    @Alias(outputClass = "Matrices")
    public static final Ops WITNESS = new Ops();

    public static final class Ops implements Matrix3Ops<Matrix3F32, Vec3F32, Float> {

        @Override
        @AliasExclude
        public Matrix3F32 of(Float m00, Float m01, Float m02,
                             Float m10, Float m11, Float m12,
                             Float m20, Float m21, Float m22) {
            return new Matrix3F32(
                    m00, m01, m02,
                    m10, m11, m12,
                    m20, m21, m22
            );
        }

        @Override
        @AliasExclude
        public Vector3Ops<Vec3F32, Float> vectorOps() {
            return Vec3F32.WITNESS;
        }

        @Override
        @AliasExclude
        public FloatNumeric scalarOps() {
            return FloatNumeric.WITNESS;
        }

        @Override
        @AliasExclude
        public Matrix3F32 zero() {
            return Matrix3Ops.super.zero();
        }

        @Override
        @AliasExclude
        public Matrix3F32 one() {
            return Matrix3Ops.super.one();
        }

        @Override
        @AliasExclude
        public Matrix3F32 identity() {
            return Matrix3Ops.super.identity();
        }
    }

//    /// @return creates an identity matrix having the 3rd column set to the translation vector.
//    public static Matrix3F32 fromTranslation(Vector2F32 translation) {
//        final float x = translation.x();
//        final float y = translation.y();
//        return new Matrix3F32(
//                1f, 0f, x,
//                0f, 1f, y,
//                0f, 0f, 1f
//        );
//    }
//
//    /// @return a pure rotation matrix from the provided angle.
//    public static Matrix3F32 fromAffineRotation(Radians angle) {
//        final float cos = (float) Math.cos(angle.value());
//        final float sin = (float) Math.sin(angle.value());
//        return new Matrix3F32(
//                cos, -sin, 0f,
//                sin, cos, 0f,
//                0f, 0f, 1f
//        );
//    }
//
//    /// @return a pure rotation matrix from the provided quaternion.
//    public static Matrix3F32 fromRotation(QuatF32 rotation) {
//
//        final var rot = rotation.normalize();
//
//        final double xs = rot.x() * 2f, ys = rot.y() * 2f, zs = rot.z() * 2f;
//        final double wx = rot.w() * xs, wy = rot.w() * ys, wz = rot.w() * zs;
//        final double xx = rot.x() * xs, xy = rot.x() * ys, xz = rot.x() * zs;
//        final double yy = rot.y() * ys, yz = rot.y() * zs, zz = rot.z() * zs;
//
//        final double m00 = 1d - (yy + zz), m01 = xy - wz       , m02 = xz + wy;
//        final double m10 = xy + wz       , m11 = 1d - (xx + zz), m12 = yz - wx;
//        final double m20 = xz - wy       , m21 = yz + wx       , m22 = 1d - (xx + yy);
//        return new Matrix3F32(
//                (float) m00, (float) m01, (float) m02,
//                (float) m10, (float) m11, (float) m12,
//                (float) m20, (float) m21, (float) m22
//        );
//    }
//
//    /// @return a new pure scaling matrix.
//    public static Matrix3F32 fromScale(Vector2F32 scale) {
//        final float x = scale.x();
//        final float y = scale.y();
//        return new Matrix3F32(
//                x, 0f, 0f,
//                0f, y, 0f,
//                0f, 0f, 1f
//        );
//    }
//
//    /// @return a new transformation matrix from scale and translation.
//    public static Matrix3F32 fromST(Vector2F32 translation, Vector2F32 scale) {
//        return new Matrix3F32(
//                scale.x(), 0f, translation.x(),
//                0f, scale.y(), translation.y(),
//                0f, 0f, 1f
//        );
//    }
//
//    /// Creates a transformation matrix from affine translation, affine  rotation, and affine scale.
//    /// @return The transformation matrix.
//    /// @apiNote The rotation quaternion is normalized internally.
//    public static Matrix3F32 fromAffineTRS(Vector2F32 translation, Radians rotation, Vector2F32 scale) {
//        final var rot = fromAffineRotation(rotation);
//        final var scl = fromScale(scale);
//        final var rotScl = rot.affineMul(scl);
//        return fromTranslation(translation).affineMul(rotScl);
//    }
//
//    /// Creates a transformation matrix from translation, rotation, and scale.
//    /// @return The transformation matrix.
//    /// @apiNote The rotation quaternion is normalized internally.
//    public static Matrix3F32 fromTRS(Vector2F32 translation, QuatF32 rotation, Vector2F32 scale) {
//        final var rot = fromRotation(rotation);
//        final var scl = fromScale(scale);
//        final var rotScl = rot.mul(scl);
//        return fromTranslation(translation).mul(rotScl);
//    }
//
//    /// Creates a new matrix using the top-left 3x3 of the matrix4.
//    public static Matrix3F32 fromMatrix4(Matrix4F32 matrix) {
//        return new Matrix3F32(
//                matrix.m00(), matrix.m01(), matrix.m02(),
//                matrix.m10(), matrix.m11(), matrix.m12(),
//                matrix.m20(), matrix.m21(), matrix.m22()
//        );
//    }
//
//    /// @return a new pure shearing matrix.
//    public static Matrix3F32 fromShear(Vector2F32 shear) {
//        final float x = shear.x(), y = shear.y();
//        return new Matrix3F32(
//                1f, x,  0f,
//                y,  1f, 0f,
//                0f, 0f, 1f
//        );
//    }
}
