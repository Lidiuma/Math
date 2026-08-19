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
            return Vector1Ops.super.zero();
        }

        @Override
        @NamedAlias(methodName = ONE_FACTORY + UPPER_VEC1_FACTORY + I32)
        public Vec1I32 one() {
            return Vector1Ops.super.one();
        }

        @Override
        @AliasExclude
        public IntegerNumeric scalarOps() {
            return IntegerNumeric.OPS;
        }
    }
}
