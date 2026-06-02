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

package org.lidiuma.math.rotation;

import jdk.internal.vm.annotation.NullRestricted;
import org.lidiuma.math.api.rotation.Quaternion;
import org.lidiuma.math.api.vector.Vector4;
import org.lidiuma.math.tuple.Float3;
import jdk.internal.vm.annotation.LooselyConsistentValue;

@LooselyConsistentValue
public value record QuaternionF(
        @Override @NullRestricted Float x,
        @Override @NullRestricted Float y,
        @Override @NullRestricted Float z,
        @Override @NullRestricted Float w
) implements Quaternion<Float> {

    public QuaternionF(Vector4<Float> v4) {
        this(v4.x(), v4.y(), v4.z(), v4.w());
    }

    @Override
    public Float3 withoutW() {
        return new Float3(x(), y(), z());
    }
}
