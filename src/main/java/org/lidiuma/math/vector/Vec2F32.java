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
import org.lidiuma.math.api.vector.FloatingVector2Ops;
import org.lidiuma.math.api.vector.Vector2;
import org.lidiuma.math.numerics.FloatNumeric;
import org.lidiuma.math.rotation.AngleF32;

public value record Vec2F32(
        @Override @NullRestricted Float x,
        @Override @NullRestricted Float y
) implements Vector2<Float> {

    public static final FloatingVector2Ops<Vec2F32, AngleF32, Float> WITNESS = new FloatingVector2Ops<>() {

        @Override
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
        public FloatNumeric scalarWitness() {
            return FloatNumeric.WITNESS;
        }
    };

    public Vec2F32(Vector2<Float> vec) {
        this(vec.x(), vec.y());
    }

    @Override
    public Vec1F32 withoutY() {
        return new Vec1F32(x());
    }
}
