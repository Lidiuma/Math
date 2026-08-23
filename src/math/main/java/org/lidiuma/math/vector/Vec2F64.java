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

import jdk.internal.vm.annotation.NullRestricted;
import org.lidiuma.math.api.traits.vector.FloatingVector2Ops;
import org.lidiuma.math.api.tuple.UnaryTuple2;
import org.lidiuma.math.api.vector.Vector2;
import org.lidiuma.math.numerics.DoubleNumeric;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.processor.NamedAlias;
import java.util.function.UnaryOperator;
import static org.lidiuma.math.internal.AnnotationConst.*;

@FactoryAlias(methodName = VEC2_FACTORY, outputClass = VECTOR_OUT)
public value record Vec2F64(
        @Override @NullRestricted Double x,
        @Override @NullRestricted Double y
) implements Vector2<Double> {

    @FieldAlias(outputClass = VECTOR_OUT)
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized vector from a generic vector.
    @NamedAlias(methodName = VEC2_FACTORY + F64)
    public Vec2F64(UnaryTuple2<Double> vec) {
        this(vec.x(), vec.y());
    }

    public static final value class Ops implements FloatingVector2Ops<Vec2F64, Double> {

        private Ops() {}

        @Override
        @AliasExclude
        public Vec2F64 of(Double x, Double y) {
            return new Vec2F64(x, y);
        }

        @Override
        @NamedAlias(methodName = ZERO_FACTORY + UPPER_VEC2_FACTORY + F64)
        public Vec2F64 zero() {
            return of(0d, 0d);
        }

        @Override
        @NamedAlias(methodName = ONE_FACTORY + UPPER_VEC2_FACTORY + F64)
        public Vec2F64 one() {
            return of(1d, 1d);
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
        public Vec2F64 sqrt(Vec2F64 operand) {
            return of(
                    Math.sqrt(operand.x()),
                    Math.sqrt(operand.y())
            );
        }

        @Override
        public Vec2F64 ceil(Vec2F64 operand) {
            return of(
                    Math.ceil(operand.x()),
                    Math.ceil(operand.y())
            );
        }

        @Override
        public Vec2F64 floor(Vec2F64 operand) {
            return of(
                    Math.floor(operand.x()),
                    Math.floor(operand.y())
            );
        }

        @Override
        public boolean epsilonEquals(Vec2F64 v1, Vec2F64 v2, Double epsilon) {
            final var vec = abs(subtract(v1, v2));
            if (vec.x() > epsilon) return false;
            return vec.y() <= epsilon;
        }

        @Override
        public Vec2F64 signum(Vec2F64 vector) {
            return of(
                    Math.signum(vector.x()),
                    Math.signum(vector.y())
            );
        }

        @Override
        public Double distance(Vec2F64 v1, Vec2F64 v2) {
            return Math.sqrt(distanceSquared(v1, v2));
        }

        @Override
        public Double length(Vec2F64 vector) {
            return Math.sqrt(lengthSquared(vector));
        }

        @Override
        public Vec2F64 withLength(Vec2F64 vector, Double length) {
            return withMagnitude(vector, length, length(vector));
        }

        @Override
        public Vec2F64 withLimit(Vec2F64 vector, Double limit) {
            final var length = length(vector);
            if (length <= limit) return vector;
            return withMagnitude(vector, limit, length);
        }

        @Override
        public Vec2F64 normalize(Vec2F64 vector) {
            return withLength(vector, 1d);
        }

        @Override
        public Vec2F64 normalizeOrElse(Vec2F64 vector, Double epsilon, Vec2F64 fallback) {
            if (epsilonEquals(vector, zero(), epsilon)) return fallback;
            return normalize(vector);
        }

        @Override
        public Vec2F64 abs(Vec2F64 vector) {
            return of(
                    Math.abs(vector.x()),
                    Math.abs(vector.y())
            );
        }

        @Override
        public Vec2F64 interpolate(Vec2F64 start, Vec2F64 end, Double alpha, UnaryOperator<Double> easing) {
            final var eased = easing.apply(alpha);
            final var invAlpha = 1d - eased;
            final var invStart = multiply(start, invAlpha);
            final var invEnd = multiply(end, eased);
            return add(invStart, invEnd);
        }

        @Override
        public Double cross(Vec2F64 v1, Vec2F64 v2) {
            return v1.x() * v2.y() - v1.y() * v2.x();
        }

        @Override
        public Double sum(Vec2F64 vector) {
            return vector.x() + vector.y();
        }

        @Override
        public Vec2F64 multiply(Vec2F64 vector, Double scalar) {
            return multiply(vector, of(scalar, scalar));
        }

        @Override
        public Vec2F64 clamp(Vec2F64 vector, Double min, Double max) {
            return clamp(vector, of(min, min), of(max, max));
        }

        @Override
        public Vec2F64 clamp(Vec2F64 value, Vec2F64 min, Vec2F64 max) {
            return of(
                    Math.clamp(value.x(), min.x(), max.x()),
                    Math.clamp(value.y(), min.y(), max.y())
            );
        }

        @Override
        public Vec2F64 add(Vec2F64 op1, Vec2F64 op2) {
            return of(
                    op1.x() + op2.x(),
                    op1.y() + op2.y()
            );
        }

        @Override
        public Vec2F64 multiply(Vec2F64 op1, Vec2F64 op2) {
            return of(
                    op1.x() * op2.x(),
                    op1.y() * op2.y()
            );
        }

        @Override
        public Vec2F64 divide(Vec2F64 op1, Vec2F64 op2) {
            return of(
                    op1.x() / op2.x(),
                    op1.y() / op2.y()
            );
        }

        @Override
        public Vec2F64 remainder(Vec2F64 op1, Vec2F64 op2) {
            return of(
                    op1.x() % op2.x(),
                    op1.y() % op2.y()
            );
        }

        @Override
        public Vec2F64 negated(Vec2F64 operand) {
            return of(
                    -operand.x(),
                    -operand.y()
            );
        }

        @Override
        public Double distanceSquared(Vec2F64 a, Vec2F64 b) {
            final var sub = subtract(a, b);
            return sum(multiply(sub, sub));
        }

        @Override
        public Double lengthSquared(Vec2F64 vector) {
            return dot(vector, vector);
        }

        @Override
        public Double dot(Vec2F64 v1, Vec2F64 v2) {
            return sum(multiply(v1, v2));
        }

        @Override
        public Vec2F64 subtract(Vec2F64 op1, Vec2F64 op2) {
            return of(
                    op1.x() - op2.x(),
                    op1.y() - op2.y()
            );
        }

        private Vec2F64 withMagnitude(Vec2F64 vector, Double wanted, Double current) {
            return multiply(vector, wanted / current);
        }
    }
}
