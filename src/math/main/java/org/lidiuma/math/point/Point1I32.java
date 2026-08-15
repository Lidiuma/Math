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
import org.lidiuma.math.api.point.Point1;
import org.lidiuma.math.api.traits.point.Point1Ops;
import org.lidiuma.math.processor.Alias;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.vector.Vec1I32;
import static org.lidiuma.math.internal.AnnotationConst.POINT1_FACTORY;
import static org.lidiuma.math.internal.AnnotationConst.POINT_OUT;

@LooselyConsistentValue
@FactoryAlias(methodName = POINT1_FACTORY, outputClass = POINT_OUT)
public value record Point1I32(@NullRestricted Integer x) implements Point1<Integer> {

    @Alias(outputClass = POINT_OUT)
    public static final Ops OPS = new Ops();

    public static final class Ops implements Point1Ops<Point1I32, Vec1I32, Integer> {

        @Override
        @AliasExclude
        public Point1I32 of(Integer x) {
            return new Point1I32(x);
        }

        @Override
        @AliasExclude
        public Vec1I32.Ops vectorOps() {
            return Vec1I32.OPS;
        }
    }
}
