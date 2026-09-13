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

package org.lidiuma.math_benchmark.lidiuma_math.vector;

import org.lidiuma.math.vector.Vec3F32;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import java.util.concurrent.TimeUnit;
import static org.lidiuma.math.vector.Vectors.*;
import static org.lidiuma.math_benchmark.BenchmarkMain.consume;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)

@Warmup(iterations = 2, time = 3, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@Threads(1)
@OperationsPerInvocation(5)
public final class Vec3F32Benchmark {

	Vec3F32 a;
	Vec3F32 b;	
	
	@Setup(Level.Iteration)
	public void setupMatrix() {
		a = new Vec3F32(0.0f, 1.0f, 0.0f);
		b = new Vec3F32(1.0f, 0.0f, 0.0f);
	}
	
	@Benchmark
	public void testCreation(Blackhole hole) {
		consume(hole, new Vec3F32(1F, 0F, 0F));
	}

	@Benchmark
	public void testExampleCase(Blackhole hole) {
		Vec3F32 c = add(a, b);
		consume(hole, normalize(cross(c, a)));
	}
	
	@Benchmark
	public float testAngle() {
		final double dot = dot(a, b);
		final double length1 = lengthSquared(a);
		final double length2 = lengthSquared(b);
		final double theta = dot / Math.sqrt(length1 * length2);
		return (float) Math.acos(theta);
	}
}
