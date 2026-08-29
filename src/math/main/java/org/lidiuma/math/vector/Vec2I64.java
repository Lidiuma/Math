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

import org.lidiuma.math.api.traits.vector.Vector2Ops;
import org.lidiuma.math.api.tuple.UnaryTuple2;
import org.lidiuma.math.api.vector.Vector2;
import org.lidiuma.math.internal.Math28;
import org.lidiuma.math.numerics.LongNumeric;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.processor.NamedAlias;
import static org.lidiuma.math.internal.AnnotationConst.*;

@FactoryAlias(methodName = VEC2_FACTORY, outputClass = VECTOR_OUT)
public record Vec2I64(
        @Override Long x,
        @Override Long y
) implements Vector2<Long> {

    @FieldAlias(outputClass = VECTOR_OUT)
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized vector from a generic tuple.
    @AliasExclude // This method can be a performance sink if used inappropriately, so I exclude it from the alias.
    public Vec2I64(UnaryTuple2<Long> vec) {
        this(vec.x(), vec.y());
    }

    public static final class Ops implements Vector2Ops<Vec2I64, Long> {

        private Ops() {}

        @Override
        @AliasExclude
        public Vec2I64 of(Long x, Long y) {
            return new Vec2I64(x, y);
        }

        @Override
        public Vec2I64 signum(Vec2I64 vector) {
            return of(
                    (long) Long.signum(vector.x()),
                    (long) Long.signum(vector.y())
            );
        }

        @Override
        @NamedAlias(methodName = ZERO_FACTORY + UPPER_VEC2_FACTORY + I64)
        public Vec2I64 zero() {
            return of(0L, 0L);
        }

        @Override
        @NamedAlias(methodName = ONE_FACTORY + UPPER_VEC2_FACTORY + I64)
        public Vec2I64 one() {
            return of(1L, 1L);
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
        public Vec2I64 abs(Vec2I64 vector) {
            return of(
                    Math.abs(vector.x()),
                    Math.abs(vector.y())
            );
        }

        @Override
        public Long cross(Vec2I64 v1, Vec2I64 v2) {
            return v1.x() * v2.y() - v1.y() * v2.x();
        }

        @Override
        public Long sum(Vec2I64 vector) {
            return vector.x() + vector.y();
        }

        @Override
        public Vec2I64 multiply(Vec2I64 vector, Long scalar) {
            return multiply(vector, of(scalar, scalar));
        }

        @Override
        public Vec2I64 clamp(Vec2I64 vector, Long min, Long max) {
            return clamp(vector, of(min, min), of(max, max));
        }

        @Override
        public Vec2I64 clamp(Vec2I64 value, Vec2I64 min, Vec2I64 max) {
            return of(
                    Math28.clamp(value.x(), min.x(), max.x()),
                    Math28.clamp(value.y(), min.y(), max.y())
            );
        }

        @Override
        public Vec2I64 add(Vec2I64 op1, Vec2I64 op2) {
            return of(
                    op1.x() + op2.x(),
                    op1.y() + op2.y()
            );
        }

        @Override
        public Vec2I64 multiply(Vec2I64 op1, Vec2I64 op2) {
            return of(
                    op1.x() * op2.x(),
                    op1.y() * op2.y()
            );
        }

        @Override
        public Vec2I64 divide(Vec2I64 op1, Vec2I64 op2) {
            return of(
                    op1.x() / op2.x(),
                    op1.y() / op2.y()
            );
        }

        @Override
        public Vec2I64 remainder(Vec2I64 op1, Vec2I64 op2) {
            return of(
                    op1.x() % op2.x(),
                    op1.y() % op2.y()
            );
        }

        @Override
        public Vec2I64 negated(Vec2I64 operand) {
            return of(
                    -operand.x(),
                    -operand.y()
            );
        }

        @Override
        public Long distanceSquared(Vec2I64 a, Vec2I64 b) {
            final var sub = subtract(a, b);
            return sum(multiply(sub, sub));
        }

        @Override
        public Long lengthSquared(Vec2I64 vector) {
            return dot(vector, vector);
        }

        @Override
        public Long dot(Vec2I64 v1, Vec2I64 v2) {
            return sum(multiply(v1, v2));
        }

        @Override
        public Vec2I64 subtract(Vec2I64 op1, Vec2I64 op2) {
            return of(
                    op1.x() - op2.x(),
                    op1.y() - op2.y()
            );
        }
    }
}
