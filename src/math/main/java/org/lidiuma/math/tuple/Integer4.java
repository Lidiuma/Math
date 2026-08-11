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
import org.lidiuma.math.api.tuple.UnaryTuple4;
import org.lidiuma.math.processor.FactoryAlias;

@LooselyConsistentValue
@FactoryAlias(methodName = "int4", outputClass = "Tuples")
public value record Integer4(
        @Override @NullRestricted Integer x,
        @Override @NullRestricted Integer y,
        @Override @NullRestricted Integer z,
        @Override @NullRestricted Integer w
) implements UnaryTuple4<Integer> {

    public Integer4(Integer3 int3, int w) {
        this(int3.x(), int3.y(), int3.z(), w);
    }
}
