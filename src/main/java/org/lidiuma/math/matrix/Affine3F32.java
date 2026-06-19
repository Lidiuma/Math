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
import org.lidiuma.math.api.matrix.Affine3Ops;
import org.lidiuma.math.numerics.FloatNumeric;
import org.lidiuma.math.vector.Vec3F32;

public value record Affine3F32(
        @NullRestricted Float m00, @NullRestricted Float m01, @NullRestricted Float m02, @NullRestricted Float m03,
        @NullRestricted Float m10, @NullRestricted Float m11, @NullRestricted Float m12, @NullRestricted Float m13,
        @NullRestricted Float m20, @NullRestricted Float m21, @NullRestricted Float m22, @NullRestricted Float m23
) implements Affine3<Float> {

    public static final Affine3Ops<Affine3F32, Vec3F32, Float> WITNESS = new Affine3Ops<>() {

        @Override
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
        public Vec3F32 multiply(Affine3F32 matrix, Vec3F32 vector) {
            final var x = matrix.m00() * vector.x() + matrix.m01() * vector.y() + matrix.m02() * vector.z() + matrix.m03();
            final var y = matrix.m10() * vector.x() + matrix.m11() * vector.y() + matrix.m12() * vector.z() + matrix.m13();
            final var z = matrix.m20() * vector.x() + matrix.m21() * vector.y() + matrix.m22() * vector.z() + matrix.m23();
            return new Vec3F32(x, y, z);
        }

        @Override
        public FloatNumeric scalarOps() {
            return FloatNumeric.WITNESS;
        }
    };

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
}
