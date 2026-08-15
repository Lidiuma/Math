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
import org.lidiuma.math.api.tuple.UnaryTuple2;
import org.lidiuma.math.api.tuple.UnaryTuple3;
import org.lidiuma.math.api.tuple.UnaryTuple4;
import org.lidiuma.math.processor.FactoryAlias;
import static org.lidiuma.math.internal.AnnotationConst.LONG4;
import static org.lidiuma.math.internal.AnnotationConst.TUPLES_OUT;

@LooselyConsistentValue
@FactoryAlias(methodName = LONG4, outputClass = TUPLES_OUT)
public value record Long4(
        @Override @NullRestricted Long x,
        @Override @NullRestricted Long y,
        @Override @NullRestricted Long z,
        @Override @NullRestricted Long w
) implements UnaryTuple4<Long> {

    public Long4(UnaryTuple1<Long> tuple, long y, long z, long w) {
        this(tuple.x(), y, z, w);
    }

    public Long4(UnaryTuple2<Long> tuple, long z, long w) {
        this(tuple.x(), tuple.y(), z, w);
    }

    public Long4(UnaryTuple3<Long> tuple, long w) {
        this(tuple.x(), tuple.y(), tuple.z(), w);
    }

    public Long4(UnaryTuple4<Long> tuple) {
        this(tuple.x(), tuple.y(), tuple.z(), tuple.w());
    }
}