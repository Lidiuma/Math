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
import org.lidiuma.math.api.matrix.Affine2Ops;
import org.lidiuma.math.api.vector.Vector2Ops;
import org.lidiuma.math.numerics.FloatNumeric;
import org.lidiuma.math.vector.Vec2F32;

public value record Affine2F32(
        @NullRestricted Float m00, @NullRestricted Float m01, @NullRestricted Float m02,
        @NullRestricted Float m10, @NullRestricted Float m11, @NullRestricted Float m12
) implements Affine2<Float> {

    public static final Affine2Ops<Affine2F32, Vec2F32, Float> WITNESS = new Affine2Ops<>() {

        @Override
        public Affine2F32 of(Float m00, Float m01, Float m02, Float m10, Float m11, Float m12) {
            return new Affine2F32(
                    m00, m01, m02,
                    m10, m11, m12
            );
        }

        @Override
        public Vector2Ops<Vec2F32, Float> vectorOps() {
            return Vec2F32.WITNESS;
        }

        @Override
        public FloatNumeric scalarOps() {
            return FloatNumeric.WITNESS;
        }
    };

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
}
