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
import static org.lidiuma.math.internal.AnnotationConst.INT2;
import static org.lidiuma.math.internal.AnnotationConst.TUPLES_OUT;

@LooselyConsistentValue
@FactoryAlias(methodName = INT2, outputClass = TUPLES_OUT)
public value record Integer2(
        @Override @NullRestricted Integer x,
        @Override @NullRestricted Integer y
) implements UnaryTuple2<Integer> {

    public Integer2(UnaryTuple1<Integer> tuple, int y) {
        this(tuple.x(), y);
    }

    public Integer2(UnaryTuple2<Integer> tuple) {
        this(tuple.x(), tuple.y());
    }

    public Integer2(UnaryTuple3<Integer> tuple) {
        this(tuple.x(), tuple.y());
    }

    public Integer2(UnaryTuple4<Integer> tuple) {
        this(tuple.x(), tuple.y());
    }
}
