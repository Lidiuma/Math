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

package org.lidiuma.math.point;

import jdk.internal.vm.annotation.LooselyConsistentValue;
import jdk.internal.vm.annotation.NullRestricted;
import org.lidiuma.math.api.geometry.point.Point3;

@LooselyConsistentValue
public value record Point3I64(
        @NullRestricted Long x,
        @NullRestricted Long y,
        @NullRestricted Long z
) implements Point3<Long> {

    @Override
    public Point2I64 withoutZ() {
        return new Point2I64(x(), y());
    }
}
