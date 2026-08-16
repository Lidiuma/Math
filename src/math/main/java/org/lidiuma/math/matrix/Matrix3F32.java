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
import org.lidiuma.math.api.matrix.Matrix3;
import org.lidiuma.math.api.traits.matrix.Matrix3Ops;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.processor.NamedAlias;
import org.lidiuma.math.vector.Vec3F32;
import static org.lidiuma.math.internal.AnnotationConst.*;

/// @see Matrix3
@LooselyConsistentValue
@FactoryAlias(methodName = MATRIX3_FACTORY, outputClass = MATRIX_OUT)
public value record Matrix3F32(
        @NullRestricted Float m00, @NullRestricted Float m01, @NullRestricted Float m02,
        @NullRestricted Float m10, @NullRestricted Float m11, @NullRestricted Float m12,
        @NullRestricted Float m20, @NullRestricted Float m21, @NullRestricted Float m22
) implements Matrix3<Float> {

    @FieldAlias(outputClass = MATRIX_OUT)
    public static final Ops OPS = new Ops();

    @NamedAlias(methodName = MATRIX3_FACTORY + F32)
    public Matrix3F32(Matrix3<Float> matrix3) {
        this(
                matrix3.m00(), matrix3.m01(), matrix3.m02(),
                matrix3.m10(), matrix3.m11(), matrix3.m12(),
                matrix3.m20(), matrix3.m21(), matrix3.m22()
        );
    }

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
        public Vec3F32.Ops vectorOps() {
            return Vec3F32.OPS;
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
