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

package org.lidiuma.math.shape.rectangle;

import jdk.internal.vm.annotation.LooselyConsistentValue;
import jdk.internal.vm.annotation.NullRestricted;
import org.lidiuma.math.api.shapes.rectangle.Rectangle4;
import org.lidiuma.math.vector.Vec4F32;

@LooselyConsistentValue
public value record Rect4F32(@NullRestricted Vec4F32 dimensions) implements Rectangle4<Float> {

    public Rect4F32(float width, float height, float length, float depth) {
        this(new Vec4F32(width, height, length, depth));
    }
}
