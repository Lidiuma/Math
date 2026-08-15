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

import jdk.internal.vm.annotation.LooselyConsistentValue;
import jdk.internal.vm.annotation.NullRestricted;
import org.lidiuma.math.api.matrix.Matrix4;
import org.lidiuma.math.api.traits.matrix.Matrix4Ops;
import org.lidiuma.math.processor.Alias;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.vector.Vec4F64;
import static org.lidiuma.math.internal.AnnotationConst.MATRIX4_FACTORY;
import static org.lidiuma.math.internal.AnnotationConst.MATRIX_OUT;

/// @see Matrix4
@LooselyConsistentValue
@FactoryAlias(methodName = MATRIX4_FACTORY, outputClass = MATRIX_OUT)
public value record Matrix4F64(
        // I'm not using an array because it's an identity object, and this reads and feels better to work with.
        @NullRestricted Double m00, @NullRestricted Double m01, @NullRestricted Double m02, @NullRestricted Double m03,
        @NullRestricted Double m10, @NullRestricted Double m11, @NullRestricted Double m12, @NullRestricted Double m13,
        @NullRestricted Double m20, @NullRestricted Double m21, @NullRestricted Double m22, @NullRestricted Double m23,
        @NullRestricted Double m30, @NullRestricted Double m31, @NullRestricted Double m32, @NullRestricted Double m33
) implements Matrix4<Double> {

    @Alias(outputClass = MATRIX_OUT)
    public static final Ops OPS = new Ops();

    public static final class Ops implements Matrix4Ops<Matrix4F64, Vec4F64, Double> {

        @Override
        @AliasExclude
        public Matrix4F64 of(Double m00, Double m01, Double m02, Double m03,
                             Double m10, Double m11, Double m12, Double m13,
                             Double m20, Double m21, Double m22, Double m23,
                             Double m30, Double m31, Double m32, Double m33) {
            return new Matrix4F64(
                    m00, m01, m02, m03,
                    m10, m11, m12, m13,
                    m20, m21, m22, m23,
                    m30, m31, m32, m33
            );
        }

        @Override
        @AliasExclude
        public Vec4F64.Ops vectorOps() {
            return Vec4F64.OPS;
        }

        @Override
        @AliasExclude
        public Matrix4F64 zero() {
            return Matrix4Ops.super.zero();
        }

        @Override
        @AliasExclude
        public Matrix4F64 one() {
            return Matrix4Ops.super.one();
        }

        @Override
        @AliasExclude
        public Matrix4F64 identity() {
            return Matrix4Ops.super.identity();
        }
    }
}
