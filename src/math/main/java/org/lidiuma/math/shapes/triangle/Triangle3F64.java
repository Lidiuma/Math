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

package org.lidiuma.math.shapes.triangle;

import jdk.internal.vm.annotation.LooselyConsistentValue;
import jdk.internal.vm.annotation.NullRestricted;
import org.lidiuma.math.api.shapes.triangle.Triangle3;
import org.lidiuma.math.processor.FactoryAlias;
import org.lidiuma.math.vector.Vec3F64;
import static org.lidiuma.math.internal.AnnotationConst.SHAPES_OUT;
import static org.lidiuma.math.internal.AnnotationConst.TRIANGLE3_FACTORY;

@LooselyConsistentValue
@FactoryAlias(methodName = TRIANGLE3_FACTORY, outputClass = SHAPES_OUT, isPackageDefined = true)
public value record Triangle3F64(@Override @NullRestricted Vec3F64 ab, @Override @NullRestricted Vec3F64 ac) implements Triangle3<Double> {
}
