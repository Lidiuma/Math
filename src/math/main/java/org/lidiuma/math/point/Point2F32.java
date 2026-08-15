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
import org.lidiuma.math.api.traits.point.FloatingPoint2Ops;
import org.lidiuma.math.api.tuple.UnaryTuple2;
import org.lidiuma.math.processor.Alias;
import org.lidiuma.math.processor.AliasExclude;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.NamedAlias;
import org.lidiuma.math.vector.Vec2F32;
import static org.lidiuma.math.internal.AnnotationConst.*;

@LooselyConsistentValue
@FactoryAlias(methodName = POINT2_FACTORY, outputClass = POINT_OUT)
public value record Point2F32(
        @NullRestricted Float x,
        @NullRestricted Float y
) implements Point2<Float> {

    @Alias(outputClass = POINT_OUT)
    public static final Ops OPS = new Ops();

    @NamedAlias(methodName = POINT2_FACTORY + F32)
    public Point2F32(UnaryTuple2<Float> tuple) {
        this(tuple.x(), tuple.y());
    }

    public static final class Ops implements FloatingPoint2Ops<Point2F32, Vec2F32, Float> {

        @Override
        @AliasExclude
        public Point2F32 of(Float x, Float y) {
            return new Point2F32(x, y);
        }

        @Override
        @AliasExclude
        public Vec2F32.Ops vectorOps() {
            return Vec2F32.OPS;
        }
    }
}
