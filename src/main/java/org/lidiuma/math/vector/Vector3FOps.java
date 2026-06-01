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

import org.lidiuma.math.api.vector.FloatingVector3Ops;
import org.lidiuma.math.numerics.FloatNumeric;
import org.lidiuma.math.rotation.AngleF;

public value class Vector3FOps implements FloatingVector3Ops<Vector3F, AngleF, Float> {

    public static final Vector3FOps WITNESS = new Vector3FOps();

    private Vector3FOps() {}

    @Override
    public Vector3F of(Float x, Float y, Float z) {
        return new Vector3F(x, y, z);
    }

    @Override
    public AngleF angle(Vector3F v1, Vector3F v2) {
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
}
