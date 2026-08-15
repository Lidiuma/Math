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
import org.lidiuma.math.api.vector.Vector2;
import org.lidiuma.math.numerics.DoubleNumeric;
import org.lidiuma.math.processor.Alias;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.NamedAlias;
import org.lidiuma.math.rotation.AngleF64;
import static org.lidiuma.math.internal.AnnotationConst.*;

@FactoryAlias(methodName = VEC2_FACTORY, outputClass = VECTOR_OUT)
public value record Vec2F64(
        @Override @NullRestricted Double x,
        @Override @NullRestricted Double y
) implements Vector2<Double> {

    @Alias(outputClass = VECTOR_OUT)
    public static final Ops OPS = new Ops();

    /// A constructor creating a specialized vector from a generic vector.
    @NamedAlias(methodName = VEC2_FACTORY + F64)
    public Vec2F64(Vector2<Double> vec) {
        this(vec.x(), vec.y());
    }

    public static final class Ops implements FloatingVector2Ops<Vec2F64, AngleF64, Double> {

        @Override
        @AliasExclude
        public Vec2F64 of(Double x, Double y) {
            return new Vec2F64(x, y);
        }

        @Override
        public AngleF64 angle(Vec2F64 v1, Vec2F64 v2) {
            final double dot = dot(v1, v2);
            final double length1 = lengthSquared(v1);
            final double length2 = lengthSquared(v2);
            final double theta = (dot / Math.sqrt(length1 * length2));
            return AngleF64.radians(Math.acos(theta));
        }

        @Override
        @AliasExclude
        public DoubleNumeric scalarOps() {
            return DoubleNumeric.OPS;
        }

        @Override
        @AliasExclude
        public Vec2F64 zero() {
            return FloatingVector2Ops.super.zero();
        }

        @Override
        @AliasExclude
        public Vec2F64 one() {
            return FloatingVector2Ops.super.one();
        }
    }
}
