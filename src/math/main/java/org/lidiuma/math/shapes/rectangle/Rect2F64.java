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

package org.lidiuma.math.shapes.rectangle;

import jdk.internal.vm.annotation.LooselyConsistentValue;
import jdk.internal.vm.annotation.NullRestricted;
import org.lidiuma.math.api.shapes.rectangle.Rectangle2;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.vector.Vec2F64;
import static org.lidiuma.math.internal.AnnotationConst.RECT2_FACTORY;
import static org.lidiuma.math.internal.AnnotationConst.SHAPES_OUT;

@LooselyConsistentValue
@FactoryAlias(methodName = RECT2_FACTORY, outputClass = SHAPES_OUT, isPackageDefined = true)
public value record Rect2F64(@NullRestricted Vec2F64 dimensions) implements Rectangle2<Double> {

    public Rect2F64(double width, double height) {
        this(new Vec2F64(width, height));
    }
}
