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
import org.lidiuma.math.rotation.QuaternionF32;
import org.lidiuma.math.vector.Vec3F32;
import static org.lidiuma.math.internal.AnnotationConst.AFFINE3_FACTORY;
import static org.lidiuma.math.internal.AnnotationConst.MATRIX_OUT;

@FactoryAlias(methodName = AFFINE3_FACTORY, outputClass = MATRIX_OUT)
public value record Affine3F32(
        @NullRestricted Float m00, @NullRestricted Float m01, @NullRestricted Float m02, @NullRestricted Float m03,
        @NullRestricted Float m10, @NullRestricted Float m11, @NullRestricted Float m12, @NullRestricted Float m13,
        @NullRestricted Float m20, @NullRestricted Float m21, @NullRestricted Float m22, @NullRestricted Float m23
) implements Affine3<Float> {

    @Alias(outputClass = MATRIX_OUT)
    public static final Ops OPS = new Ops();

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

    public static final class Ops implements Affine3Ops<Affine3F32, Vec3F32, Float>, FloatingAffineOps<Affine3F32, Vec3F32, QuaternionF32, Float> {

        /// Creates a transformation matrix from translation, rotation, and scale.
        public Affine3F32 fromTRS(Affine3F32 translation, Affine3F32 rotation, Affine3F32 scale) {
            return multiply(translation, multiply(rotation, scale));
        }

        /// Creates a transformation matrix from translation, rotation, and scale.
        public Affine3F32 fromTRS(Vec3F32 translation, QuaternionF32 rotation, Vec3F32 scale) {
            final var trs = fromTranslation(translation);
            final var rot = fromRotation(rotation);
            final var scl = fromScale(scale);
            return fromTRS(trs, rot, scl);
        }

        @Override
        public Affine3F32 fromRotation(QuaternionF32 quaternion) {

            final float xs = quaternion.x() * 2f, ys = quaternion.y() * 2f, zs = quaternion.z() * 2f;
            final float wx = quaternion.w() * xs, wy = quaternion.w() * ys, wz = quaternion.w() * zs;
            final float xx = quaternion.x() * xs, xy = quaternion.x() * ys, xz = quaternion.x() * zs;
            final float yy = quaternion.y() * ys, yz = quaternion.y() * zs, zz = quaternion.z() * zs;

            final float m00 = 1f - (yy + zz), m01 = xy - wz       , m02 = xz + wy;
            final float m10 = xy + wz       , m11 = 1f - (xx + zz), m12 = yz - wx;
            final float m20 = xz - wy       , m21 = yz + wx       , m22 = 1f - (xx + yy);
            return new Affine3F32(
                    m00, m01, m02, 0f,
                    m10, m11, m12, 0f,
                    m20, m21, m22, 0f
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
        @AliasExclude
        public Vec3F32.Ops vectorOps() {
            return Vec3F32.OPS;
        }

        @Override
        @AliasExclude
        public Affine3F32 zero() {
            return Affine3Ops.super.zero();
        }

        @Override
        @AliasExclude
        public Affine3F32 one() {
            return Affine3Ops.super.one();
        }

        @Override
        @AliasExclude
        public Affine3F32 identity() {
            return Affine3Ops.super.identity();
        }
    }
}
