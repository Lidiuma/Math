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

import org.lidiuma.math.api.traits.vector.FloatingVector1Ops;
import org.lidiuma.math.api.tuple.UnaryTuple1;
import org.lidiuma.math.api.vector.Vector1;
import org.lidiuma.math.numerics.DoubleNumeric;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.processor.NamedAlias;
import java.util.function.UnaryOperator;
import static org.lidiuma.math.internal.AnnotationConst.*;

@FactoryAlias(methodName = VEC1_FACTORY, outputClass = VECTOR_OUT)
public record Vec1F64(@Override Double x) implements Vector1<Double> {

    @FieldAlias(outputClass = VECTOR_OUT)
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized vector from a generic tuple.
    @AliasExclude // This method can be a performance sink if used inappropriately, so I exclude it from the alias.
    public Vec1F64(UnaryTuple1<Double> vec) {
        this(vec.x());
    }

    public static final class Ops implements FloatingVector1Ops<Vec1F64, Double> {

        private Ops() {}

        @Override
        @AliasExclude
        public Vec1F64 of(Double x) {
            return new Vec1F64(x);
        }

        @Override
        @NamedAlias(methodName = ZERO_FACTORY + UPPER_VEC1_FACTORY + F64)
        public Vec1F64 zero() {
            return of(0d);
        }

        @Override
        @NamedAlias(methodName = ONE_FACTORY + UPPER_VEC1_FACTORY + F64)
        public Vec1F64 one() {
            return of(1d);
        }

        @Override
        @AliasExclude
        public DoubleNumeric scalarOps() {
            return DoubleNumeric.OPS;
        }

        /*
        Handwritten to remove GC collections when used polymorphically (different generic parameters).
        Speed is more or less the same, but without a rare case of the JIT failing giving x10 less performance.
        This unfortunately creates code duplication, and I'm sure some methods are fine as-is,
        but writing them anyway is faster than making sure with benchmarking.
        */

        @Override
        public Vec1F64 sqrt(Vec1F64 operand) {
            return of(Math.sqrt(operand.x()));
        }

        @Override
        public Vec1F64 ceil(Vec1F64 operand) {
            return of(Math.ceil(operand.x()));
        }

        @Override
        public Vec1F64 floor(Vec1F64 operand) {
            return of(Math.floor(operand.x()));
        }

        @Override
        public boolean epsilonEquals(Vec1F64 v1, Vec1F64 v2, Double epsilon) {
            return abs(subtract(v1, v2)).x() <= epsilon;
        }

        @Override
        public Vec1F64 signum(Vec1F64 vector) {
            return of(Math.signum(vector.x()));
        }

        @Override
        public Double distance(Vec1F64 v1, Vec1F64 v2) {
            return abs(subtract(v1, v2)).x();
        }

        @Override
        public Double length(Vec1F64 vector) {
            return abs(vector).x();
        }

        @Override
        public Vec1F64 withLength(Vec1F64 vector, Double length) {
            return of(length);
        }

        @Override
        public Vec1F64 withLimit(Vec1F64 vector, Double limit) {
            final var length = length(vector);
            if (length <= limit) return vector;
            return of(limit);
        }

        @Override
        public Vec1F64 normalize(Vec1F64 vector) {
            return withLength(vector, 1d);
        }

        @Override
        public Vec1F64 normalizeOrElse(Vec1F64 vector, Double epsilon, Vec1F64 fallback) {
            if (epsilonEquals(vector, zero(), epsilon)) return fallback;
            return normalize(vector);
        }

        @Override
        public Vec1F64 abs(Vec1F64 vector) {
            return of(Math.abs(vector.x()));
        }

        @Override
        public Vec1F64 interpolate(Vec1F64 start, Vec1F64 end, Double alpha, UnaryOperator<Double> easing) {
            final var eased = easing.apply(alpha);
            final var invAlpha = 1d - eased;
            final var invStart = multiply(start, invAlpha);
            final var invEnd = multiply(end, eased);
            return add(invStart, invEnd);
        }

        @Override
        public Vec1F64 lerp(Vec1F64 start, Vec1F64 end, Double alpha) {
            return interpolate(start, end, alpha, UnaryOperator.identity());
        }

        @Override
        public Double sum(Vec1F64 vector) {
            return vector.x();
        }

        @Override
        public Vec1F64 multiply(Vec1F64 vector, Double scalar) {
            return multiply(vector, of(scalar));
        }

        @Override
        public Vec1F64 clamp(Vec1F64 vector, Double min, Double max) {
            return clamp(vector, of(min), of(max));
        }

        @Override
        public Vec1F64 clamp(Vec1F64 value, Vec1F64 min, Vec1F64 max) {
            return of(Math.clamp(value.x(), min.x(), max.x()));
        }

        @Override
        public Vec1F64 add(Vec1F64 op1, Vec1F64 op2) {
            return of(op1.x() + op2.x());
        }

        @Override
        public Vec1F64 multiply(Vec1F64 op1, Vec1F64 op2) {
            return of(op1.x() * op2.x());
        }

        @Override
        public Vec1F64 divide(Vec1F64 op1, Vec1F64 op2) {
            return of(op1.x() / op2.x());
        }

        @Override
        public Vec1F64 remainder(Vec1F64 op1, Vec1F64 op2) {
            return of(op1.x() % op2.x());
        }

        @Override
        public Vec1F64 negated(Vec1F64 operand) {
            return of(-operand.x());
        }

        @Override
        public Double distanceSquared(Vec1F64 a, Vec1F64 b) {
            final var sub = subtract(a, b);
            return sum(multiply(sub, sub));
        }

        @Override
        public Double lengthSquared(Vec1F64 vector) {
            return dot(vector, vector);
        }

        @Override
        public Double dot(Vec1F64 v1, Vec1F64 v2) {
            return sum(multiply(v1, v2));
        }

        @Override
        public Vec1F64 subtract(Vec1F64 op1, Vec1F64 op2) {
            return of(op1.x() - op2.x());
        }
    }
}
