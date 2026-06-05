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
import org.lidiuma.math.rotation.AngleF;

public value record Vector2F(
        @Override @NullRestricted Float x,
        @Override @NullRestricted Float y
) implements Vector2<Float> {

    public static final FloatingVector2Ops<Vector2F, AngleF, Float> WITNESS = new FloatingVector2Ops<>() {

        @Override
        public Vector2F of(Float x, Float y) {
            return new Vector2F(x, y);
        }

        @Override
        public AngleF angle(Vector2F v1, Vector2F v2) {
            final var dot = dot(v1, v2);
            final float length1 = lengthSquared(v1);
            final float length2 = lengthSquared(v2);
            final float theta = (float) (dot / Math.sqrt(length1 * length2));
            return AngleF.radians((float) Math.acos(theta));
        }

        @Override
        public FloatNumeric scalarWitness() {
            return FloatNumeric.WITNESS;
        }
    };

    public Vector2F(Vector2<Float> vec) {
        this(vec.x(), vec.y());
    }

    @Override
    public Vector1F withoutY() {
        return new Vector1F(x());
    }
}
