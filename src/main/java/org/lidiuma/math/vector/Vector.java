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

package org.lidiuma.math.vector;

import java.util.function.UnaryOperator;

public interface Vector<T extends Vector<T, N>, N> {

    /// @return The dimension of this vector.
    int dimension();

    /// @return the component at the provided index.<br>
    ///  Example, for a Vector2, `0` returns `x`, `1` returns `y`, while `2` throws {@link IndexOutOfBoundsException}.
    /// @apiNote **Warning!** This method is slow since it needs to resolve the index.
    N component(int index) throws IndexOutOfBoundsException;

    /// Sums the components of the vector together.
    N sum();

    T add(T other);

    T sub(T other);

    T mul(T other);

    T mul(N scalar);

    T div(T other);

    boolean lt(T other);

    boolean ltEq(T other);

    boolean gt(T other);

    boolean gtEq(T other);

    T abs();

    T max(T other);

    T min(T other);

    N distance2(T vector);

    N length2();

    N dot(T vector);

    T clamp(N min, N max);

    boolean hasSameDirection(T vector);

    boolean hasOppositeDirection(T vector);

    interface Int<T extends Int<T, N>, N> extends Vector<T, N> {
        /// Converts this integer vector to a real/floating-point vector.
        /// This conversion may be lossy if the integer cannot be represented exactly as a floating-point.
        /// If a conversion with no precision loss is needed, it is possible with pattern matching:
        /// ```java
        /// var vector = new Vector2.F64(10, 30);
        /// if (vector instanceof Vector2.F64(int x, int y)){
        ///     var newVector = new Vector2.I32(x, y);
        ///}
        ///```
        Real<?, ?> asReal();
    }

    interface Real<T extends Real<T, N>, N> extends Vector<T, N> {

        /// @return This vector with the floating point value truncated.
        Int<?, ?> asInt();

        T ceil();

        T floor();

        N length();

        T withLength(N length);

        T withLength2(N length2);

        T limit(N limit);

        T limit2(N limit2);

        T normalize();

        N distance(T vector);

        T lerp(T target, N alpha);

        T interpolate(T target, N alpha, UnaryOperator<N> interpolator);

        boolean isUnit(N margin);

        boolean isCollinear(T vector, N epsilon);

        boolean isPerpendicular(T vector, N epsilon);

        boolean epsilonEquals(T vector, N epsilon);

        boolean isZero(N epsilon);
    }
}
