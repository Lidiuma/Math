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
import org.lidiuma.math.numerics.FloatNumeric;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.Alias;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.rotation.AngleF32;

@FactoryAlias(methodName = "vec2", outputClass = "Vectors")
public value record Vec2F32(
        @Override @NullRestricted Float x,
        @Override @NullRestricted Float y
) implements Vector2<Float> {

    @Alias(outputClass = "Vectors")
    public static final Ops WITNESS = new Ops();

    @AliasExclude
    public Vec2F32(Vector2<Float> vec) {
        this(vec.x(), vec.y());
    }

    public static final class Ops implements FloatingVector2Ops<Vec2F32, AngleF32, Float> {

        @Override
        @AliasExclude
        public Vec2F32 of(Float x, Float y) {
            return new Vec2F32(x, y);
        }

        @Override
        public AngleF32 angle(Vec2F32 v1, Vec2F32 v2) {
            final float dot = dot(v1, v2);
            final float length1 = lengthSquared(v1);
            final float length2 = lengthSquared(v2);
            final float theta = (float) (dot / Math.sqrt(length1 * length2));
            return AngleF32.radians((float) Math.acos(theta));
        }

        @Override
        @AliasExclude
        public FloatNumeric scalarOps() {
            return FloatNumeric.WITNESS;
        }

        @Override
        @AliasExclude
        public Vec2F32 zero() {
            return FloatingVector2Ops.super.zero();
        }

        @Override
        @AliasExclude
        public Vec2F32 one() {
            return FloatingVector2Ops.super.one();
        }
    }
}
