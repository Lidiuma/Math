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
import org.lidiuma.math.api.point.Point2;
import org.lidiuma.math.api.traits.point.Point2Ops;
import org.lidiuma.math.processor.Alias;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.vector.Vec2I32;

@LooselyConsistentValue
@FactoryAlias(methodName = "point2", outputClass = "Points")
public value record Point2I32(
        @NullRestricted Integer x,
        @NullRestricted Integer y
) implements Point2<Integer> {

    @Alias(outputClass = "Points")
    public static final Point2I32.Ops WITNESS = new Point2I32.Ops();

    public static final class Ops implements Point2Ops<Point2I32, Vec2I32, Integer> {

        @Override
        @AliasExclude
        public Point2I32 of(Integer x, Integer y) {
            return new Point2I32(x, y);
        }

        @Override
        @AliasExclude
        public Vec2I32.Ops vectorOps() {
            return Vec2I32.OPS;
        }
    }
}
