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
import org.lidiuma.math.numerics.DoubleNumeric;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.Alias;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.vector.Vec3F64;

/// @see Matrix3
@LooselyConsistentValue
@FactoryAlias(methodName = "matrix3", outputClass = "Matrices")
public value record Matrix3F64(
        @NullRestricted Double m00, @NullRestricted Double m01, @NullRestricted Double m02,
        @NullRestricted Double m10, @NullRestricted Double m11, @NullRestricted Double m12,
        @NullRestricted Double m20, @NullRestricted Double m21, @NullRestricted Double m22
) implements Matrix3<Double> {

    @Alias(outputClass = "Matrices")
    public static final Ops OPS = new Ops();

    public static final class Ops implements Matrix3Ops<Matrix3F64, Vec3F64, Double> {

        @Override
        @AliasExclude
        public Matrix3F64 of(Double m00, Double m01, Double m02,
                             Double m10, Double m11, Double m12,
                             Double m20, Double m21, Double m22) {
            return new Matrix3F64(
                    m00, m01, m02,
                    m10, m11, m12,
                    m20, m21, m22
            );
        }

        @Override
        @AliasExclude
        public Vector3Ops<Vec3F64, Double> vectorOps() {
            return Vec3F64.OPS;
        }

        @Override
        @AliasExclude
        public DoubleNumeric scalarOps() {
            return DoubleNumeric.OPS;
        }

        @Override
        @AliasExclude
        public Matrix3F64 zero() {
            return Matrix3Ops.super.zero();
        }

        @Override
        @AliasExclude
        public Matrix3F64 one() {
            return Matrix3Ops.super.one();
        }

        @Override
        @AliasExclude
        public Matrix3F64 identity() {
            return Matrix3Ops.super.identity();
        }
    }
}
