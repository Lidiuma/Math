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

import org.lidiuma.math.rotation.Quaternion;
import org.lidiuma.math.rotation.Radians;
import org.lidiuma.math.vector.v2.Vector2F32;
import org.lidiuma.math.vector.v3.Vector3F32;
import jdk.internal.vm.annotation.LooselyConsistentValue;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import static org.lidiuma.math.FloatingUtil.EPSILON;

/// @see Matrix4
@SuppressWarnings("unused")
@LooselyConsistentValue
public value record Matrix4F32(
        // I'm not using an array because it's an identity object, and this reads and feels better to work with.
        float m00, float m01, float m02, float m03,
        float m10, float m11, float m12, float m13,
        float m20, float m21, float m22, float m23,
        float m30, float m31, float m32, float m33
) implements Matrix4<Matrix4F32, Float, Vector3F32> {

    public static final int M00 = 0, M01 = 4, M02 =  8, M03 = 12;
    public static final int M10 = 1, M11 = 5, M12 =  9, M13 = 13;
    public static final int M20 = 2, M21 = 6, M22 = 10, M23 = 14;
    public static final int M30 = 3, M31 = 7, M32 = 11, M33 = 15;

    public static Matrix4F32 identity() {
        return new Matrix4F32(
                1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                0f, 0f, 0f, 1f
        );
    }

    /// Creates a new matrix from the given {@link MemorySegment} starting at the specified logical index.\
    /// The memory segment must be able to hold *at least* `(index + 1) * `{@link #byteSize()}.
    /// @param segment the memory segment to copy from.
    /// @param index the logical index in units of {@link #byteSize()} where copying begins.
    /// @apiNote The memory segment must be stored in [column-major](https://en.wikipedia.org/wiki/Row-_and_column-major_order) order.
    public static Matrix4F32 fromMemorySegment(MemorySegment segment, long index) {

        final var layout = ValueLayout.JAVA_FLOAT;
        final long baseIndex = index * SIZE;

        final float m00 = segment.getAtIndex(layout, baseIndex + M00);
        final float m10 = segment.getAtIndex(layout, baseIndex + M10);
        final float m20 = segment.getAtIndex(layout, baseIndex + M20);
        final float m30 = segment.getAtIndex(layout, baseIndex + M30);

        final float m01 = segment.getAtIndex(layout, baseIndex + M01);
        final float m11 = segment.getAtIndex(layout, baseIndex + M11);
        final float m21 = segment.getAtIndex(layout, baseIndex + M21);
        final float m31 = segment.getAtIndex(layout, baseIndex + M31);

        final float m02 = segment.getAtIndex(layout, baseIndex + M02);
        final float m12 = segment.getAtIndex(layout, baseIndex + M12);
        final float m22 = segment.getAtIndex(layout, baseIndex + M22);
        final float m32 = segment.getAtIndex(layout, baseIndex + M32);

        final float m03 = segment.getAtIndex(layout, baseIndex + M03);
        final float m13 = segment.getAtIndex(layout, baseIndex + M13);
        final float m23 = segment.getAtIndex(layout, baseIndex + M23);
        final float m33 = segment.getAtIndex(layout, baseIndex + M33);

        return new Matrix4F32(
                m00, m01, m02, m03,
                m10, m11, m12, m13,
                m20, m21, m22, m23,
                m30, m31, m32, m33
        );
    }

    /// Creates a transformation matrix from a translation and rotation.
    /// @return The transformation matrix.
    /// @apiNote The rotation quaternion is normalized internally.
    public static Matrix4F32 fromTR(Vector3F32 translation, Quaternion rotation) {

        final var rot = rotation.normalize();

        final double xs = rot.x() * 2f, ys = rot.y() * 2f, zs = rot.z() * 2f;
        final double wx = rot.w() * xs, wy = rot.w() * ys, wz = rot.w() * zs;
        final double xx = rot.x() * xs, xy = rot.x() * ys, xz = rot.x() * zs;
        final double yy = rot.y() * ys, yz = rot.y() * zs, zz = rot.z() * zs;

        final double m00 = 1f - (yy + zz), m01 = xy - wz       , m02 = xz + wy       , m03 = translation.x();
        final double m10 = xy + wz       , m11 = 1f - (xx + zz), m12 = yz - wx       , m13 = translation.y();
        final double m20 = xz - wy       , m21 = yz + wx       , m22 = 1f - (xx + yy), m23 = translation.z();
        final double m30 = 0f            , m31 = 0f            , m32 = 0f            , m33 = 1f;
        return new Matrix4F32(
                (float) m00, (float) m01, (float) m02, (float) m03,
                (float) m10, (float) m11, (float) m12, (float) m13,
                (float) m20, (float) m21, (float) m22, (float) m23,
                (float) m30, (float) m31, (float) m32, (float) m33
        );
    }

    /// @return a new rotation matrix around the given axis.
    public static Matrix4F32 fromAxisAngle(Vector3F32 axis, Radians angle) {
        if (angle.value() == 0) return identity();
        final var quat = Quaternion.fromAxisAngle(axis.asF64(), angle);
        return fromRotation(quat);
    }

    /// @return a pure rotation matrix from the quaternion.
    public static Matrix4F32 fromRotation(Quaternion quaternion) {
        return fromTR(new Vector3F32(0f, 0f, 0f), quaternion);
    }

    /// @return a new rotation matrix that aligns `v1` direction with `v2` direction.
    public static Matrix4F32 fromRotationBetween(Vector3F32 v1, Vector3F32 v2) {
        final var quat = Quaternion.fromRotationBetween(v1.asF64(), v2.asF64());
        return fromRotation(quat);
    }

    /// @return a new rotation matrix from the given Euler angles.
    public static Matrix4F32 fromEulerAngles(Radians yaw, Radians pitch, Radians roll) {
        final var quat = Quaternion.fromEulerAngles(yaw, pitch, roll);
        return fromRotation(quat);
    }

    /// Creates a transformation matrix from translation, rotation, and scale.
    /// @return The transformation matrix.
    /// @apiNote The rotation quaternion is normalized internally.
    public static Matrix4F32 fromTRS(Vector3F32 translation, Quaternion rotation, Vector3F32 scale) {
        return fromTR(translation, rotation).scale(scale);
    }

    /// Creates a matrix from three axes and a translation vector.
    /// @return a matrix representing the given axes and translation.
    /// @apiNote
    /// |   |   |   |             |
    /// |:-:|:-:|:-:|:-----------:|
    /// | x | x | x | x-translation |
    /// | y | y | y | y-translation |
    /// | z | z | z | z-translation |
    /// | 0 | 0 | 0 |      1       |
    public static Matrix4F32 fromAxes(Vector3F32 xAxis, Vector3F32 yAxis, Vector3F32 zAxis, Vector3F32 translation) {
        return new Matrix4F32(
                xAxis.x(), xAxis.y(), xAxis.z(), translation.x(),
                yAxis.x(), yAxis.y(), yAxis.z(), translation.y(),
                zAxis.x(), zAxis.y(), zAxis.z(), translation.z(),
                0, 0, 0, 1
        );
    }

    /// Creates a projection matrix with a near and far plane, a field of view, and an aspect ratio.
    /// @param near The near plane.
    /// @param far The far plane.
    /// @param fovY The field of view of the height.
    /// @param aspectRatio The aspect ratio.
    /// @apiNote Only the vertical FOV is specified, the horizontal FOV is derived from the aspect ratio.
    public static Matrix4F32 fromProjection(float near, float far, Radians fovY, float aspectRatio) {
        final float focalLen = (float) (1f / Math.tan(fovY.value() / 2f));
        final float m00 = focalLen / aspectRatio;
        final float m22 = (far + near) / (near - far);
        final float m33 = (2f * far * near) / (near - far);
        return new Matrix4F32(
                m00, 0, 0, 0,
                0, focalLen, 0, 0,
                0, 0, m22, m33,
                0, 0, -1, 0
        );
    }

    /// Creates an off-center perspective projection matrix.\
    /// Useful for asymmetric frustums (off-center projections), e.g., stereo rendering or shadows.
    /// @param left The X coordinate on the near plane that maps to the left of the viewport.
    /// @param right The X coordinate on the near plane that maps to the right of the viewport.
    /// @param bottom The Y coordinate on the near plane that maps to the bottom of the viewport.
    /// @param top The Y coordinate on the near plane that maps to the top of the viewport.
    /// @param near The distance to the near clipping plane (must be positive).
    /// @param far The distance to the far clipping plane (must be positive and greater than near).
    /// @return the projection matrix that maps the specified frustum to normalized device coordinates.
    public static Matrix4F32 fromProjection(float left, float right, float bottom, float top, float near, float far) {
        float m00 = 2f * near / (right - left); // X offset.
        float m11 = 2f * near / (top - bottom); // Y offset.
        float m02 = (right + left) / (right - left);
        float m12 = (top + bottom) / (top - bottom);
        float m22 = (far + near) / (near - far);
        float m23 = (2f * far * near) / (near - far);
        return new Matrix4F32(
                m00, 0, m02, 0,
                0, m11, m12, 0,
                0, 0, m22, m23,
                0, 0, -1, 0
        );
    }

    /// Creates an orthographic projection matrix, equivalent to OpenGL's glOrtho ([docs](https://registry.khronos.org/OpenGL-Refpages/gl2.1/xhtml/glOrtho.xml)).
    /// @param left   The left clipping plane (x-coordinate)
    /// @param right  The right clipping plane (x-coordinate)
    /// @param bottom The bottom clipping plane (y-coordinate)
    /// @param top    The top clipping plane (y-coordinate)
    /// @param near   The near clipping plane (z-coordinate, must be less than far)
    /// @param far    The far clipping plane (z-coordinate, must be greater than near)
    /// @return       the new matrix representing the orthographic projection.
    public static Matrix4F32 fromOrtho(float left, float right, float bottom, float top, float near, float far) {

        final float xOrtho =  2f / (right - left);
        final float yOrtho =  2f / (top - bottom);
        final float zOrtho = -2f / (far - near);

        final float tx = -(right + left) / (right - left);
        final float ty = -(top + bottom) / (top - bottom);
        final float tz = -(far + near) / (far - near);

        return new Matrix4F32(
                xOrtho, 0, 0, tx,
                0, yOrtho, 0, ty,
                0, 0, zOrtho, tz,
                0, 0, 0, 1
        );
    }

    /// Creates an orthographic projection matrix whose lower‑left corner is {@code origin},
    /// extending {@code width} horizontally and {@code height} vertically.
    /// @param width   horizontal size (must be positive)
    /// @param height  vertical size (must be positive)
    /// @param near   The near clipping plane (z-coordinate, must be less than far)
    /// @param far    The far clipping plane (z-coordinate, must be greater than near)
    /// @return       the new matrix representing the 2D orthographic projection.
    public static Matrix4F32 fromOrtho2D(Vector2F32 origin, float width, float height, float near, float far) {
        return fromOrtho(origin.x(), origin.x() + width, origin.y(), origin.y() + height, near, far);
    }

    /// Creates an orthographic projection matrix whose lower‑left corner is {@code origin},
    /// extending {@code width} horizontally and {@code height} vertically.
    ///
    /// The near plane is set to 0, and the far plane is set to 1.
    /// @param width   horizontal size (must be positive)
    /// @param height  vertical size (must be positive)
    /// @return       the new matrix representing the 2D orthographic projection.
    public static Matrix4F32 fromOrtho2D(Vector2F32 origin, float width, float height) {
        return fromOrtho(origin.x(), origin.x() + width, origin.y(), origin.y() + height, 0, 1);
    }

    /// @return creates an identity matrix having the 4th column set to the translation vector.
    public static Matrix4F32 fromTranslation(Vector3F32 translation) {
        return new Matrix4F32(
                1, 0, 0, translation.x(),
                0, 1, 0, translation.y(),
                0, 0, 1, translation.z(),
                0, 0, 0, 1
        );
    }

    /// @return creates an identity matrix having the 4th column set to the translation vector and the scaling vector in the diagonal.
    public static Matrix4F32 fromTranslation(Vector3F32 translation, Vector3F32 scaling) {
        final Matrix4F32 m = fromTranslation(translation);
        final float m00 = scaling.x();
        final float m11 = scaling.y();
        final float m22 = scaling.z();
        return new Matrix4F32(
                m00  , m.m01, m.m02, m.m03,
                m.m10, m11  , m.m12, m.m13,
                m.m20, m.m21, m22  , m.m23,
                m.m30, m.m31, m.m32, m.m33
        );
    }

    /// @return a new pure scaling matrix.
    public static Matrix4F32 fromScale(Vector3F32 scale) {
        final Matrix4F32 i = identity();
        return new Matrix4F32(
                scale.x(), i.m01      , i.m02      , i.m03,
                i.m10      , scale.y(), i.m12      , i.m13,
                i.m20      , i.m21      , scale.z(), i.m23,
                i.m30      , i.m31      , i.m32      , i.m33
        );
    }

    /// Creates a view rotation matrix from a view direction and an up vector.
    /// This matrix contains rotation only; combine with a translation to form a full view matrix.
    public static Matrix4F32 fromLookRotation(Vector3F32 direction, Vector3F32 up) {

        final var f = direction.normalize();   // forward
        final var r = f.cross(up).normalize(); // right
        final var u = r.cross(f).normalize();  // true up

        final Matrix4F32 i = identity();
        return new Matrix4F32(
                 r.x(),  r.y(),  r.z(), i.m03,
                 u.x(),  u.y(),  u.z(), i.m13,
                -f.x(), -f.y(), -f.z(), i.m23,
                 i.m30,  i.m31,  i.m32, i.m33
        );
    }

    /// Creates a view (camera) matrix that looks from `position` towards `target`, using `up` as the up direction.
    ///
    /// The resulting matrix transforms world-space coordinates into view space.
    public static Matrix4F32 fromLookAt(Vector3F32 position, Vector3F32 target, Vector3F32 up) {
        final var direction = target.sub(position);
        final Matrix4F32 rotation = fromLookRotation(direction, up);
        final Matrix4F32 translation = fromTranslation(position.mul(-1f));
        return rotation.mul(translation);
    }

    public static Matrix4F32 fromWorld(Vector3F32 position, Vector3F32 forward, Vector3F32 up) {
        final var f = forward.normalize();     // forward
        final var r = f.cross(up).normalize(); // right
        final var u = r.cross(f).normalize();  // true Up
        return fromAxes(r, u, f.mul(-1f), position);
    }

    /// Creates a new Matrix from the 3x3 matrix, with the missing elements copied from the identity matrix.
    public static Matrix4F32 fromMatrix3(Matrix3F32 matrix) {
        return new Matrix4F32(
                matrix.m00(), matrix.m01(), matrix.m02(), 0,
                matrix.m10(), matrix.m11(), matrix.m12(), 0,
                matrix.m20(), matrix.m21(), matrix.m22(), 0,
                0, 0, 0, 1
        );
    }

    @Override
    public long byteSize() {
        return (long) SIZE * Float.SIZE;
    }

    @Override
    public Matrix4F32 add(Matrix4F32 other) {
        return new Matrix4F32(
                m00 + other.m00, m01 + other.m01, m02 + other.m02, m03 + other.m03,
                m10 + other.m10, m11 + other.m11, m12 + other.m12, m13 + other.m13,
                m20 + other.m20, m21 + other.m21, m22 + other.m22, m23 + other.m23,
                m30 + other.m30, m31 + other.m31, m32 + other.m32, m33 + other.m33
        );
    }

    @Override
    public Matrix4F32 sub(Matrix4F32 other) {
        return new Matrix4F32(
                m00 - other.m00, m01 - other.m01, m02 - other.m02, m03 - other.m03,
                m10 - other.m10, m11 - other.m11, m12 - other.m12, m13 - other.m13,
                m20 - other.m20, m21 - other.m21, m22 - other.m22, m23 - other.m23,
                m30 - other.m30, m31 - other.m31, m32 - other.m32, m33 - other.m33
        );
    }

    @Override
    public Matrix4F32 mul(Float scalar) {
        return new Matrix4F32(
                m00 * scalar, m01 * scalar, m02 * scalar, m03 * scalar,
                m10 * scalar, m11 * scalar, m12 * scalar, m13 * scalar,
                m20 * scalar, m21 * scalar, m22 * scalar, m23 * scalar,
                m30 * scalar, m31 * scalar, m32 * scalar, m33 * scalar
        );
    }

    @Override
    public Matrix4F32 mul(Matrix4F32 other) {
        final float n00 = m00 * other.m00 + m01 * other.m10 + m02 * other.m20 + m03 * other.m30;
        final float n01 = m00 * other.m01 + m01 * other.m11 + m02 * other.m21 + m03 * other.m31;
        final float n02 = m00 * other.m02 + m01 * other.m12 + m02 * other.m22 + m03 * other.m32;
        final float n03 = m00 * other.m03 + m01 * other.m13 + m02 * other.m23 + m03 * other.m33;
        final float n10 = m10 * other.m00 + m11 * other.m10 + m12 * other.m20 + m13 * other.m30;
        final float n11 = m10 * other.m01 + m11 * other.m11 + m12 * other.m21 + m13 * other.m31;
        final float n12 = m10 * other.m02 + m11 * other.m12 + m12 * other.m22 + m13 * other.m32;
        final float n13 = m10 * other.m03 + m11 * other.m13 + m12 * other.m23 + m13 * other.m33;
        final float n20 = m20 * other.m00 + m21 * other.m10 + m22 * other.m20 + m23 * other.m30;
        final float n21 = m20 * other.m01 + m21 * other.m11 + m22 * other.m21 + m23 * other.m31;
        final float n22 = m20 * other.m02 + m21 * other.m12 + m22 * other.m22 + m23 * other.m32;
        final float n23 = m20 * other.m03 + m21 * other.m13 + m22 * other.m23 + m23 * other.m33;
        final float n30 = m30 * other.m00 + m31 * other.m10 + m32 * other.m20 + m33 * other.m30;
        final float n31 = m30 * other.m01 + m31 * other.m11 + m32 * other.m21 + m33 * other.m31;
        final float n32 = m30 * other.m02 + m31 * other.m12 + m32 * other.m22 + m33 * other.m32;
        final float n33 = m30 * other.m03 + m31 * other.m13 + m32 * other.m23 + m33 * other.m33;
        return new Matrix4F32(
                n00, n01, n02, n03,
                n10, n11, n12, n13,
                n20, n21, n22, n23,
                n30, n31, n32, n33
        );
    }

    @Override
    public Matrix4F32 preMul(Matrix4F32 matrix) {
        return matrix.mul(this);
    }

    @Override
    public Matrix4F32 transpose() {
        return new Matrix4F32(
                m00, m10, m20, m30,
                m01, m11, m21, m31,
                m02, m12, m22, m32,
                m03, m13, m23, m33
        );
    }

    @Override
    public Float determinant() {
        return m30 * m21 * m12 * m03 - m20 * m31 * m12 * m03
             - m30 * m11 * m22 * m03 + m10 * m31 * m22 * m03
             + m20 * m11 * m32 * m03 - m10 * m21 * m32 * m03
             - m30 * m21 * m02 * m13 + m20 * m31 * m02 * m13
             + m30 * m01 * m22 * m13 - m00 * m31 * m22 * m13
             - m20 * m01 * m32 * m13 + m00 * m21 * m32 * m13
             + m30 * m11 * m02 * m23 - m10 * m31 * m02 * m23
             - m30 * m01 * m12 * m23 + m00 * m31 * m12 * m23
             + m10 * m01 * m32 * m23 - m00 * m11 * m32 * m23
             - m20 * m11 * m02 * m33 + m10 * m21 * m02 * m33
             + m20 * m01 * m12 * m33 - m00 * m21 * m12 * m33
             - m10 * m01 * m22 * m33 + m00 * m11 * m22 * m33;
    }

    @Override
    public boolean isSingular() {
        return Math.abs(determinant()) < EPSILON;
    }

    @Override
    public Matrix4F32 invert() {

        final float det = determinant();
        if (Math.abs(det) < EPSILON) throw new ArithmeticException("The matrix cannot be inverted since singular.");

        final float invDet = 1f / det;

        // I honestly can't imagine how people figure out these things... (Hardly I can, still, how much time did it take?)
        final float n00 = (m12 * m23 * m31 - m13 * m22 * m31 + m13 * m21 * m32 - m11 * m23 * m32 - m12 * m21 * m33 + m11 * m22 * m33) * invDet;
        final float n01 = (m03 * m22 * m31 - m02 * m23 * m31 - m03 * m21 * m32 + m01 * m23 * m32 + m02 * m21 * m33 - m01 * m22 * m33) * invDet;
        final float n02 = (m02 * m13 * m31 - m03 * m12 * m31 + m03 * m11 * m32 - m01 * m13 * m32 - m02 * m11 * m33 + m01 * m12 * m33) * invDet;
        final float n03 = (m03 * m12 * m21 - m02 * m13 * m21 - m03 * m11 * m22 + m01 * m13 * m22 + m02 * m11 * m23 - m01 * m12 * m23) * invDet;
        final float n10 = (m13 * m22 * m30 - m12 * m23 * m30 - m13 * m20 * m32 + m10 * m23 * m32 + m12 * m20 * m33 - m10 * m22 * m33) * invDet;
        final float n11 = (m02 * m23 * m30 - m03 * m22 * m30 + m03 * m20 * m32 - m00 * m23 * m32 - m02 * m20 * m33 + m00 * m22 * m33) * invDet;
        final float n12 = (m03 * m12 * m30 - m02 * m13 * m30 - m03 * m10 * m32 + m00 * m13 * m32 + m02 * m10 * m33 - m00 * m12 * m33) * invDet;
        final float n13 = (m02 * m13 * m20 - m03 * m12 * m20 + m03 * m10 * m22 - m00 * m13 * m22 - m02 * m10 * m23 + m00 * m12 * m23) * invDet;
        final float n20 = (m11 * m23 * m30 - m13 * m21 * m30 + m13 * m20 * m31 - m10 * m23 * m31 - m11 * m20 * m33 + m10 * m21 * m33) * invDet;
        final float n21 = (m03 * m21 * m30 - m01 * m23 * m30 - m03 * m20 * m31 + m00 * m23 * m31 + m01 * m20 * m33 - m00 * m21 * m33) * invDet;
        final float n22 = (m01 * m13 * m30 - m03 * m11 * m30 + m03 * m10 * m31 - m00 * m13 * m31 - m01 * m10 * m33 + m00 * m11 * m33) * invDet;
        final float n23 = (m03 * m11 * m20 - m01 * m13 * m20 - m03 * m10 * m21 + m00 * m13 * m21 + m01 * m10 * m23 - m00 * m11 * m23) * invDet;
        final float n30 = (m12 * m21 * m30 - m11 * m22 * m30 - m12 * m20 * m31 + m10 * m22 * m31 + m11 * m20 * m32 - m10 * m21 * m32) * invDet;
        final float n31 = (m01 * m22 * m30 - m02 * m21 * m30 + m02 * m20 * m31 - m00 * m22 * m31 - m01 * m20 * m32 + m00 * m21 * m32) * invDet;
        final float n32 = (m02 * m11 * m30 - m01 * m12 * m30 - m02 * m10 * m31 + m00 * m12 * m31 + m01 * m10 * m32 - m00 * m11 * m32) * invDet;
        final float n33 = (m01 * m12 * m20 - m02 * m11 * m20 + m02 * m10 * m21 - m00 * m12 * m21 - m01 * m10 * m22 + m00 * m11 * m22) * invDet;
        return new Matrix4F32(
                n00, n01, n02, n03,
                n10, n11, n12, n13,
                n20, n21, n22, n23,
                n30, n31, n32, n33
        );
    }

    @Override
    public Matrix4F32 lerp(Matrix4F32 other, Float alpha) {
        final float invAlpha = 1f - alpha;
        final float n00 = m00 * invAlpha + other.m00 * alpha;
        final float n01 = m01 * invAlpha + other.m01 * alpha;
        final float n02 = m02 * invAlpha + other.m02 * alpha;
        final float n03 = m03 * invAlpha + other.m03 * alpha;
        final float n10 = m10 * invAlpha + other.m10 * alpha;
        final float n11 = m11 * invAlpha + other.m11 * alpha;
        final float n12 = m12 * invAlpha + other.m12 * alpha;
        final float n13 = m13 * invAlpha + other.m13 * alpha;
        final float n20 = m20 * invAlpha + other.m20 * alpha;
        final float n21 = m21 * invAlpha + other.m21 * alpha;
        final float n22 = m22 * invAlpha + other.m22 * alpha;
        final float n23 = m23 * invAlpha + other.m23 * alpha;
        final float n30 = m30 * invAlpha + other.m30 * alpha;
        final float n31 = m31 * invAlpha + other.m31 * alpha;
        final float n32 = m32 * invAlpha + other.m32 * alpha;
        final float n33 = m33 * invAlpha + other.m33 * alpha;
        return new Matrix4F32(
                n00, n01, n02, n03,
                n10, n11, n12, n13,
                n20, n21, n22, n23,
                n30, n31, n32, n33
        );
    }

    @Override
    public Matrix4F32 average(Matrix4F32 other, Float weight) {

        final float otherWeight = 1 - weight;
        final Vector3F32 scaling = scale().lerp(other.scale(), otherWeight);
        final Quaternion rotation = rotation().slerp(other.rotation(), otherWeight);
        final Vector3F32 translation = translation().lerp(other.translation(), otherWeight);

        return fromTRS(translation, rotation, scaling);
    }

    @Override
    public Matrix4F32 average(Matrix4F32[] matrices) {

        final float weight = 1f / matrices.length;

        var scale = matrices[0].scale().mul(weight);
        var rot = matrices[0].rotation().pow(weight);
        var tran = matrices[0].translation().mul(weight);

        for (int i = 1; i < matrices.length; i++) {

            final var matrix = matrices[i];

            scale = scale.add(matrix.scale().mul(weight));
            rot = rot.mul(matrix.rotation().pow(weight));
            tran = tran.add(matrix.translation().mul(weight));
        }
        return fromTRS(tran, rot, scale); // The rotations gets normalized internally.
    }

    @Override
    public Matrix4F32 average(Matrix4F32[] matrices, Float[] weights) {

        if (matrices.length != weights.length) throw new IllegalArgumentException("The matrices and weights must have the same length.");

        var scale = matrices[0].scale().mul(weights[0]);
        var rot = matrices[0].rotation().pow(weights[0]);
        var tran = matrices[0].translation().mul(weights[0]);

        for (int i = 1; i < matrices.length; i++) {

            final var matrix = matrices[i];

            scale = scale.add(matrix.scale().mul(weights[i]));
            rot = rot.mul(matrix.rotation().pow(weights[i]));
            tran = tran.add(matrix.translation().mul(weights[i]));
        }
        return fromTRS(tran, rot, scale); // The rotations gets normalized internally.
    }

    @Override
    public Vector3F32 translation() {
        return new Vector3F32(m03, m13, m23);
    }

    @Override
    public Quaternion rotation() {
        return Quaternion.fromMatrix4(asF64());
    }

    @Override
    public Vector3F32 scale() {
        final float x = (float) Math.sqrt(m00 * m00 + m01 * m01 + m02 * m02);
        final float y = (float) Math.sqrt(m10 * m10 + m11 * m11 + m12 * m12);
        final float z = (float) Math.sqrt(m20 * m20 + m21 * m21 + m22 * m22);
        return new Vector3F32(x, y, z);
    }

    @Override
    public Matrix4F32 toNormalMatrix() {
        return new Matrix4F32(
                m00, m01, m02, 0f,
                m10, m11, m12, 0f,
                m20, m21, m22, 0f,
                m30, m31, m32, m33
        ).invert().transpose();
    }

    @Override
    public void toMemorySegment(MemorySegment segment, long index) {

        final var layout = ValueLayout.JAVA_FLOAT;
        final long baseIndex = index * SIZE;
        segment.setAtIndex(layout, baseIndex + M00, m00);
        segment.setAtIndex(layout, baseIndex + M01, m01);
        segment.setAtIndex(layout, baseIndex + M02, m02);
        segment.setAtIndex(layout, baseIndex + M03, m03);

        segment.setAtIndex(layout, baseIndex + M10, m10);
        segment.setAtIndex(layout, baseIndex + M11, m11);
        segment.setAtIndex(layout, baseIndex + M12, m12);
        segment.setAtIndex(layout, baseIndex + M13, m13);

        segment.setAtIndex(layout, baseIndex + M20, m20);
        segment.setAtIndex(layout, baseIndex + M21, m21);
        segment.setAtIndex(layout, baseIndex + M22, m22);
        segment.setAtIndex(layout, baseIndex + M23, m23);

        segment.setAtIndex(layout, baseIndex + M30, m30);
        segment.setAtIndex(layout, baseIndex + M31, m31);
        segment.setAtIndex(layout, baseIndex + M32, m32);
        segment.setAtIndex(layout, baseIndex + M33, m33);
    }

    @Override
    public MemorySegment asMemorySegment(Arena arena) {
        final var segment = arena.allocate(byteSize());
        toMemorySegment(segment, 0);
        return segment;
    }

    @Override
    public Matrix3F32 asMatrix3() {
        return Matrix3F32.fromMatrix4(this);
    }

    @Override
    public Vector3F32 transform(Vector3F32 vector) {
        return asMatrix3()
                .transform(vector)
                .add(new Vector3F32(m03, m13, m23));
    }

    @Override
    public Matrix4F32 translate(Vector3F32 translation) {
        return mul(fromTranslation(translation));
    }

    @Override
    public Matrix4F32 rotateAround(Vector3F32 axis, Radians angle) {
        return mul(fromAxisAngle(axis, angle));
    }

    @Override
    public Matrix4F32 rotate(Quaternion rotation) {
        return mul(fromRotation(rotation));
    }

    @Override
    public Matrix4F32 rotateBetween(Vector3F32 v1, Vector3F32 v2) {
        return mul(fromRotationBetween(v1, v2));
    }

    @Override
    public Matrix4F32 rotateToDirection(Vector3F32 direction, Vector3F32 up) {
        return mul(fromLookRotation(direction, up));
    }

    @Override
    public Matrix4F32 scale(Vector3F32 scale) {
        return mul(fromScale(scale));
    }

    @Override
    public Vector3F32 project(Vector3F32 vector) {
        final float invW = 1f / (vector.x() * m30() + vector.y() * m31() + vector.z() * m32() + m33());
        return transform(vector).mul(invW);
    }

    @Override
    public Vector3F32 rotate(Vector3F32 vector) {
        return asMatrix3().transform(vector);
    }

    @Override
    public Vector3F32 unrotate(Vector3F32 vector) {
        return asMatrix3().unrotate(vector);
    }

    @Override
    public Vector3F32 untransform(Vector3F32 vector) {
        return asMatrix3().unrotate(vector.sub(new Vector3F32(m03, m13, m23)));
    }

    @Override
    public void toMatrix4x3Array(Float[] out) {
        if (out.length < 12) throw new IllegalArgumentException("The matrix array provided is not a 4x3 matrix.");
        out[0] = m00; out[3] = m01; out[6] = m02; out[9] = m03;
        out[1] = m10; out[4] = m11; out[7] = m12; out[10] = m13;
        out[2] = m20; out[5] = m21; out[8] = m22; out[11] = m23;
    }

    public Matrix4F64 asF64() {
        return new Matrix4F64(
                m00, m01, m02, m03,
                m10, m11, m12, m13,
                m20, m21, m22, m23,
                m30, m31, m32, m33
        );
    }
}
