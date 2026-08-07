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
import org.lidiuma.math.api.vector.Vector1;
import org.lidiuma.math.numerics.DoubleNumeric;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.Alias;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.rotation.AngleF64;

@FactoryAlias(methodName = "vec1", outputClass = "Vectors")
public value record Vec1F64(@Override @NullRestricted Double x) implements Vector1<Double> {

    @Alias(outputClass = "Vectors")
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized vector from a generic vector.
    @AliasExclude
    public Vec1F64(Vector1<Double> vec) {
        this(vec.x());
    }

    public static final class Ops implements FloatingVector1Ops<Vec1F64, AngleF64, Double> {

        @Override
        @AliasExclude
        public Vec1F64 of(Double x) {
            return new Vec1F64(x);
        }

        @Override
        public AngleF64 angle(Vec1F64 v1, Vec1F64 v2) {
            return AngleF64.radians(0f);
        }

        @Override
        @AliasExclude
        public DoubleNumeric scalarOps() {
            return DoubleNumeric.OPS;
        }

        @Override
        @AliasExclude
        public Vec1F64 zero() {
            return FloatingVector1Ops.super.zero();
        }

        @Override
        @AliasExclude
        public Vec1F64 one() {
            return FloatingVector1Ops.super.one();
        }
    }
}
