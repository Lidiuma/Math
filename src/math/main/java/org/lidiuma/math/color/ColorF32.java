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

package org.lidiuma.math.color;

import jdk.internal.vm.annotation.NullRestricted;
import org.lidiuma.math.api.color.Color;
import org.lidiuma.math.api.traits.color.ColorOps;
import org.lidiuma.math.api.tuple.UnaryTuple4;
import org.lidiuma.math.processor.Alias;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.processor.NamedAlias;
import org.lidiuma.math.vector.Vec4F32;
import java.util.function.UnaryOperator;
import static org.lidiuma.math.internal.AnnotationConst.*;

@FactoryAlias(methodName = COLOR_FACTORY, outputClass = COLOR_OUT)
public value record ColorF32(
        @Override @NullRestricted Float red,
        @Override @NullRestricted Float green,
        @Override @NullRestricted Float blue,
        @Override @NullRestricted Float alpha
) implements Color<Float> {

    @Alias(outputClass = COLOR_OUT)
    public static final Ops OPS = new ColorF32.Ops();

    @NamedAlias(methodName = COLOR_FACTORY + F32)
    public ColorF32(UnaryTuple4<Float> tuple) {
        this(tuple.x(), tuple.y(), tuple.z(), tuple.w());
    }

    public static final class Ops implements ColorOps<ColorF32, Float> {

        // I use Vec4.Ops to avoid re-doing the math, which is error-prone.

        private Vec4F32 v(ColorF32 c) {
            return new Vec4F32(c.red(), c.green(), c.blue(), c.alpha());
        }

        private ColorF32 c(Vec4F32 v) {
            return new ColorF32(v.x(), v.y(), v.z(), v.w());
        }

        @Override
        public ColorF32 clamp(ColorF32 value, ColorF32 min, ColorF32 max) {
            return c(Vec4F32.OPS.clamp(v(value), v(min), v(max)));
        }

        @Override
        public ColorF32 interpolate(ColorF32 start, ColorF32 end, Float alpha, UnaryOperator<Float> easing) {
            return c(Vec4F32.OPS.interpolate(v(start), v(end), alpha, easing));
        }
    }
}
