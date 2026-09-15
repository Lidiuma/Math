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

import org.lidiuma.math.api.matrix.Affine3;
import org.lidiuma.math.api.traits.matrix.Affine3Ops;
import org.lidiuma.math.api.traits.matrix.FloatingAffineOps;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.processor.NamedAlias;
import org.lidiuma.math.rotation.QuaternionF32;
import org.lidiuma.math.vector.Vec3F32;
import static java.lang.Math.fma;
import static org.lidiuma.math.internal.AnnotationConst.*;

@FactoryAlias(methodName = AFFINE3_FACTORY, outputClass = MATRIX_OUT)
public value record Affine3F32(
        // I'm not using an array because it's an identity object, and this reads and feels better to work with.
        @Override Float m00, @Override Float m01, @Override Float m02, @Override Float m03,
        @Override Float m10, @Override Float m11, @Override Float m12, @Override Float m13,
        @Override Float m20, @Override Float m21, @Override Float m22, @Override Float m23
) implements Affine3<Float> {

    @FieldAlias(outputClass = MATRIX_OUT)
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized affine-matrix from a generic affine-matrix.
    @AliasExclude // This method can be a performance sink if used inappropriately, so I exclude it from the alias.
    public Affine3F32(Affine3<Float> affine3) {
        this(
                affine3.m00(), affine3.m01(), affine3.m02(), affine3.m03(),
                affine3.m10(), affine3.m11(), affine3.m12(), affine3.m13(),
                affine3.m20(), affine3.m21(), affine3.m22(), affine3.m23()
        );
    }

    @Override
    public Float m30() {
        return 0f;
    }

    @Override
    public Float m31() {
        return 0f;
    }

    @Override
    public Float m32() {
        return 0f;
    }

    @Override
    public Float m33() {
        return 1f;
    }

    public static final value class Ops implements Affine3Ops<Affine3F32, Vec3F32, Float>, FloatingAffineOps<Affine3F32, Vec3F32, QuaternionF32, Float> {

        private Ops() {}

        /// Creates a transformation matrix from translation, rotation, and scale.
        public Affine3F32 fromTRS(Affine3F32 translation, Affine3F32 rotation, Affine3F32 scale) {
            return multiply(translation, multiply(rotation, scale));
        }

        /// Creates a transformation matrix from translation, rotation, and scale.
        public Affine3F32 fromTRS(Vec3F32 translation, QuaternionF32 rotation, Vec3F32 scale) {
            final float xs = rotation.x() * 2f, ys = rotation.y() * 2f, zs = rotation.z() * 2f;
            final float wx = rotation.w() * xs, wy = rotation.w() * ys, wz = rotation.w() * zs;
            final float xx = rotation.x() * xs, xy = rotation.x() * ys, xz = rotation.x() * zs;
            final float yy = rotation.y() * ys, yz = rotation.y() * zs, zz = rotation.z() * zs;
            return new Affine3F32(
                    (1f - (yy + zz)) * scale.x(), (xy - wz) * scale.y()       , (xz + wy) * scale.z(), translation.x(),
                    (xy + wz) * scale.x()       , (1f - (xx + zz)) * scale.y(), (yz - wx) * scale.z(), translation.y(),
                    (xz - wy) * scale.x()       , (yz + wx) * scale.y()       , (1f - (xx + yy)) * scale.z(), translation.z()
            );
        }

        @Override
        public Affine3F32 fromRotation(QuaternionF32 quaternion) {
            final float xs = quaternion.x() * 2f, ys = quaternion.y() * 2f, zs = quaternion.z() * 2f;
            final float wx = quaternion.w() * xs, wy = quaternion.w() * ys, wz = quaternion.w() * zs;
            final float xx = quaternion.x() * xs, xy = quaternion.x() * ys, xz = quaternion.x() * zs;
            final float yy = quaternion.y() * ys, yz = quaternion.y() * zs, zz = quaternion.z() * zs;
            return new Affine3F32(
                    1f - (yy + zz), xy - wz       , xz + wy       , 0f,
                    xy + wz       , 1f - (xx + zz), yz - wx       , 0f,
                    xz - wy       , yz + wx       , 1f - (xx + yy), 0f
            );
        }

        @Override
        @AliasExclude
        public Affine3F32 of(Float m00, Float m01, Float m02, Float m03,
                             Float m10, Float m11, Float m12, Float m13,
                             Float m20, Float m21, Float m22, Float m23) {
            return new Affine3F32(
                    m00, m01, m02, m03,
                    m10, m11, m12, m13,
                    m20, m21, m22, m23
            );
        }

        @Override
        @NamedAlias(methodName = ZERO_FACTORY + UPPER_AFFINE3_FACTORY + F32)
        public Affine3F32 zero() {
            return of(
                    0f, 0f, 0f, 0f,
                    0f, 0f, 0f, 0f,
                    0f, 0f, 0f, 0f
            );
        }

        @Override
        @NamedAlias(methodName = ONE_FACTORY + UPPER_AFFINE3_FACTORY + F32)
        public Affine3F32 one() {
            return of(
                    1f, 1f, 1f, 1f,
                    1f, 1f, 1f, 1f,
                    1f, 1f, 1f, 1f
            );
        }

        @Override
        @NamedAlias(methodName = IDENTITY_FACTORY + UPPER_AFFINE3_FACTORY + F32)
        public Affine3F32 identity() {
            return of(
                    1f, 0f, 0f, 0f,
                    0f, 1f, 0f, 0f,
                    0f, 0f, 1f, 0f
            );
        }

        @Override
        @AliasExclude
        public Vec3F32.Ops vectorOps() {
            return Vec3F32.OPS;
        }

        @Override
        public Affine3F32 fromAxes(Vec3F32 xAxis, Vec3F32 yAxis, Vec3F32 zAxis, Vec3F32 translation) {
            return of(
                    xAxis.x(), yAxis.x(), zAxis.x(), translation.x(),
                    xAxis.y(), yAxis.y(), zAxis.y(), translation.y(),
                    xAxis.z(), yAxis.z(), zAxis.z(), translation.z()
            );
        }

        @Override
        public Affine3F32 fromTranslation(Vec3F32 translation) {
            return of(
                    1f, 0f, 0f, translation.x(),
                    0f, 1f, 0f, translation.y(),
                    0f, 0f, 1f, translation.z());
        }

        @Override
        public Affine3F32 fromScale(Vec3F32 scale) {
            return of(
                    scale.x(), 0f, 0f, 0f,
                    0f, scale.y(), 0f, 0f,
                    0f, 0f, scale.z(), 0f
            );
        }

        @Override
        public Affine3F32 transpose(Affine3F32 affine) {
            return of(
                    affine.m00(), affine.m10(), affine.m20(), affine.m03(),
                    affine.m01(), affine.m11(), affine.m21(), affine.m13(),
                    affine.m02(), affine.m12(), affine.m22(), affine.m23()
            );
        }

        @Override
        public Float determinant(Affine3F32 matrix) {
            return  fma(matrix.m00(), fma(matrix.m11(), matrix.m22(), -matrix.m12() * matrix.m21()),
                   fma(-matrix.m01(), fma(matrix.m10(), matrix.m22(), -matrix.m12() * matrix.m20()),
                       (matrix.m02()* fma(matrix.m10(), matrix.m21(), -matrix.m11() * matrix.m20()))));
        }

        @Override
        public Affine3F32 inverse(Affine3F32 matrix) throws ArithmeticException {

            final float det = determinant(matrix);
            if (det == 0f) throw new ArithmeticException("The matrix cannot be inverted since singular.");

            final float inv = 1f / det;
            final float m00 = fma(matrix.m11(), matrix.m22(), -matrix.m12() * matrix.m21()) * inv;
            final float m01 = fma(matrix.m02(), matrix.m21(), -matrix.m01() * matrix.m22()) * inv;
            final float m02 = fma(matrix.m01(), matrix.m12(), -matrix.m02() * matrix.m11()) * inv;

            final float m10 = fma(matrix.m12(), matrix.m20(), -matrix.m10() * matrix.m22()) * inv;
            final float m11 = fma(matrix.m00(), matrix.m22(), -matrix.m02() * matrix.m20()) * inv;
            final float m12 = fma(matrix.m02(), matrix.m10(), -matrix.m00() * matrix.m12()) * inv;

            final float m20 = fma(matrix.m10(), matrix.m21(), -matrix.m11() * matrix.m20()) * inv;
            final float m21 = fma(matrix.m01(), matrix.m20(), -matrix.m00() * matrix.m21()) * inv;
            final float m22 = fma(matrix.m00(), matrix.m11(), -matrix.m01() * matrix.m10()) * inv;

            return of(
                    m00, m01, m02, -fma(m00, matrix.m03(), fma(m01, matrix.m13(), m02 * matrix.m23())),
                    m10, m11, m12, -fma(m10, matrix.m03(), fma(m11, matrix.m13(), m12 * matrix.m23())),
                    m20, m21, m22, -fma(m20, matrix.m03(), fma(m21, matrix.m13(), m22 * matrix.m23()))
            );
        }

        @Override
        public Vec3F32 multiply(Affine3F32 matrix, Vec3F32 vector) {
            return new Vec3F32(
                    fma(matrix.m00(), vector.x(), fma(matrix.m01(), vector.y(), fma(matrix.m02(), vector.z(), matrix.m03()))),
                    fma(matrix.m10(), vector.x(), fma(matrix.m11(), vector.y(), fma(matrix.m12(), vector.z(), matrix.m13()))),
                    fma(matrix.m20(), vector.x(), fma(matrix.m21(), vector.y(), fma(matrix.m22(), vector.z(), matrix.m23())))
            );
        }

        @Override
        public Affine3F32 multiply(Affine3F32 matrix, Float scalar) {
            return of(
                    matrix.m00() * scalar,
                    matrix.m01() * scalar,
                    matrix.m02() * scalar,
                    matrix.m03() * scalar,
                    matrix.m10() * scalar,
                    matrix.m11() * scalar,
                    matrix.m12() * scalar,
                    matrix.m13() * scalar,
                    matrix.m20() * scalar,
                    matrix.m21() * scalar,
                    matrix.m22() * scalar,
                    matrix.m23() * scalar
            );
        }

        @Override
        public Affine3F32 add(Affine3F32 op1, Affine3F32 op2) {
            return of(
                    op1.m00() + op2.m00(),
                    op1.m01() + op2.m01(),
                    op1.m02() + op2.m02(),
                    op1.m03() + op2.m03(),
                    op1.m10() + op2.m10(),
                    op1.m11() + op2.m11(),
                    op1.m12() + op2.m12(),
                    op1.m13() + op2.m13(),
                    op1.m20() + op2.m20(),
                    op1.m21() + op2.m21(),
                    op1.m22() + op2.m22(),
                    op1.m23() + op2.m23()
            );
        }

        @Override
        public Affine3F32 subtract(Affine3F32 op1, Affine3F32 op2) {
            return of(
                    op1.m00() - op2.m00(),
                    op1.m01() - op2.m01(),
                    op1.m02() - op2.m02(),
                    op1.m03() - op2.m03(),
                    op1.m10() - op2.m10(),
                    op1.m11() - op2.m11(),
                    op1.m12() - op2.m12(),
                    op1.m13() - op2.m13(),
                    op1.m20() - op2.m20(),
                    op1.m21() - op2.m21(),
                    op1.m22() - op2.m22(),
                    op1.m23() - op2.m23()
            );
        }

        @Override
        public Affine3F32 multiply(Affine3F32 op1, Affine3F32 op2) {
            return of(
                    fma(op1.m00(), op2.m00(), fma(op1.m01(), op2.m10(), op1.m02() * op2.m20())),
                    fma(op1.m00(), op2.m01(), fma(op1.m01(), op2.m11(), op1.m02() * op2.m21())),
                    fma(op1.m00(), op2.m02(), fma(op1.m01(), op2.m12(), op1.m02() * op2.m22())),
                    fma(op1.m00(), op2.m03(), fma(op1.m01(), op2.m13(), op1.m02() * op2.m23())) + op1.m03(),

                    fma(op1.m10(), op2.m00(), fma(op1.m11(), op2.m10(), op1.m12() * op2.m20())),
                    fma(op1.m10(), op2.m01(), fma(op1.m11(), op2.m11(), op1.m12() * op2.m21())),
                    fma(op1.m10(), op2.m02(), fma(op1.m11(), op2.m12(), op1.m12() * op2.m22())),
                    fma(op1.m10(), op2.m03(), fma(op1.m11(), op2.m13(), op1.m12() * op2.m23())) + op1.m13(),

                    fma(op1.m20(), op2.m00(), fma(op1.m21(), op2.m10(), op1.m22() * op2.m20())),
                    fma(op1.m20(), op2.m01(), fma(op1.m21(), op2.m11(), op1.m22() * op2.m21())),
                    fma(op1.m20(), op2.m02(), fma(op1.m21(), op2.m12(), op1.m22() * op2.m22())),
                    fma(op1.m20(), op2.m03(), fma(op1.m21(), op2.m13(), op1.m22() * op2.m23())) + op1.m23()
            );
        }

        @Override
        public Affine3F32 remainder(Affine3F32 op1, Affine3F32 op2) {
            return of(
                    op1.m00() % op2.m00(),
                    op1.m01() % op2.m01(),
                    op1.m02() % op2.m02(),
                    op1.m03() % op2.m03(),
                    op1.m10() % op2.m10(),
                    op1.m11() % op2.m11(),
                    op1.m12() % op2.m12(),
                    op1.m13() % op2.m13(),
                    op1.m20() % op2.m20(),
                    op1.m21() % op2.m21(),
                    op1.m22() % op2.m22(),
                    op1.m23() % op2.m23()
            );
        }

        @Override
        public Affine3F32 negated(Affine3F32 operand) {
            return of(
                    -operand.m00(), -operand.m01(), -operand.m02(), -operand.m03(),
                    -operand.m10(), -operand.m11(), -operand.m12(), -operand.m13(),
                    -operand.m20(), -operand.m21(), -operand.m22(), -operand.m23()
            );
        }

        @Override
        public Affine3F32 normalMatrix(Affine3F32 matrix) throws ArithmeticException {
            final var transposed = transpose(inverse(matrix));
            return of(
                    transposed.m00(), transposed.m01(), transposed.m02(), 0f,
                    transposed.m10(), transposed.m11(), transposed.m12(), 0f,
                    transposed.m20(), transposed.m21(), transposed.m22(), 0f
            );
        }

        @Override
        public boolean isSingular(Affine3F32 matrix) {
            return determinant(matrix) == 0f;
        }

        @Override
        public Affine3F32 divide(Affine3F32 op1, Affine3F32 op2) throws ArithmeticException {
            return multiply(op1, inverse(op2));
        }
    }
}
