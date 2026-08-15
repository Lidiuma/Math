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
import static org.lidiuma.math.internal.AnnotationConst.FLOAT4;
import static org.lidiuma.math.internal.AnnotationConst.TUPLES_OUT;

@LooselyConsistentValue
@FactoryAlias(methodName = FLOAT4, outputClass = TUPLES_OUT)
public value record Float4(
        @Override @NullRestricted Float x,
        @Override @NullRestricted Float y,
        @Override @NullRestricted Float z,
        @Override @NullRestricted Float w
) implements UnaryTuple4<Float> {

    public Float4(UnaryTuple1<Float> tuple, float y, float z, float w) {
        this(tuple.x(), y, z, w);
    }

    public Float4(UnaryTuple2<Float> tuple, float z, float w) {
        this(tuple.x(), tuple.y(), z, w);
    }

    public Float4(UnaryTuple3<Float> tuple, float w) {
        this(tuple.x(), tuple.y(), tuple.z(), w);
    }

    public Float4(UnaryTuple4<Float> tuple) {
        this(tuple.x(), tuple.y(), tuple.z(), tuple.w());
    }
}
