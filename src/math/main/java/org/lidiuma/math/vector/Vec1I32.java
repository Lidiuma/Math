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
import org.lidiuma.math.api.traits.vector.Vector1Ops;
import org.lidiuma.math.api.tuple.UnaryTuple1;
import org.lidiuma.math.api.vector.Vector1;
import org.lidiuma.math.numerics.IntegerNumeric;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.processor.NamedAlias;
import static org.lidiuma.math.internal.AnnotationConst.*;

@FactoryAlias(methodName = VEC1_FACTORY, outputClass = VECTOR_OUT)
public value record Vec1I32(@Override @NullRestricted Integer x) implements Vector1<Integer> {

    @FieldAlias(outputClass = VECTOR_OUT)
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized vector from a generic vector.
    @NamedAlias(methodName = VEC1_FACTORY + I32)
    public Vec1I32(UnaryTuple1<Integer> vec) {
        this(vec.x());
    }

    public static final value class Ops implements Vector1Ops<Vec1I32, Integer> {

        private Ops() {}

        @Override
        @AliasExclude
        public Vec1I32 of(Integer x) {
            return new Vec1I32(x);
        }

        @Override
        public Vec1I32 signum(Vec1I32 vector) {
            return of(Integer.signum(vector.x()));
        }

        @Override
        @NamedAlias(methodName = ZERO_FACTORY + UPPER_VEC1_FACTORY + I32)
        public Vec1I32 zero() {
            return of(0);
        }

        @Override
        @NamedAlias(methodName = ONE_FACTORY + UPPER_VEC1_FACTORY + I32)
        public Vec1I32 one() {
            return of(1);
        }

        @Override
        @AliasExclude
        public IntegerNumeric scalarOps() {
            return IntegerNumeric.OPS;
        }

        /*
        Handwritten to remove GC collections when used polymorphically (different generic parameters).
        Speed is more or less the same, but without a rare case of the JIT failing giving x10 less performance.
        This unfortunately creates code duplication, and I'm sure some methods are fine as-is,
        but writing them anyway is faster than making sure with benchmarking.
        */

        @Override
        public Integer distance(Vec1I32 v1, Vec1I32 v2) {
            return abs(subtract(v1, v2)).x();
        }

        @Override
        public Integer length(Vec1I32 vector) {
            return abs(vector).x();
        }

        @Override
        public Vec1I32 abs(Vec1I32 vector) {
            return of(Math.abs(vector.x()));
        }

        @Override
        public Integer sum(Vec1I32 vector) {
            return vector.x();
        }

        @Override
        public Vec1I32 multiply(Vec1I32 vector, Integer scalar) {
            return multiply(vector, of(scalar));
        }

        @Override
        public Vec1I32 clamp(Vec1I32 vector, Integer min, Integer max) {
            return clamp(vector, of(min), of(max));
        }

        @Override
        public Vec1I32 clamp(Vec1I32 value, Vec1I32 min, Vec1I32 max) {
            return of(Math.clamp(value.x(), min.x(), max.x()));
        }

        @Override
        public Vec1I32 add(Vec1I32 op1, Vec1I32 op2) {
            return of(op1.x() + op2.x());
        }

        @Override
        public Vec1I32 multiply(Vec1I32 op1, Vec1I32 op2) {
            return of(op1.x() * op2.x());
        }

        @Override
        public Vec1I32 divide(Vec1I32 op1, Vec1I32 op2) {
            return of(op1.x() / op2.x());
        }

        @Override
        public Vec1I32 remainder(Vec1I32 op1, Vec1I32 op2) {
            return of(op1.x() % op2.x());
        }

        @Override
        public Vec1I32 negated(Vec1I32 operand) {
            return of(-operand.x());
        }

        @Override
        public Integer distanceSquared(Vec1I32 a, Vec1I32 b) {
            final var sub = subtract(a, b);
            return sum(multiply(sub, sub));
        }

        @Override
        public Integer lengthSquared(Vec1I32 vector) {
            return dot(vector, vector);
        }

        @Override
        public Integer dot(Vec1I32 v1, Vec1I32 v2) {
            return sum(multiply(v1, v2));
        }

        @Override
        public Vec1I32 subtract(Vec1I32 op1, Vec1I32 op2) {
            return of(op1.x() - op2.x());
        }
    }
}
