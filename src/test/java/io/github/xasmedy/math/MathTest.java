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

package io.github.xasmedy.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import static org.lidiuma.math.rotation.Radians.degrees;
import static org.lidiuma.math.vector.Vectors.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public value class MathTest {

    @Test
    void testRotationV2() {

        // TODO Use matrix
//        final var start = vec2(10f, 10f);
//
//        final var end = start.rotate(degrees(180));
//        final var full = start.rotate(degrees(360));
//
//        Assertions.assertEquals(vec2(-10f, -10f), end);
//        Assertions.assertEquals(start, full);
    }

    @Test
    void testRotationV3() {

        // TODO Use matrix
//        final var start = vec3(10f, 10f, 10f);
//
//        final var endX = start.rotate(vec3(1f, 0f, 0f), degrees(180));
//        final var endY = start.rotate(vec3(0f, 1f, 0f), degrees(180));
//        final var endZ = start.rotate(vec3(0f, 0f, 1f), degrees(180));
//        final var full = start.rotate(vec3(0f, 1f, 0f), degrees(360));
//
//        Assertions.assertEquals(vec3(10f, -10f, -10f), endX);
//        Assertions.assertEquals(vec3(-10f, 10f, -10f), endY);
//        Assertions.assertEquals(vec3(-10f, -10f, 10f), endZ);
//        Assertions.assertEquals(vec3(-10f, -10f, 10f), endZ);
//        Assertions.assertEquals(vec3(-10f, -10f, 10f), endZ);
//        Assertions.assertEquals(start, full);
    }
}
