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

import org.lidiuma.math.api.matrix.Matrix4;
import org.lidiuma.math.api.traits.matrix.Matrix4Ops;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.processor.NamedAlias;
import org.lidiuma.math.vector.Vec4F32;
import static org.lidiuma.math.internal.AnnotationConst.*;

@FactoryAlias(methodName = MATRIX4_FACTORY, outputClass = MATRIX_OUT)
public record Matrix4F32(
        // I'm not using an array because it's an identity object, and this reads and feels better to work with.
        @Override Float m00, @Override Float m01, @Override Float m02, @Override Float m03,
        @Override Float m10, @Override Float m11, @Override Float m12, @Override Float m13,
        @Override Float m20, @Override Float m21, @Override Float m22, @Override Float m23,
        @Override Float m30, @Override Float m31, @Override Float m32, @Override Float m33
) implements Matrix4<Float> {

    @FieldAlias(outputClass = MATRIX_OUT)
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized matrix from a generic matrix.
    @AliasExclude // This method can be a performance sink if used inappropriately, so I exclude it from the alias.
    public Matrix4F32(Matrix4<Float> matrix4) {
        this(
                matrix4.m00(), matrix4.m01(), matrix4.m02(), matrix4.m03(),
                matrix4.m10(), matrix4.m11(), matrix4.m12(), matrix4.m13(),
                matrix4.m20(), matrix4.m21(), matrix4.m22(), matrix4.m23(),
                matrix4.m30(), matrix4.m31(), matrix4.m32(), matrix4.m33()
        );
    }

    public static final class Ops implements Matrix4Ops<Matrix4F32, Vec4F32, Float> {

        private Ops() {}

        @Override
        @AliasExclude
        public Matrix4F32 of(Float m00, Float m01, Float m02, Float m03,
                             Float m10, Float m11, Float m12, Float m13,
                             Float m20, Float m21, Float m22, Float m23,
                             Float m30, Float m31, Float m32, Float m33) {
            return new Matrix4F32(
                    m00, m01, m02, m03,
                    m10, m11, m12, m13,
                    m20, m21, m22, m23,
                    m30, m31, m32, m33
            );
        }

        @Override
        @NamedAlias(methodName = ZERO_FACTORY + UPPER_MATRIX4_FACTORY + F32)
        public Matrix4F32 zero() {
            return Matrix4Ops.super.zero();
        }

        @Override
        @NamedAlias(methodName = ONE_FACTORY + UPPER_MATRIX4_FACTORY + F32)
        public Matrix4F32 one() {
            return Matrix4Ops.super.one();
        }

        @Override
        @NamedAlias(methodName = IDENTITY_FACTORY + UPPER_MATRIX4_FACTORY + F32)
        public Matrix4F32 identity() {
            return Matrix4Ops.super.identity();
        }

        @Override
        @AliasExclude
        public Vec4F32.Ops vectorOps() {
            return Vec4F32.OPS;
        }
    }
}
