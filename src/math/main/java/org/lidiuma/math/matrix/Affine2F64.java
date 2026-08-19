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
import org.lidiuma.math.api.matrix.Affine2;
import org.lidiuma.math.api.traits.matrix.Affine2Ops;
import org.lidiuma.math.api.traits.matrix.FloatingAffineOps;
import org.lidiuma.math.internal.Strict;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.processor.NamedAlias;
import org.lidiuma.math.rotation.AngleF64;
import org.lidiuma.math.vector.Vec2F64;
import static org.lidiuma.math.internal.AnnotationConst.*;

@FactoryAlias(methodName = AFFINE2_FACTORY, outputClass = MATRIX_OUT)
public value record Affine2F64(
        @NullRestricted Double m00, @NullRestricted Double m01, @NullRestricted Double m02,
        @NullRestricted Double m10, @NullRestricted Double m11, @NullRestricted Double m12
) implements Affine2<Double> {

    @FieldAlias(outputClass = MATRIX_OUT)
    public static final Ops OPS = new Ops();

    @NamedAlias(methodName = AFFINE2_FACTORY + F64)
    public Affine2F64(Affine2<Double> affine2) {
        this(
                affine2.m00(), affine2.m01(), affine2.m02(),
                affine2.m10(), affine2.m11(), affine2.m12()
        );
    }

    @Override
    public Double m20() {
        return 0d;
    }

    @Override
    public Double m21() {
        return 0d;
    }

    @Override
    public Double m22() {
        return 1d;
    }

    public static final value class Ops implements Affine2Ops<Affine2F64, Vec2F64, Double>, FloatingAffineOps<Affine2F64, Vec2F64, AngleF64, Double> {

        private Ops() {}

        /// Creates a transformation matrix from translation, rotation, and scale.
        public Affine2F64 fromTRS(Affine2F64 translation, Affine2F64 rotation, Affine2F64 scale) {
            return multiply(translation, multiply(rotation, scale));
        }

        /// Creates a transformation matrix from translation, rotation, and scale.
        public Affine2F64 fromTRS(Vec2F64 translation, AngleF64 rotation, Vec2F64 scale) {
            final var trs = fromTranslation(translation);
            final var rot = fromRotation(rotation);
            final var scl = fromScale(scale);
            return fromTRS(trs, rot, scl);
        }

        @Override
        public Affine2F64 fromRotation(AngleF64 angle) {
            final double cos = Strict.cos(angle.radian());
            final double sin = Strict.sin(angle.radian());
            return new Affine2F64(
                    cos, -sin, 0d,
                    sin, cos, 0d
            );
        }

        @Override
        @AliasExclude
        public Affine2F64 of(Double m00, Double m01, Double m02, Double m10, Double m11, Double m12) {
            return new Affine2F64(
                    m00, m01, m02,
                    m10, m11, m12
            );
        }

        @Override
        @NamedAlias(methodName = ZERO_FACTORY + UPPER_AFFINE2_FACTORY + F64)
        public Affine2F64 zero() {
            return Affine2Ops.super.zero();
        }

        @Override
        @NamedAlias(methodName = ONE_FACTORY + UPPER_AFFINE2_FACTORY + F64)
        public Affine2F64 one() {
            return Affine2Ops.super.one();
        }

        @Override
        @NamedAlias(methodName = IDENTITY_FACTORY + UPPER_AFFINE2_FACTORY + F64)
        public Affine2F64 identity() {
            return Affine2Ops.super.identity();
        }

        @Override
        @AliasExclude
        public Vec2F64.Ops vectorOps() {
            return Vec2F64.OPS;
        }
    }
}
