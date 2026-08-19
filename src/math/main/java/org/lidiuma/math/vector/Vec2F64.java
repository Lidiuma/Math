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
            return FloatingVector2Ops.super.zero();
        }

        @Override
        @NamedAlias(methodName = ONE_FACTORY + UPPER_VEC2_FACTORY + F64)
        public Vec2F64 one() {
            return FloatingVector2Ops.super.one();
        }

        @Override
        @AliasExclude
        public DoubleNumeric scalarOps() {
            return DoubleNumeric.OPS;
        }
    }
}
