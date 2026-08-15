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
import static org.lidiuma.math.internal.AnnotationConst.LONG1;
import static org.lidiuma.math.internal.AnnotationConst.TUPLES_OUT;

@LooselyConsistentValue
@FactoryAlias(methodName = LONG1, outputClass = TUPLES_OUT)
public value record Long1(
        @Override @NullRestricted Long x
) implements UnaryTuple1<Long> {

    public Long1(Long2 long2) {
        this(long2.x());
    }

    public Long1(Long3 long3) {
        this(long3.x());
    }

    public Long1(Long4 long4) {
        this(long4.x());
    }
}