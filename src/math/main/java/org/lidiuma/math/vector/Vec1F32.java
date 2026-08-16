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
import org.lidiuma.math.api.traits.vector.FloatingVector1Ops;
import org.lidiuma.math.api.tuple.UnaryTuple1;
import org.lidiuma.math.api.vector.Vector1;
import org.lidiuma.math.numerics.FloatNumeric;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.FieldAlias;
import org.lidiuma.math.processor.NamedAlias;
import static org.lidiuma.math.internal.AnnotationConst.*;

@FactoryAlias(methodName = VEC1_FACTORY, outputClass = VECTOR_OUT)
public value record Vec1F32(@Override @NullRestricted Float x) implements Vector1<Float> {

    @FieldAlias(outputClass = VECTOR_OUT)
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized vector from a generic vector.
    @NamedAlias(methodName = VEC1_FACTORY + F32)
    public Vec1F32(UnaryTuple1<Float> vec) {
        this(vec.x());
    }

    public static final class Ops implements FloatingVector1Ops<Vec1F32, Float> {

        @Override
        @AliasExclude
        public Vec1F32 of(Float x) {
            return new Vec1F32(x);
        }

        @Override
        @NamedAlias(methodName = ZERO_FACTORY + UPPER_VEC1_FACTORY + F32)
        public Vec1F32 zero() {
            return FloatingVector1Ops.super.zero();
        }

        @Override
        @NamedAlias(methodName = ONE_FACTORY + UPPER_VEC1_FACTORY + F32)
        public Vec1F32 one() {
            return FloatingVector1Ops.super.one();
        }

        @Override
        @AliasExclude
        public FloatNumeric scalarOps() {
            return FloatNumeric.OPS;
        }
    }
}
