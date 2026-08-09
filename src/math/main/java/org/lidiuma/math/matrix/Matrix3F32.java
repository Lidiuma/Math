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
import org.lidiuma.math.api.matrix.Matrix3;
import jdk.internal.vm.annotation.LooselyConsistentValue;
import org.lidiuma.math.api.traits.matrix.Matrix3Ops;
import org.lidiuma.math.api.traits.vector.Vector3Ops;
import org.lidiuma.math.numerics.FloatNumeric;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.Alias;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.vector.Vec3F32;

/// @see Matrix3
@LooselyConsistentValue
@FactoryAlias(methodName = "matrix3", outputClass = "Matrices")
public value record Matrix3F32(
        @NullRestricted Float m00, @NullRestricted Float m01, @NullRestricted Float m02,
        @NullRestricted Float m10, @NullRestricted Float m11, @NullRestricted Float m12,
        @NullRestricted Float m20, @NullRestricted Float m21, @NullRestricted Float m22
) implements Matrix3<Float> {

    @Alias(outputClass = "Matrices")
    public static final Ops OPS = new Ops();

    public static final class Ops implements Matrix3Ops<Matrix3F32, Vec3F32, Float> {

        @Override
        @AliasExclude
        public Matrix3F32 of(Float m00, Float m01, Float m02,
                             Float m10, Float m11, Float m12,
                             Float m20, Float m21, Float m22) {
            return new Matrix3F32(
                    m00, m01, m02,
                    m10, m11, m12,
                    m20, m21, m22
            );
        }

        @Override
        @AliasExclude
        public Vector3Ops<Vec3F32, Float> vectorOps() {
            return Vec3F32.OPS;
        }

        @Override
        @AliasExclude
        public FloatNumeric scalarOps() {
            return FloatNumeric.OPS;
        }

        @Override
        @AliasExclude
        public Matrix3F32 zero() {
            return Matrix3Ops.super.zero();
        }

        @Override
        @AliasExclude
        public Matrix3F32 one() {
            return Matrix3Ops.super.one();
        }

        @Override
        @AliasExclude
        public Matrix3F32 identity() {
            return Matrix3Ops.super.identity();
        }
    }
}
