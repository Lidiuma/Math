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
import org.lidiuma.math.api.point.Point4;
import org.lidiuma.math.api.traits.point.Point4Ops;
import org.lidiuma.math.processor.Alias;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.vector.Vec4I32;
import static org.lidiuma.math.internal.AnnotationConst.POINT4_FACTORY;
import static org.lidiuma.math.internal.AnnotationConst.POINT_OUT;

@LooselyConsistentValue
@FactoryAlias(methodName = POINT4_FACTORY, outputClass = POINT_OUT)
public value record Point4I32(
        @NullRestricted Integer x,
        @NullRestricted Integer y,
        @NullRestricted Integer z,
        @NullRestricted Integer w
) implements Point4<Integer> {

    @Alias(outputClass = POINT_OUT)
    public static final Ops OPS = new Ops();

    public static final class Ops implements Point4Ops<Point4I32, Vec4I32, Integer> {

        @Override
        @AliasExclude
        public Point4I32 of(Integer x, Integer y, Integer z, Integer w) {
            return new Point4I32(x, y, z, w);
        }

        @Override
        @AliasExclude
        public Vec4I32.Ops vectorOps() {
            return Vec4I32.OPS;
        }
    }
}
