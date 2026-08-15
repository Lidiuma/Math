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
import org.lidiuma.math.processor.FactoryAlias;
import static org.lidiuma.math.internal.AnnotationConst.INT1;
import static org.lidiuma.math.internal.AnnotationConst.TUPLES_OUT;

@LooselyConsistentValue
@FactoryAlias(methodName = INT1, outputClass = TUPLES_OUT)
public value record Integer1(
        @Override @NullRestricted Integer x
) implements UnaryTuple1<Integer> {

    public Integer1(Integer2 integer2) {
        this(integer2.x());
    }

    public Integer1(Integer3 integer3) {
        this(integer3.x());
    }

    public Integer1(Integer4 integer4) {
        this(integer4.x());
    }
}

