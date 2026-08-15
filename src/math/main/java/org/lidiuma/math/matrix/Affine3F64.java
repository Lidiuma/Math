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
import org.lidiuma.math.api.matrix.Affine3;
import org.lidiuma.math.api.traits.matrix.Affine3Ops;
import org.lidiuma.math.api.traits.matrix.FloatingAffineOps;
import org.lidiuma.math.processor.Alias;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.NamedAlias;
import org.lidiuma.math.rotation.QuaternionF64;
import org.lidiuma.math.vector.Vec3F64;
import static org.lidiuma.math.internal.AnnotationConst.*;

@FactoryAlias(methodName = AFFINE3_FACTORY, outputClass = MATRIX_OUT)
public value record Affine3F64(
        @NullRestricted Double m00, @NullRestricted Double m01, @NullRestricted Double m02, @NullRestricted Double m03,
        @NullRestricted Double m10, @NullRestricted Double m11, @NullRestricted Double m12, @NullRestricted Double m13,
        @NullRestricted Double m20, @NullRestricted Double m21, @NullRestricted Double m22, @NullRestricted Double m23
) implements Affine3<Double> {

    @Alias(outputClass = MATRIX_OUT)
    public static final Ops OPS = new Ops();

    @NamedAlias(methodName = AFFINE3_FACTORY + F64)
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

    public static final class Ops implements Affine3Ops<Affine3F64, Vec3F64, Double>, FloatingAffineOps<Affine3F64, Vec3F64, QuaternionF64, Double> {

        /// Creates a transformation matrix from translation, rotation, and scale.
        public Affine3F64 fromTRS(Affine3F64 translation, Affine3F64 rotation, Affine3F64 scale) {
            return multiply(translation, multiply(rotation, scale));
        }

        /// Creates a transformation matrix from translation, rotation, and scale.
        public Affine3F64 fromTRS(Vec3F64 translation, QuaternionF64 rotation, Vec3F64 scale) {
            final var trs = fromTranslation(translation);
            final var rot = fromRotation(rotation);
            final var scl = fromScale(scale);
            return fromTRS(trs, rot, scl);
        }

        @Override
        public Affine3F64 fromRotation(QuaternionF64 quaternion) {

            final double xs = quaternion.x() * 2d, ys = quaternion.y() * 2d, zs = quaternion.z() * 2d;
            final double wx = quaternion.w() * xs, wy = quaternion.w() * ys, wz = quaternion.w() * zs;
            final double xx = quaternion.x() * xs, xy = quaternion.x() * ys, xz = quaternion.x() * zs;
            final double yy = quaternion.y() * ys, yz = quaternion.y() * zs, zz = quaternion.z() * zs;

            final double m00 = 1d - (yy + zz), m01 = xy - wz       , m02 = xz + wy;
            final double m10 = xy + wz       , m11 = 1d - (xx + zz), m12 = yz - wx;
            final double m20 = xz - wy       , m21 = yz + wx       , m22 = 1d - (xx + yy);
            return new Affine3F64(
                    m00, m01, m02, 0d,
                    m10, m11, m12, 0d,
                    m20, m21, m22, 0d
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
        @AliasExclude
        public Vec3F64.Ops vectorOps() {
            return Vec3F64.OPS;
        }

        @Override
        @AliasExclude
        public Affine3F64 zero() {
            return Affine3Ops.super.zero();
        }

        @Override
        @AliasExclude
        public Affine3F64 one() {
            return Affine3Ops.super.one();
        }

        @Override
        @AliasExclude
        public Affine3F64 identity() {
            return Affine3Ops.super.identity();
        }
    }
}
