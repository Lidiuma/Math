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

import jdk.internal.vm.annotation.LooselyConsistentValue;
import jdk.internal.vm.annotation.NullRestricted;
import org.lidiuma.math.api.tuple.UnaryTuple1;
import static org.lidiuma.math.tuple.Tuples.*;

@LooselyConsistentValue
public value record Float1(
        @Override @NullRestricted Float x
) implements UnaryTuple1<Float> {

    @Override
    public boolean equals(UnaryTuple1<Float> other, Float epsilon) {
        return epsilonEquals(x, other.x(), epsilon);
    }

    @Override
    public boolean componentEquals(Float value, Float epsilon) {
        return equals(float1(value), epsilon);
    }
}
