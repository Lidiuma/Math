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

package org.lidiuma.math_benchmark;

import org.jspecify.annotations.NullMarked;
import org.lidiuma.math.matrix.Affine3F32;
import org.lidiuma.math.vector.Vec3F32;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.infra.Blackhole;
import java.util.Arrays;

@NullMarked
public final class BenchmarkMain {

    static void main(String... args) throws Exception {
        IO.println(Arrays.toString(args));
        Main.main(args);
    }

    public static void consume(Blackhole hole, Affine3F32 affine) {

        hole.consume(affine.m00());
        hole.consume(affine.m01());
        hole.consume(affine.m02());
        hole.consume(affine.m03());

        hole.consume(affine.m10());
        hole.consume(affine.m11());
        hole.consume(affine.m12());
        hole.consume(affine.m13());

        hole.consume(affine.m20());
        hole.consume(affine.m21());
        hole.consume(affine.m22());
        hole.consume(affine.m23());
    }

    public static void consume(Blackhole hole, Vec3F32 vec) {
        hole.consume(vec.x());
        hole.consume(vec.y());
        hole.consume(vec.z());
    }
}
