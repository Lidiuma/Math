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
import org.lidiuma.math.rotation.Quaternion;
import org.lidiuma.math.rotation.Radians;
import org.lidiuma.math.vector.v2.Vector2F32;
import org.lidiuma.math.vector.v3.Vector3F32;
import jdk.internal.vm.annotation.LooselyConsistentValue;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import static org.lidiuma.math.FloatingUtil.EPSILON;

/// @see Matrix3
@LooselyConsistentValue
@SuppressWarnings("unused")
public value record Matrix3F32(
        @NullRestricted Float m00, @NullRestricted Float m01, @NullRestricted Float m02,
        @NullRestricted Float m10, @NullRestricted Float m11, @NullRestricted Float m12,
        @NullRestricted Float m20, @NullRestricted Float m21, @NullRestricted Float m22
) implements Matrix3<Matrix3F32, Float, Vector2F32, Vector3F32> {

    public static Matrix3F32 identity() {
        return new Matrix3F32(
                1f, 0f, 0f,
                0f, 1f, 0f,
                0f, 0f, 1f
        );
    }

    /// Creates a new matrix from the given {@link MemorySegment} starting at the specified logical index.\
    /// The memory segment must be able to hold *at least* `(index + 1) * `{@link #byteSize()}.
    /// @param segment the memory segment to copy from.
    /// @param index the logical index in units of {@link #byteSize()} where copying begins.
    /// @apiNote The memory segment must be stored in [column-major](https://en.wikipedia.org/wiki/Row-_and_column-major_order) order.
    public static Matrix3F32 fromMemorySegment(MemorySegment segment, long index) {

        final var layout = ValueLayout.JAVA_FLOAT;
        final long baseIndex = index * SIZE;

        final float m00 = segment.getAtIndex(layout, baseIndex + M00);
        final float m10 = segment.getAtIndex(layout, baseIndex + M10);
        final float m20 = segment.getAtIndex(layout, baseIndex + M20);

        final float m01 = segment.getAtIndex(layout, baseIndex + M01);
        final float m11 = segment.getAtIndex(layout, baseIndex + M11);
        final float m21 = segment.getAtIndex(layout, baseIndex + M21);

        final float m02 = segment.getAtIndex(layout, baseIndex + M02);
        final float m12 = segment.getAtIndex(layout, baseIndex + M12);
        final float m22 = segment.getAtIndex(layout, baseIndex + M22);
        return new Matrix3F32(
                m00, m01, m02,
                m10, m11, m12,
                m20, m21, m22
        );
    }

    /// @return creates an identity matrix having the 3rd column set to the translation vector.
    public static Matrix3F32 fromTranslation(Vector2F32 translation) {
        final float x = translation.x();
        final float y = translation.y();
        return new Matrix3F32(
                1f, 0f, x,
                0f, 1f, y,
                0f, 0f, 1f
        );
    }

    /// @return a pure rotation matrix from the provided angle.
    public static Matrix3F32 fromAffineRotation(Radians angle) {
        final float cos = (float) Math.cos(angle.value());
        final float sin = (float) Math.sin(angle.value());
        return new Matrix3F32(
                cos, -sin, 0f,
                sin, cos, 0f,
                0f, 0f, 1f
        );
    }

    /// @return a pure rotation matrix from the provided quaternion.
    public static Matrix3F32 fromRotation(Quaternion rotation) {

        final var rot = rotation.normalize();

        final double xs = rot.x() * 2f, ys = rot.y() * 2f, zs = rot.z() * 2f;
        final double wx = rot.w() * xs, wy = rot.w() * ys, wz = rot.w() * zs;
        final double xx = rot.x() * xs, xy = rot.x() * ys, xz = rot.x() * zs;
        final double yy = rot.y() * ys, yz = rot.y() * zs, zz = rot.z() * zs;

        final double m00 = 1d - (yy + zz), m01 = xy - wz       , m02 = xz + wy;
        final double m10 = xy + wz       , m11 = 1d - (xx + zz), m12 = yz - wx;
        final double m20 = xz - wy       , m21 = yz + wx       , m22 = 1d - (xx + yy);
        return new Matrix3F32(
                (float) m00, (float) m01, (float) m02,
                (float) m10, (float) m11, (float) m12,
                (float) m20, (float) m21, (float) m22
        );
    }

    /// @return a new pure scaling matrix.
    public static Matrix3F32 fromScale(Vector2F32 scale) {
        final float x = scale.x();
        final float y = scale.y();
        return new Matrix3F32(
                x, 0f, 0f,
                0f, y, 0f,
                0f, 0f, 1f
        );
    }

    /// @return a new transformation matrix from scale and translation.
    public static Matrix3F32 fromST(Vector2F32 translation, Vector2F32 scale) {
        return new Matrix3F32(
                scale.x(), 0f, translation.x(),
                0f, scale.y(), translation.y(),
                0f, 0f, 1f
        );
    }

    /// Creates a transformation matrix from affine translation, affine  rotation, and affine scale.
    /// @return The transformation matrix.
    /// @apiNote The rotation quaternion is normalized internally.
    public static Matrix3F32 fromAffineTRS(Vector2F32 translation, Radians rotation, Vector2F32 scale) {
        final var rot = fromAffineRotation(rotation);
        final var scl = fromScale(scale);
        final var rotScl = rot.affineMul(scl);
        return fromTranslation(translation).affineMul(rotScl);
    }

    /// Creates a transformation matrix from translation, rotation, and scale.
    /// @return The transformation matrix.
    /// @apiNote The rotation quaternion is normalized internally.
    public static Matrix3F32 fromTRS(Vector2F32 translation, Quaternion rotation, Vector2F32 scale) {
        final var rot = fromRotation(rotation);
        final var scl = fromScale(scale);
        final var rotScl = rot.mul(scl);
        return fromTranslation(translation).mul(rotScl);
    }

    /// Creates a new matrix using the top-left 3x3 of the matrix4.
    public static Matrix3F32 fromMatrix4(Matrix4F32 matrix) {
        return new Matrix3F32(
                matrix.m00(), matrix.m01(), matrix.m02(),
                matrix.m10(), matrix.m11(), matrix.m12(),
                matrix.m20(), matrix.m21(), matrix.m22()
        );
    }

    /// @return a new pure shearing matrix.
    public static Matrix3F32 fromShear(Vector2F32 shear) {
        final float x = shear.x(), y = shear.y();
        return new Matrix3F32(
                1f, x,  0f,
                y,  1f, 0f,
                0f, 0f, 1f
        );
    }

    @Override
    public long byteSize() {
        return (long) SIZE * Float.SIZE;
    }

    @Override
    public Matrix3F32 add(Matrix3F32 other) {
        return new Matrix3F32(
                m00 + other.m00(), m01 + other.m01(), m02 + other.m02(),
                m10 + other.m10(), m11 + other.m11(), m12 + other.m12(),
                m20 + other.m20(), m21 + other.m21(), m22 + other.m22()
        );
    }

    @Override
    public Matrix3F32 sub(Matrix3F32 other) {
        return new Matrix3F32(
                m00 - other.m00(), m01 - other.m01(), m02 - other.m02(),
                m10 - other.m10(), m11 - other.m11(), m12 - other.m12(),
                m20 - other.m20(), m21 - other.m21(), m22 - other.m22()
        );
    }

    @Override
    public Matrix3F32 mul(Float scalar) {
        return new Matrix3F32(
                m00 * scalar, m01 * scalar, m02 * scalar,
                m10 * scalar, m11 * scalar, m12 * scalar,
                m20 * scalar, m21 * scalar, m22 * scalar
        );
    }

    @Override
    public Matrix3F32 mul(Matrix3F32 other) {
		final float n00 = m00 * other.m00 + m01 * other.m10 + m02 * other.m20;
		final float n01 = m00 * other.m01 + m01 * other.m11 + m02 * other.m21;
		final float n02 = m00 * other.m02 + m01 * other.m12 + m02 * other.m22;
		final float n10 = m10 * other.m00 + m11 * other.m10 + m12 * other.m20;
		final float n11 = m10 * other.m01 + m11 * other.m11 + m12 * other.m21;
		final float n12 = m10 * other.m02 + m11 * other.m12 + m12 * other.m22;
		final float n20 = m20 * other.m00 + m21 * other.m10 + m22 * other.m20;
		final float n21 = m20 * other.m01 + m21 * other.m11 + m22 * other.m21;
		final float n22 = m20 * other.m02 + m21 * other.m12 + m22 * other.m22;
        return new Matrix3F32(
                n00, n01, n02,
                n10, n11, n12,
                n20, n21, n22
        );
	}

    @Override
    public Matrix3F32 preMul(Matrix3F32 other) {
        return other.mul(this);
    }

    @Override
    public Vector3F32 transform(Vector3F32 vector) {
        final float x = vector.x() * m00 + vector.y() * m01 + vector.z() * m02;
        final float y = vector.x() * m10 + vector.y() * m11 + vector.z() * m12;
        final float z = vector.x() * m20 + vector.y() * m21 + vector.z() * m22;
        return new Vector3F32(x, y, z);
    }

    @Override
    public Vector2F32 transform(Vector2F32 vector) {
        final float x = vector.x(), y = vector.y();
        return new Vector2F32(
                m00 * x + m01 * y + m02,
                m10 * x + m11 * y + m12
        );
    }

    @Override
	public Float determinant() {
		return m00 * m11 * m22 + m01 * m12 * m20 + m02 * m10 * m21
			  -m00 * m12 * m21 - m01 * m10 * m22 - m02 * m11 * m20;
	}

    @Override
    public boolean isSingular() {
        return Math.abs(determinant()) < EPSILON;
    }

    @Override
    public Matrix3F32 toNormalMatrix() {
        return invert().transpose();
    }

    @Override
    public Matrix3F32 invert() {

		final float det = determinant();
        if (Math.abs(det) < EPSILON) throw new ArithmeticException("The matrix cannot be inverted since singular.");

		final float inv_det = 1f / det;

		final float n00 = (m11 * m22 - m21 * m12) * inv_det;
        final float n01 = (m21 * m02 - m01 * m22) * inv_det;
        final float n02 = (m01 * m12 - m11 * m02) * inv_det;
        final float n10 = (m20 * m12 - m10 * m22) * inv_det;
        final float n11 = (m00 * m22 - m20 * m02) * inv_det;
        final float n12 = (m10 * m02 - m00 * m12) * inv_det;
        final float n20 = (m10 * m21 - m20 * m11) * inv_det;
        final float n21 = (m20 * m01 - m00 * m21) * inv_det;
		final float n22 = (m00 * m11 - m10 * m01) * inv_det;
        return new Matrix3F32(
                n00, n01, n02,
                n10, n11, n12,
                n20, n21, n22
        );
	}

    @Override
    public Matrix3F32 transpose() {
        return new Matrix3F32(
                m00, m10, m20,
                m01, m11, m21,
                m02, m12, m22
        );
    }

    @Override
    public Matrix3F32 rotate(Quaternion quaternion) {
        return mul(fromRotation(quaternion));
    }

    @Override
    public Vector3F32 unrotate(Vector3F32 vector) {
        return transpose().transform(vector);
    }

    @Override
    public Matrix3F32 affineMul(Matrix3F32 other) {
        final float n00 = m00 * other.m00 + m01 * other.m10;
        final float n01 = m00 * other.m01 + m01 * other.m11;
        final float n02 = m00 * other.m02 + m01 * other.m12 + m02;
        final float n10 = m10 * other.m00 + m11 * other.m10;
        final float n11 = m10 * other.m01 + m11 * other.m11;
        final float n12 = m10 * other.m02 + m11 * other.m12 + m12;
        return new Matrix3F32(
                n00, n01, n02,
                n10, n11, n12,
                0f, 0f, 1f
        );
    }

    @Override
    public Float affineDeterminant() {
        return m00 * m11 - m01 * m10;
    }

    @Override
    public Matrix3F32 affineInvert() {

        final float det = affineDeterminant();
        if (Math.abs(det) < EPSILON) throw new ArithmeticException("The matrix cannot be inverted since singular.");

        final float invDet = 1f / det;

        final float n00 = invDet * m11;
        final float n01 = invDet * -m01;
        final float n02 = invDet * (m01 * m12 - m11 * m02);
        final float n10 = invDet * -m10;
        final float n11 = invDet * m00;
        final float n12 = invDet * (m10 * m02 - m00 * m12);
        return new Matrix3F32(
                n00, n01, n02,
                n10, n11, n12,
                0f, 0f, 1f
        );
    }

    @Override
    public boolean isAffineSingular() {
        return Math.abs(affineDeterminant()) < EPSILON;
    }

    @Override
    public Matrix3F32 affineTranslate(Vector2F32 translation) {
        return affineMul(fromTranslation(translation));
	}

    @Override
    public Matrix3F32 affineRotate(Radians angle) {
        return affineMul(fromAffineRotation(angle));
	}

    @Override
    public Matrix3F32 affineShear(Vector2F32 shear) {
        return affineMul(fromShear(shear));
    }

    @Override
    public Matrix3F32 affineScale(Vector2F32 scale) {
        return affineMul(fromScale(scale));
	}

    @Override
    public Vector2F32 affineScale() {
        final float x = (float) Math.sqrt(m00 * m00 + m01 * m01);
        final float y = (float) Math.sqrt(m10 * m10 + m11 * m11);
        return new Vector2F32(x, y);
    }

    @Override
	public Vector2F32 translation() {
        return new Vector2F32(m02, m12);
	}

    @Override
	public Radians rotation() {
		return Radians.radians(Math.atan2(m10, m00));
	}

    @Override
    public void toMemorySegment(MemorySegment segment, long index) {

        final var layout = ValueLayout.JAVA_FLOAT;
        final long baseIndex = index * SIZE;
        segment.setAtIndex(layout, baseIndex + M00, m00);
        segment.setAtIndex(layout, baseIndex + M01, m01);
        segment.setAtIndex(layout, baseIndex + M02, m02);

        segment.setAtIndex(layout, baseIndex + M10, m10);
        segment.setAtIndex(layout, baseIndex + M11, m11);
        segment.setAtIndex(layout, baseIndex + M12, m12);

        segment.setAtIndex(layout, baseIndex + M20, m20);
        segment.setAtIndex(layout, baseIndex + M21, m21);
        segment.setAtIndex(layout, baseIndex + M22, m22);
    }

    @Override
    public MemorySegment asMemorySegment(Arena arena) {
        final var segment = arena.allocate(byteSize());
        toMemorySegment(segment, 0);
        return segment;
    }

    public Matrix3F64 asF64() {
        return new Matrix3F64(
                (double) m00, (double) m01, (double) m02,
                (double) m10, (double) m11, (double) m12,
                (double) m20, (double) m21, (double) m22
        );
    }
}
