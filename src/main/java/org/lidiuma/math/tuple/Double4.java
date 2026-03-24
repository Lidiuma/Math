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

package org.lidiuma.math.tuple;

import jdk.internal.vm.annotation.NullRestricted;
import org.lidiuma.math.api.tuple.UnaryTuple4;
import static org.lidiuma.math.tuple.Tuples.*;

public value record Double4(
        @Override @NullRestricted Double x,
        @Override @NullRestricted Double y,
        @Override @NullRestricted Double z,
        @Override @NullRestricted Double w
) implements UnaryTuple4<Double> {

    @Override
    public Double3 withoutW() {
        return double3(x, y, z);
    }

    @Override
    public boolean equals(UnaryTuple4<Double> other, Double epsilon) {
        return epsilonEquals(x, other.x(), epsilon) &&
               epsilonEquals(y, other.y(), epsilon) &&
               epsilonEquals(z, other.z(), epsilon) &&
               epsilonEquals(w, other.w(), epsilon);
    }

    @Override
    public boolean componentEquals(Double value, Double epsilon) {
        return equals(double4(value), epsilon);
    }
}
