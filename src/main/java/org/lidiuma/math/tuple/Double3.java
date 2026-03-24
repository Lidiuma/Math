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
import org.lidiuma.math.api.tuple.UnaryTuple3;
import static org.lidiuma.math.tuple.Tuples.*;

public value record Double3(
        @Override @NullRestricted Double x,
        @Override @NullRestricted Double y,
        @Override @NullRestricted Double z
) implements UnaryTuple3<Double> {

    public Double3(Double2 double2, double z) {
        this(double2.x(), double2.y(), z);
    }

    @Override
    public Double2 withoutZ() {
        return double2(x, y);
    }

    @Override
    public boolean equals(UnaryTuple3<Double> other, Double epsilon) {
        return epsilonEquals(x, other.x(), epsilon) &&
               epsilonEquals(y, other.y(), epsilon) &&
               epsilonEquals(z, other.z(), epsilon);
    }

    @Override
    public boolean componentEquals(Double value, Double epsilon) {
        return equals(double3(value), epsilon);
    }
}
