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

package org.lidiuma.math.vector;

import org.lidiuma.math.api.traits.vector.Vector4Ops;
import org.lidiuma.math.api.tuple.UnaryTuple4;
import org.lidiuma.math.api.vector.Vector4;
import org.lidiuma.math.numerics.LongNumeric;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.processor.NamedAlias;
import static org.lidiuma.math.internal.AnnotationConst.*;

@FactoryAlias(methodName = VEC4_FACTORY, outputClass = VECTOR_OUT)
public value record Vec4I64(
        @Override Long x,
        @Override Long y,
        @Override Long z,
        @Override Long w
) implements Vector4<Long> {

    @FieldAlias(outputClass = VECTOR_OUT)
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized vector from a generic tuple.
    @AliasExclude // This method can be a performance sink if used inappropriately, so I exclude it from the alias.
    public Vec4I64(UnaryTuple4<Long> vec) {
        this(vec.x(), vec.y(), vec.z(), vec.w());
    }

    public static final value class Ops implements Vector4Ops<Vec4I64, Long> {

        private Ops() {}

        @Override
        @AliasExclude
        public Vec4I64 of(Long x, Long y, Long z, Long w) {
            return new Vec4I64(x, y, z, w);
        }

        @Override
        public Vec4I64 signum(Vec4I64 vector) {
            return of(
                    (long) Long.signum(vector.x()),
                    (long) Long.signum(vector.y()),
                    (long) Long.signum(vector.z()),
                    (long) Long.signum(vector.w())
            );
        }

        @Override
        @NamedAlias(methodName = ZERO_FACTORY + UPPER_VEC4_FACTORY + I64)
        public Vec4I64 zero() {
            return of(0L, 0L, 0L, 0L);
        }

        @Override
        @NamedAlias(methodName = ONE_FACTORY + UPPER_VEC4_FACTORY + I64)
        public Vec4I64 one() {
            return of(1L, 1L, 1L, 1L);
        }

        @Override
        @AliasExclude
        public LongNumeric scalarOps() {
            return LongNumeric.OPS;
        }

        /*
        Handwritten to remove GC collections when used polymorphically (different generic parameters).
        Speed is more or less the same, but without a rare case of the JIT failing giving x10 less performance.
        This unfortunately creates code duplication, and I'm sure some methods are fine as-is,
        but writing them anyway is faster than making sure with benchmarking.
        */

        @Override
        public Vec4I64 abs(Vec4I64 vector) {
            return of(
                    Math.abs(vector.x()),
                    Math.abs(vector.y()),
                    Math.abs(vector.z()),
                    Math.abs(vector.w())
            );
        }

        @Override
        public Long sum(Vec4I64 vector) {
            return vector.x() + vector.y() + vector.z() + vector.w();
        }

        @Override
        public Vec4I64 multiply(Vec4I64 vector, Long scalar) {
            return multiply(vector, of(scalar, scalar, scalar, scalar));
        }

        @Override
        public Vec4I64 clamp(Vec4I64 vector, Long min, Long max) {
            return clamp(vector, of(min, min, min, min), of(max, max, max, max));
        }

        @Override
        public Vec4I64 clamp(Vec4I64 value, Vec4I64 min, Vec4I64 max) {
            return of(
                    Math.clamp(value.x(), min.x(), max.x()),
                    Math.clamp(value.y(), min.y(), max.y()),
                    Math.clamp(value.z(), min.z(), max.z()),
                    Math.clamp(value.w(), min.w(), max.w())
            );
        }

        @Override
        public Vec4I64 add(Vec4I64 op1, Vec4I64 op2) {
            return of(
                    op1.x() + op2.x(),
                    op1.y() + op2.y(),
                    op1.z() + op2.z(),
                    op1.w() + op2.w()
            );
        }

        @Override
        public Vec4I64 multiply(Vec4I64 op1, Vec4I64 op2) {
            return of(
                    op1.x() * op2.x(),
                    op1.y() * op2.y(),
                    op1.z() * op2.z(),
                    op1.w() * op2.w()
            );
        }

        @Override
        public Vec4I64 divide(Vec4I64 op1, Vec4I64 op2) {
            return of(
                    op1.x() / op2.x(),
                    op1.y() / op2.y(),
                    op1.z() / op2.z(),
                    op1.w() / op2.w()
            );
        }

        @Override
        public Vec4I64 remainder(Vec4I64 op1, Vec4I64 op2) {
            return of(
                    op1.x() % op2.x(),
                    op1.y() % op2.y(),
                    op1.z() % op2.z(),
                    op1.w() % op2.w()
            );
        }

        @Override
        public Vec4I64 negated(Vec4I64 operand) {
            return of(
                    -operand.x(),
                    -operand.y(),
                    -operand.z(),
                    -operand.w()
            );
        }

        @Override
        public Long distanceSquared(Vec4I64 a, Vec4I64 b) {
            final var sub = subtract(a, b);
            return sum(multiply(sub, sub));
        }

        @Override
        public Long lengthSquared(Vec4I64 vector) {
            return dot(vector, vector);
        }

        @Override
        public Long dot(Vec4I64 v1, Vec4I64 v2) {
            return sum(multiply(v1, v2));
        }

        @Override
        public Vec4I64 subtract(Vec4I64 op1, Vec4I64 op2) {
            return of(
                    op1.x() - op2.x(),
                    op1.y() - op2.y(),
                    op1.z() - op2.z(),
                    op1.w() - op2.w()
            );
        }
    }
}
