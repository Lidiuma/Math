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

import org.lidiuma.math.api.vector.FloatingVector1Ops;
import org.lidiuma.math.numerics.FloatNumeric;
import org.lidiuma.math.rotation.AngleF;

public value class Vector1FOps implements FloatingVector1Ops<Vector1F, AngleF, Float> {

    public static final Vector1FOps WITNESS = new Vector1FOps();

    private Vector1FOps() {}

    @Override
    public Vector1F of(Float x) {
        return new Vector1F(x);
    }

    @Override
    public AngleF angle(Vector1F v1, Vector1F v2) {
        return AngleF.radians(0f);
    }

    @Override
    public FloatNumeric scalarWitness() {
        return FloatNumeric.WITNESS;
    }
}
