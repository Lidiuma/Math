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
import org.lidiuma.math.api.matrix.Matrix4;
import jdk.internal.vm.annotation.LooselyConsistentValue;
import org.lidiuma.math.api.traits.matrix.Matrix4Ops;
import org.lidiuma.math.api.traits.vector.Vector4Ops;
import org.lidiuma.math.numerics.FloatNumeric;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.Alias;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.vector.Vec4F32;

/// @see Matrix4
@LooselyConsistentValue
@FactoryAlias(methodName = "matrix4", outputClass = "Matrices")
public value record Matrix4F32(
        // I'm not using an array because it's an identity object, and this reads and feels better to work with.
        @NullRestricted Float m00, @NullRestricted Float m01, @NullRestricted Float m02, @NullRestricted Float m03,
        @NullRestricted Float m10, @NullRestricted Float m11, @NullRestricted Float m12, @NullRestricted Float m13,
        @NullRestricted Float m20, @NullRestricted Float m21, @NullRestricted Float m22, @NullRestricted Float m23,
        @NullRestricted Float m30, @NullRestricted Float m31, @NullRestricted Float m32, @NullRestricted Float m33
) implements Matrix4<Float> {

    @Alias(outputClass = "Matrices")
    public static final Ops WITNESS = new Ops();

    public static final class Ops implements Matrix4Ops<Matrix4F32, Vec4F32, Float> {

        @Override
        @AliasExclude
        public Matrix4F32 of(Float m00, Float m01, Float m02, Float m03,
                             Float m10, Float m11, Float m12, Float m13,
                             Float m20, Float m21, Float m22, Float m23,
                             Float m30, Float m31, Float m32, Float m33) {
            return new Matrix4F32(
                    m00, m01, m02, m03,
                    m10, m11, m12, m13,
                    m20, m21, m22, m23,
                    m30, m31, m32, m33
            );
        }

        @Override
        @AliasExclude
        public Vector4Ops<Vec4F32, Float> vectorOps() {
            return Vec4F32.WITNESS;
        }

        @Override
        @AliasExclude
        public FloatNumeric scalarOps() {
            return FloatNumeric.WITNESS;
        }

        @Override
        @AliasExclude
        public Matrix4F32 zero() {
            return Matrix4Ops.super.zero();
        }

        @Override
        @AliasExclude
        public Matrix4F32 one() {
            return Matrix4Ops.super.one();
        }

        @Override
        @AliasExclude
        public Matrix4F32 identity() {
            return Matrix4Ops.super.identity();
        }
    }

//    /// Creates a transformation matrix from a translation and rotation.
//    /// @return The transformation matrix.
//    /// @apiNote The rotation quaternion is normalized internally.
//    public static Matrix4F32 fromTR(Vector3F32 translation, QuatF32 rotation) {
//
//        final var rot = rotation.normalize();
//
//        final double xs = rot.x() * 2f, ys = rot.y() * 2f, zs = rot.z() * 2f;
//        final double wx = rot.w() * xs, wy = rot.w() * ys, wz = rot.w() * zs;
//        final double xx = rot.x() * xs, xy = rot.x() * ys, xz = rot.x() * zs;
//        final double yy = rot.y() * ys, yz = rot.y() * zs, zz = rot.z() * zs;
//
//        final double m00 = 1f - (yy + zz), m01 = xy - wz       , m02 = xz + wy       , m03 = translation.x();
//        final double m10 = xy + wz       , m11 = 1f - (xx + zz), m12 = yz - wx       , m13 = translation.y();
//        final double m20 = xz - wy       , m21 = yz + wx       , m22 = 1f - (xx + yy), m23 = translation.z();
//        final double m30 = 0f            , m31 = 0f            , m32 = 0f            , m33 = 1f;
//        return new Matrix4F32(
//                (float) m00, (float) m01, (float) m02, (float) m03,
//                (float) m10, (float) m11, (float) m12, (float) m13,
//                (float) m20, (float) m21, (float) m22, (float) m23,
//                (float) m30, (float) m31, (float) m32, (float) m33
//        );
//    }
//
//    /// @return a new rotation matrix around the given axis.
//    public static Matrix4F32 fromAxisAngle(Vector3F32 axis, Radians angle) {
//        if (angle.value() == 0) return identity();
//        final var quat = QuatF32.fromAxisAngle(axis.asF64(), angle);
//        return fromRotation(quat);
//    }
//
//    /// @return a pure rotation matrix from the quaternion.
//    public static Matrix4F32 fromRotation(QuatF32 quaternion) {
//        return fromTR(new Vector3F32(0f, 0f, 0f), quaternion);
//    }
//
//    /// @return a new rotation matrix that aligns `v1` direction with `v2` direction.
//    public static Matrix4F32 fromRotationBetween(Vector3F32 v1, Vector3F32 v2) {
//        final var quat = QuatF32.fromRotationBetween(v1.asF64(), v2.asF64());
//        return fromRotation(quat);
//    }
//
//    /// @return a new rotation matrix from the given Euler angles.
//    public static Matrix4F32 fromEulerAngles(Radians yaw, Radians pitch, Radians roll) {
//        final var quat = QuatF32.fromEulerAngles(yaw, pitch, roll);
//        return fromRotation(quat);
//    }
//
//    /// Creates a transformation matrix from translation, rotation, and scale.
//    /// @return The transformation matrix.
//    /// @apiNote The rotation quaternion is normalized internally.
//    public static Matrix4F32 fromTRS(Vector3F32 translation, QuatF32 rotation, Vector3F32 scale) {
//        return fromTR(translation, rotation).scale(scale);
//    }
//
//    /// Creates a matrix from three axes and a translation vector.
//    /// @return a matrix representing the given axes and translation.
//    /// @apiNote
//    /// |   |   |   |             |
//    /// |:-:|:-:|:-:|:-----------:|
//    /// | x | x | x | x-translation |
//    /// | y | y | y | y-translation |
//    /// | z | z | z | z-translation |
//    /// | 0 | 0 | 0 |      1       |
//    public static Matrix4F32 fromAxes(Vector3F32 xAxis, Vector3F32 yAxis, Vector3F32 zAxis, Vector3F32 translation) {
//        return new Matrix4F32(
//                xAxis.x(), xAxis.y(), xAxis.z(), translation.x(),
//                yAxis.x(), yAxis.y(), yAxis.z(), translation.y(),
//                zAxis.x(), zAxis.y(), zAxis.z(), translation.z(),
//                0f, 0f, 0f, 1f
//        );
//    }
//
//    /// Creates a projection matrix with a near and far plane, a field of view, and an aspect ratio.
//    /// @param near The near plane.
//    /// @param far The far plane.
//    /// @param fovY The field of view of the height.
//    /// @param aspectRatio The aspect ratio.
//    /// @apiNote Only the vertical FOV is specified, the horizontal FOV is derived from the aspect ratio.
//    public static Matrix4F32 fromProjection(float near, float far, Radians fovY, float aspectRatio) {
//        final float focalLen = (float) (1f / Math.tan(fovY.value() / 2f));
//        final float m00 = focalLen / aspectRatio;
//        final float m22 = (far + near) / (near - far);
//        final float m33 = (2f * far * near) / (near - far);
//        return new Matrix4F32(
//                m00, 0f, 0f, 0f,
//                0f, focalLen, 0f, 0f,
//                0f, 0f, m22, m33,
//                0f, 0f, -1f, 0f
//        );
//    }
//
//    /// Creates an off-center perspective projection matrix.\
//    /// Useful for asymmetric frustums (off-center projections), e.g., stereo rendering or shadows.
//    /// @param left The X coordinate on the near plane that maps to the left of the viewport.
//    /// @param right The X coordinate on the near plane that maps to the right of the viewport.
//    /// @param bottom The Y coordinate on the near plane that maps to the bottom of the viewport.
//    /// @param top The Y coordinate on the near plane that maps to the top of the viewport.
//    /// @param near The distance to the near clipping plane (must be positive).
//    /// @param far The distance to the far clipping plane (must be positive and greater than near).
//    /// @return the projection matrix that maps the specified frustum to normalized device coordinates.
//    public static Matrix4F32 fromProjection(float left, float right, float bottom, float top, float near, float far) {
//        float m00 = 2f * near / (right - left); // X offset.
//        float m11 = 2f * near / (top - bottom); // Y offset.
//        float m02 = (right + left) / (right - left);
//        float m12 = (top + bottom) / (top - bottom);
//        float m22 = (far + near) / (near - far);
//        float m23 = (2f * far * near) / (near - far);
//        return new Matrix4F32(
//                m00, 0f, m02, 0f,
//                0f, m11, m12, 0f,
//                0f, 0f, m22, m23,
//                0f, 0f, -1f, 0f
//        );
//    }
//
//    /// Creates an orthographic projection matrix, equivalent to OpenGL's glOrtho ([docs](https://registry.khronos.org/OpenGL-Refpages/gl2.1/xhtml/glOrtho.xml)).
//    /// @param left   The left clipping plane (x-coordinate)
//    /// @param right  The right clipping plane (x-coordinate)
//    /// @param bottom The bottom clipping plane (y-coordinate)
//    /// @param top    The top clipping plane (y-coordinate)
//    /// @param near   The near clipping plane (z-coordinate, must be less than far)
//    /// @param far    The far clipping plane (z-coordinate, must be greater than near)
//    /// @return       the new matrix representing the orthographic projection.
//    public static Matrix4F32 fromOrtho(float left, float right, float bottom, float top, float near, float far) {
//
//        final float xOrtho =  2f / (right - left);
//        final float yOrtho =  2f / (top - bottom);
//        final float zOrtho = -2f / (far - near);
//
//        final float tx = -(right + left) / (right - left);
//        final float ty = -(top + bottom) / (top - bottom);
//        final float tz = -(far + near) / (far - near);
//
//        return new Matrix4F32(
//                xOrtho, 0f, 0f, tx,
//                0f, yOrtho, 0f, ty,
//                0f, 0f, zOrtho, tz,
//                0f, 0f, 0f, 1f
//        );
//    }
//
//    /// Creates an orthographic projection matrix whose lower‑left corner is {@code origin},
//    /// extending {@code width} horizontally and {@code height} vertically.
//    /// @param width   horizontal size (must be positive)
//    /// @param height  vertical size (must be positive)
//    /// @param near   The near clipping plane (z-coordinate, must be less than far)
//    /// @param far    The far clipping plane (z-coordinate, must be greater than near)
//    /// @return       the new matrix representing the 2D orthographic projection.
//    public static Matrix4F32 fromOrtho2D(Vector2F32 origin, float width, float height, float near, float far) {
//        return fromOrtho(origin.x(), origin.x() + width, origin.y(), origin.y() + height, near, far);
//    }
//
//    /// Creates an orthographic projection matrix whose lower‑left corner is {@code origin},
//    /// extending {@code width} horizontally and {@code height} vertically.
//    ///
//    /// The near plane is set to 0, and the far plane is set to 1.
//    /// @param width   horizontal size (must be positive)
//    /// @param height  vertical size (must be positive)
//    /// @return       the new matrix representing the 2D orthographic projection.
//    public static Matrix4F32 fromOrtho2D(Vector2F32 origin, float width, float height) {
//        return fromOrtho(origin.x(), origin.x() + width, origin.y(), origin.y() + height, 0, 1);
//    }
//
//    /// @return creates an identity matrix having the 4th column set to the translation vector.
//    public static Matrix4F32 fromTranslation(Vector3F32 translation) {
//        return new Matrix4F32(
//                1f, 0f, 0f, translation.x(),
//                0f, 1f, 0f, translation.y(),
//                0f, 0f, 1f, translation.z(),
//                0f, 0f, 0f, 1f
//        );
//    }
//
//    /// @return creates an identity matrix having the 4th column set to the translation vector and the scaling vector in the diagonal.
//    public static Matrix4F32 fromTranslation(Vector3F32 translation, Vector3F32 scaling) {
//        final Matrix4F32 m = fromTranslation(translation);
//        final float m00 = scaling.x();
//        final float m11 = scaling.y();
//        final float m22 = scaling.z();
//        return new Matrix4F32(
//                m00  , m.m01, m.m02, m.m03,
//                m.m10, m11  , m.m12, m.m13,
//                m.m20, m.m21, m22  , m.m23,
//                m.m30, m.m31, m.m32, m.m33
//        );
//    }
//
//    /// @return a new pure scaling matrix.
//    public static Matrix4F32 fromScale(Vector3F32 scale) {
//        final Matrix4F32 i = identity();
//        return new Matrix4F32(
//                scale.x(), i.m01      , i.m02      , i.m03,
//                i.m10      , scale.y(), i.m12      , i.m13,
//                i.m20      , i.m21      , scale.z(), i.m23,
//                i.m30      , i.m31      , i.m32      , i.m33
//        );
//    }
//
//    /// Creates a view rotation matrix from a view direction and an up vector.
//    /// This matrix contains rotation only; combine with a translation to form a full view matrix.
//    public static Matrix4F32 fromLookRotation(Vector3F32 direction, Vector3F32 up) {
//
//        final var f = direction.normalize();   // forward
//        final var r = f.cross(up).normalize(); // right
//        final var u = r.cross(f).normalize();  // true up
//
//        final Matrix4F32 i = identity();
//        return new Matrix4F32(
//                 r.x(),  r.y(),  r.z(), i.m03,
//                 u.x(),  u.y(),  u.z(), i.m13,
//                -f.x(), -f.y(), -f.z(), i.m23,
//                 i.m30,  i.m31,  i.m32, i.m33
//        );
//    }
//
//    /// Creates a view (camera) matrix that looks from `position` towards `target`, using `up` as the up direction.
//    ///
//    /// The resulting matrix transforms world-space coordinates into view space.
//    public static Matrix4F32 fromLookAt(Vector3F32 position, Vector3F32 target, Vector3F32 up) {
//        final var direction = target.sub(position);
//        final Matrix4F32 rotation = fromLookRotation(direction, up);
//        final Matrix4F32 translation = fromTranslation(position.mul(-1f));
//        return rotation.mul(translation);
//    }
//
//    public static Matrix4F32 fromWorld(Vector3F32 position, Vector3F32 forward, Vector3F32 up) {
//        final var f = forward.normalize();     // forward
//        final var r = f.cross(up).normalize(); // right
//        final var u = r.cross(f).normalize();  // true Up
//        return fromAxes(r, u, f.mul(-1f), position);
//    }
//
//    /// Creates a new Matrix from the 3x3 matrix, with the missing elements copied from the identity matrix.
//    public static Matrix4F32 fromMatrix3(Matrix3F32 matrix) {
//        return new Matrix4F32(
//                matrix.m00(), matrix.m01(), matrix.m02(), 0f,
//                matrix.m10(), matrix.m11(), matrix.m12(), 0f,
//                matrix.m20(), matrix.m21(), matrix.m22(), 0f,
//                0f, 0f, 0f, 1f
//        );
//    }
}
