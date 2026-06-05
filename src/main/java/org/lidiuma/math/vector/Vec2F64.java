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
import org.lidiuma.math.api.vector.Vector2;

public value record Vec2F64(
        @Override @NullRestricted Double x,
        @Override @NullRestricted Double y
) implements Vector2<Double> {

    public Vec2F64(Vector2<Double> vec) {
        this(vec.x(), vec.y());
    }

    @Override
    public Vec1F64 withoutY() {
        return new Vec1F64(x());
    }
}
