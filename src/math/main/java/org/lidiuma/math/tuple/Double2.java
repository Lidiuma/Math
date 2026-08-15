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
import static org.lidiuma.math.internal.AnnotationConst.DOUBLE2;
import static org.lidiuma.math.internal.AnnotationConst.TUPLES_OUT;

@LooselyConsistentValue
@FactoryAlias(methodName = DOUBLE2, outputClass = TUPLES_OUT)
public value record Double2(
        @Override @NullRestricted Double x,
        @Override @NullRestricted Double y
) implements UnaryTuple2<Double> {

    public Double2(UnaryTuple1<Double> tuple, double y) {
        this(tuple.x(), y);
    }

    public Double2(UnaryTuple2<Double> tuple) {
        this(tuple.x(), tuple.y());
    }

    public Double2(UnaryTuple3<Double> tuple) {
        this(tuple.x(), tuple.y());
    }

    public Double2(UnaryTuple4<Double> tuple) {
        this(tuple.x(), tuple.y());
    }
}
