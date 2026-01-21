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
import org.lidiuma.math.rotation.Radians;
import org.lidiuma.math.vector.v3.Vector3F32;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;
import java.util.Arrays;
import java.util.Random;

@NullMarked
@State(Scope.Thread)
@SuppressWarnings("unused")
public class BenchmarkMain {

    static void main(String... args) throws Exception {
        System.out.println(Arrays.toString(args));
        Main.main(args);
    }

    private double angle;
    private float x;
    private float y;
    private float z;

    @Setup
    public void setup() {
        final var random = new Random();
        angle = random.nextDouble();
        x = random.nextFloat();
        y = random.nextFloat();
        z = random.nextFloat();
    }

    @Benchmark
    @Warmup(iterations = 5,  time = 1)
    @Measurement(iterations = 2,  time = 2)
    public Vector3F32 rotationInlined() {
        final var yAxis = new Vector3F32(0f, 1f, 0f);
        final var v3 = new Vector3F32(x, y, z);
        return v3.rotate(yAxis, Radians.radians(angle));
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Warmup(iterations = 5,  time = 1)
    @Measurement(iterations = 2,  time = 2)
    public Vector3F32 rotationNoInline() {
        // I don't understand why not inlining removes allocations in this case...
        final var yAxis = new Vector3F32(0f, 1f, 0f);
        final var v3 = new Vector3F32(x, y, z);
        return v3.rotate(yAxis, Radians.radians(angle));
    }

    @Benchmark
    @Warmup(iterations = 5,  time = 1)
    @Measurement(iterations = 2,  time = 2)
    public Vector3F32 operationsInlined() {
        return new Vector3F32(x, y, z)
                .add(new Vector3F32(z, y, z))
                .mul(z)
                .div(new Vector3F32(1f, x + 1, 1f))
                .sub(new Vector3F32(y - 2, z, x));
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Warmup(iterations = 5,  time = 1)
    @Measurement(iterations = 2,  time = 2)
    public Vector3F32 operationsNoInline() {
        return new Vector3F32(x, y, z)
                .add(new Vector3F32(z, y, z))
                .mul(z)
                .div(new Vector3F32(1f, x + 1, 1f))
                .sub(new Vector3F32(y - 2, z, x));
    }
}
