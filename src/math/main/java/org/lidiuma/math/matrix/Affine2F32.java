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
import org.lidiuma.math.processor.Alias;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.NamedAlias;
import org.lidiuma.math.rotation.AngleF32;
import org.lidiuma.math.vector.Vec2F32;
import static org.lidiuma.math.internal.AnnotationConst.*;

@FactoryAlias(methodName = AFFINE2_FACTORY, outputClass = MATRIX_OUT)
public value record Affine2F32(
        @NullRestricted Float m00, @NullRestricted Float m01, @NullRestricted Float m02,
        @NullRestricted Float m10, @NullRestricted Float m11, @NullRestricted Float m12
) implements Affine2<Float> {

    @Alias(outputClass = MATRIX_OUT)
    public static final Ops OPS = new Ops();

    @NamedAlias(methodName = AFFINE2_FACTORY + F32)
    public Affine2F32(Affine2<Float> affine2) {
        this(
                affine2.m00(), affine2.m01(), affine2.m02(),
                affine2.m10(), affine2.m11(), affine2.m12()
        );
    }

    @Override
    public Float m20() {
        return 0f;
    }

    @Override
    public Float m21() {
        return 0f;
    }

    @Override
    public Float m22() {
        return 1f;
    }

    public static final class Ops implements Affine2Ops<Affine2F32, Vec2F32, Float>, FloatingAffineOps<Affine2F32, Vec2F32, AngleF32, Float> {

        /// Creates a transformation matrix from translation, rotation, and scale.
        public Affine2F32 fromTRS(Affine2F32 translation, Affine2F32 rotation, Affine2F32 scale) {
            return multiply(translation, multiply(rotation, scale));
        }

        /// Creates a transformation matrix from translation, rotation, and scale.
        public Affine2F32 fromTRS(Vec2F32 translation, AngleF32 rotation, Vec2F32 scale) {
            final var trs = fromTranslation(translation);
            final var rot = fromRotation(rotation);
            final var scl = fromScale(scale);
            return fromTRS(trs, rot, scl);
        }

        @Override
        public Affine2F32 fromRotation(AngleF32 angle) {
            final float cos = (float) Math.cos(angle.radian());
            final float sin = (float) Math.sin(angle.radian());
            return new Affine2F32(
                    cos, -sin, 0f,
                    sin, cos, 0f
            );
        }

        @Override
        @AliasExclude
        public Affine2F32 of(Float m00, Float m01, Float m02, Float m10, Float m11, Float m12) {
            return new Affine2F32(
                    m00, m01, m02,
                    m10, m11, m12
            );
        }

        @Override
        @AliasExclude
        public Vec2F32.Ops vectorOps() {
            return Vec2F32.OPS;
        }

        @Override
        @AliasExclude
        public Affine2F32 zero() {
            return Affine2Ops.super.zero();
        }

        @Override
        @AliasExclude
        public Affine2F32 one() {
            return Affine2Ops.super.one();
        }

        @Override
        @AliasExclude
        public Affine2F32 identity() {
            return Affine2Ops.super.identity();
        }
    }
}
