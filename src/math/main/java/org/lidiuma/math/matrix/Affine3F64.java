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
import org.lidiuma.math.rotation.QuaternionF64;
import org.lidiuma.math.vector.Vec3F64;
import static java.lang.Math.fma;
import static org.lidiuma.math.internal.AnnotationConst.*;

@FactoryAlias(methodName = AFFINE3_FACTORY, outputClass = MATRIX_OUT)
public value record Affine3F64(
        // I'm not using an array because it's an identity object, and this reads and feels better to work with.
        @Override Double m00, @Override Double m01, @Override Double m02, @Override Double m03,
        @Override Double m10, @Override Double m11, @Override Double m12, @Override Double m13,
        @Override Double m20, @Override Double m21, @Override Double m22, @Override Double m23
) implements Affine3<Double> {

    @FieldAlias(outputClass = MATRIX_OUT)
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized affine-matrix from a generic affine-matrix.
    @AliasExclude // This method can be a performance sink if used inappropriately, so I exclude it from the alias.
    public Affine3F64(Affine3<Double> affine3) {
        this(
                affine3.m00(), affine3.m01(), affine3.m02(), affine3.m03(),
                affine3.m10(), affine3.m11(), affine3.m12(), affine3.m13(),
                affine3.m20(), affine3.m21(), affine3.m22(), affine3.m23()
        );
    }

    @Override
    public Double m30() {
        return 0d;
    }

    @Override
    public Double m31() {
        return 0d;
    }

    @Override
    public Double m32() {
        return 0d;
    }

    @Override
    public Double m33() {
        return 1d;
    }

    public static final value class Ops implements Affine3Ops<Affine3F64, Vec3F64, Double>, FloatingAffineOps<Affine3F64, Vec3F64, QuaternionF64, Double> {

        private Ops() {}

        /// Creates a transformation matrix from translation, rotation, and scale.
        public Affine3F64 fromTRS(Vec3F64 translation, Affine3F64 rotation, Vec3F64 scale) {
            return new Affine3F64(
                    rotation.m00() * scale.x(), rotation.m01() * scale.y(), rotation.m02() * scale.z(), translation.x(),
                    rotation.m10() * scale.x(), rotation.m11() * scale.y(), rotation.m12() * scale.z(), translation.y(),
                    rotation.m20() * scale.x(), rotation.m21() * scale.y(), rotation.m22() * scale.z(), translation.z()
            );
        }

        /// Creates a transformation matrix from translation, rotation, and scale.
        public Affine3F64 fromTRS(Vec3F64 translation, QuaternionF64 rotation, Vec3F64 scale) {
            return fromTRS(translation, fromRotation(rotation), scale);
        }

        @Override
        public Affine3F64 fromRotation(QuaternionF64 quaternion) {
            final double xs = quaternion.x() * 2d, ys = quaternion.y() * 2d, zs = quaternion.z() * 2d;
            final double wx = quaternion.w() * xs, wy = quaternion.w() * ys, wz = quaternion.w() * zs;
            final double xx = quaternion.x() * xs, xy = quaternion.x() * ys, xz = quaternion.x() * zs;
            final double yy = quaternion.y() * ys, yz = quaternion.y() * zs, zz = quaternion.z() * zs;
            return new Affine3F64(
                    1d - (yy + zz), xy - wz       , xz + wy       , 0d,
                    xy + wz       , 1d - (xx + zz), yz - wx       , 0d,
                    xz - wy       , yz + wx       , 1d - (xx + yy), 0d
            );
        }

        @Override
        @AliasExclude
        public Affine3F64 of(Double m00, Double m01, Double m02, Double m03,
                             Double m10, Double m11, Double m12, Double m13,
                             Double m20, Double m21, Double m22, Double m23) {
            return new Affine3F64(
                    m00, m01, m02, m03,
                    m10, m11, m12, m13,
                    m20, m21, m22, m23
            );
        }

        @Override
        @NamedAlias(methodName = ZERO_FACTORY + UPPER_AFFINE3_FACTORY + F64)
        public Affine3F64 zero() {
            return of(
                    0d, 0d, 0d, 0d,
                    0d, 0d, 0d, 0d,
                    0d, 0d, 0d, 0d
            );
        }

        @Override
        @NamedAlias(methodName = ONE_FACTORY + UPPER_AFFINE3_FACTORY + F64)
        public Affine3F64 one() {
            return of(
                    1d, 1d, 1d, 1d,
                    1d, 1d, 1d, 1d,
                    1d, 1d, 1d, 1d
            );
        }

        @Override
        @NamedAlias(methodName = IDENTITY_FACTORY + UPPER_AFFINE3_FACTORY + F64)
        public Affine3F64 identity() {
            return of(
                    1d, 0d, 0d, 0d,
                    0d, 1d, 0d, 0d,
                    0d, 0d, 1d, 0d
            );
        }

        @Override
        @AliasExclude
        public Vec3F64.Ops vectorOps() {
            return Vec3F64.OPS;
        }

        @Override
        public Affine3F64 fromAxes(Vec3F64 xAxis, Vec3F64 yAxis, Vec3F64 zAxis, Vec3F64 translation) {
            return of(
                    xAxis.x(), yAxis.x(), zAxis.x(), translation.x(),
                    xAxis.y(), yAxis.y(), zAxis.y(), translation.y(),
                    xAxis.z(), yAxis.z(), zAxis.z(), translation.z()
            );
        }

        @Override
        public Affine3F64 fromTranslation(Vec3F64 translation) {
            return of(
                    1d, 0d, 0d, translation.x(),
                    0d, 1d, 0d, translation.y(),
                    0d, 0d, 1d, translation.z());
        }

        @Override
        public Affine3F64 fromScale(Vec3F64 scale) {
            return of(
                    scale.x(), 0d, 0d, 0d,
                    0d, scale.y(), 0d, 0d,
                    0d, 0d, scale.z(), 0d
            );
        }

        @Override
        public Affine3F64 transpose(Affine3F64 affine) {
            return of(
                    affine.m00(), affine.m10(), affine.m20(), affine.m03(),
                    affine.m01(), affine.m11(), affine.m21(), affine.m13(),
                    affine.m02(), affine.m12(), affine.m22(), affine.m23()
            );
        }

        @Override
        public Double determinant(Affine3F64 matrix) {
            return  fma(matrix.m00(), fma(matrix.m11(), matrix.m22(), -matrix.m12() * matrix.m21()),
                    fma(-matrix.m01(), fma(matrix.m10(), matrix.m22(), -matrix.m12() * matrix.m20()),
                            (matrix.m02()* fma(matrix.m10(), matrix.m21(), -matrix.m11() * matrix.m20()))));
        }

        @Override
        public Affine3F64 inverse(Affine3F64 matrix) throws ArithmeticException {

            final double det = determinant(matrix);
            if (det == 0d) throw new ArithmeticException("The matrix cannot be inverted since singular.");

            final double inv = 1d / det;
            final double m00 = fma(matrix.m11(), matrix.m22(), -matrix.m12() * matrix.m21()) * inv;
            final double m01 = fma(matrix.m02(), matrix.m21(), -matrix.m01() * matrix.m22()) * inv;
            final double m02 = fma(matrix.m01(), matrix.m12(), -matrix.m02() * matrix.m11()) * inv;

            final double m10 = fma(matrix.m12(), matrix.m20(), -matrix.m10() * matrix.m22()) * inv;
            final double m11 = fma(matrix.m00(), matrix.m22(), -matrix.m02() * matrix.m20()) * inv;
            final double m12 = fma(matrix.m02(), matrix.m10(), -matrix.m00() * matrix.m12()) * inv;

            final double m20 = fma(matrix.m10(), matrix.m21(), -matrix.m11() * matrix.m20()) * inv;
            final double m21 = fma(matrix.m01(), matrix.m20(), -matrix.m00() * matrix.m21()) * inv;
            final double m22 = fma(matrix.m00(), matrix.m11(), -matrix.m01() * matrix.m10()) * inv;

            return of(
                    m00, m01, m02, -fma(m00, matrix.m03(), fma(m01, matrix.m13(), m02 * matrix.m23())),
                    m10, m11, m12, -fma(m10, matrix.m03(), fma(m11, matrix.m13(), m12 * matrix.m23())),
                    m20, m21, m22, -fma(m20, matrix.m03(), fma(m21, matrix.m13(), m22 * matrix.m23()))
            );
        }

        @Override
        public Vec3F64 multiply(Affine3F64 matrix, Vec3F64 vector) {
            return new Vec3F64(
                    fma(matrix.m00(), vector.x(), fma(matrix.m01(), vector.y(), fma(matrix.m02(), vector.z(), matrix.m03()))),
                    fma(matrix.m10(), vector.x(), fma(matrix.m11(), vector.y(), fma(matrix.m12(), vector.z(), matrix.m13()))),
                    fma(matrix.m20(), vector.x(), fma(matrix.m21(), vector.y(), fma(matrix.m22(), vector.z(), matrix.m23())))
            );
        }

        @Override
        public Affine3F64 multiply(Affine3F64 matrix, Double scalar) {
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
        public Affine3F64 add(Affine3F64 op1, Affine3F64 op2) {
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
        public Affine3F64 subtract(Affine3F64 op1, Affine3F64 op2) {
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
        public Affine3F64 multiply(Affine3F64 op1, Affine3F64 op2) {
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
        public Affine3F64 remainder(Affine3F64 op1, Affine3F64 op2) {
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
        public Affine3F64 negated(Affine3F64 operand) {
            return of(
                    -operand.m00(), -operand.m01(), -operand.m02(), -operand.m03(),
                    -operand.m10(), -operand.m11(), -operand.m12(), -operand.m13(),
                    -operand.m20(), -operand.m21(), -operand.m22(), -operand.m23()
            );
        }

        @Override
        public Affine3F64 normalMatrix(Affine3F64 matrix) throws ArithmeticException {
            final var transposed = transpose(inverse(matrix));
            return of(
                    transposed.m00(), transposed.m01(), transposed.m02(), 0d,
                    transposed.m10(), transposed.m11(), transposed.m12(), 0d,
                    transposed.m20(), transposed.m21(), transposed.m22(), 0d
            );
        }

        @Override
        public boolean isSingular(Affine3F64 matrix) {
            return determinant(matrix) == 0d;
        }

        @Override
        public Affine3F64 divide(Affine3F64 op1, Affine3F64 op2) throws ArithmeticException {
            return multiply(op1, inverse(op2));
        }
    }
}
