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
import org.lidiuma.math.api.rotation.Angle;
import org.lidiuma.math.api.tuple.UnaryTuple3;
import org.lidiuma.math.api.vector.Vector3;
import org.lidiuma.math.tuple.Tuples;

import java.util.function.UnaryOperator;
import static org.lidiuma.math.FloatingUtil.EPSILON_F32;

public value record Vector3F(
        @Override @NullRestricted Float x,
        @Override @NullRestricted Float y,
        @Override @NullRestricted Float z
) implements Vector3<Float> {

    public Vector3F(Vector3<Float> vec) {
        this(vec.x(), vec.y(), vec.z());
    }

    @Override
    public Vector2F withoutZ() {
        return new Vector2F(x(), y());
    }
}
