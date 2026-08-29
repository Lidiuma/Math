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

import org.lidiuma.math.api.traits.vector.Vector1Ops;
import org.lidiuma.math.api.tuple.UnaryTuple1;
import org.lidiuma.math.api.vector.Vector1;
import org.lidiuma.math.numerics.LongNumeric;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.processor.NamedAlias;
import static org.lidiuma.math.internal.AnnotationConst.*;

@FactoryAlias(methodName = VEC1_FACTORY, outputClass = VECTOR_OUT)
public value record Vec1I64(@Override Long x) implements Vector1<Long> {

    @FieldAlias(outputClass = VECTOR_OUT)
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized vector from a generic tuple.
    @AliasExclude // This method can be a performance sink if used inappropriately, so I exclude it from the alias.
    public Vec1I64(UnaryTuple1<Long> vec) {
        this(vec.x());
    }

    public static final value class Ops implements Vector1Ops<Vec1I64, Long> {

        private Ops() {}

        @Override
        @AliasExclude
        public Vec1I64 of(Long x) {
            return new Vec1I64(x);
        }

        @Override
        public Vec1I64 signum(Vec1I64 vector) {
            return of((long) Long.signum(vector.x()));
        }

        @Override
        @NamedAlias(methodName = ZERO_FACTORY + UPPER_VEC1_FACTORY + I64)
        public Vec1I64 zero() {
            return of(0L);
        }

        @Override
        @NamedAlias(methodName = ONE_FACTORY + UPPER_VEC1_FACTORY + I64)
        public Vec1I64 one() {
            return of(1L);
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
        public Long distance(Vec1I64 v1, Vec1I64 v2) {
            return abs(subtract(v1, v2)).x();
        }

        @Override
        public Long length(Vec1I64 vector) {
            return abs(vector).x();
        }

        @Override
        public Vec1I64 abs(Vec1I64 vector) {
            return of(Math.abs(vector.x()));
        }

        @Override
        public Long sum(Vec1I64 vector) {
            return vector.x();
        }

        @Override
        public Vec1I64 multiply(Vec1I64 vector, Long scalar) {
            return multiply(vector, of(scalar));
        }

        @Override
        public Vec1I64 clamp(Vec1I64 vector, Long min, Long max) {
            return clamp(vector, of(min), of(max));
        }

        @Override
        public Vec1I64 clamp(Vec1I64 value, Vec1I64 min, Vec1I64 max) {
            return of(Math.clamp(value.x(), min.x(), max.x()));
        }

        @Override
        public Vec1I64 add(Vec1I64 op1, Vec1I64 op2) {
            return of(op1.x() + op2.x());
        }

        @Override
        public Vec1I64 multiply(Vec1I64 op1, Vec1I64 op2) {
            return of(op1.x() * op2.x());
        }

        @Override
        public Vec1I64 divide(Vec1I64 op1, Vec1I64 op2) {
            return of(op1.x() / op2.x());
        }

        @Override
        public Vec1I64 remainder(Vec1I64 op1, Vec1I64 op2) {
            return of(op1.x() % op2.x());
        }

        @Override
        public Vec1I64 negated(Vec1I64 operand) {
            return of(-operand.x());
        }

        @Override
        public Long distanceSquared(Vec1I64 a, Vec1I64 b) {
            final var sub = subtract(a, b);
            return sum(multiply(sub, sub));
        }

        @Override
        public Long lengthSquared(Vec1I64 vector) {
            return dot(vector, vector);
        }

        @Override
        public Long dot(Vec1I64 v1, Vec1I64 v2) {
            return sum(multiply(v1, v2));
        }

        @Override
        public Vec1I64 subtract(Vec1I64 op1, Vec1I64 op2) {
            return of(op1.x() - op2.x());
        }
    }
}
