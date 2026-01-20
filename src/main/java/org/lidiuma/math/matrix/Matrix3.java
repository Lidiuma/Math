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

package org.lidiuma.math.matrix;

import org.lidiuma.math.rotation.Quaternion;
import org.lidiuma.math.rotation.Radians;
import org.lidiuma.math.vector.v2.Vector2;
import org.lidiuma.math.vector.v3.Vector3;

/// Immutable Matrix3x3 always using post-multiplication.
/// Internal indexing is row-major, while external raw output is column-major.
///
/// Methods starting with `affine` provide optimization for matrices that have has last row the constant values `[0, 0, 1]`:
///
/// |   |   |    |
/// |:-:|:-:|:--:|
/// | a | b | tx |
/// | c | d | ty |
/// | 0 | 0 |  1 |
public interface Matrix3<M extends Matrix3<M, N, V2, V3>, N, V2 extends Vector2<V2, N>, V3 extends Vector3<V3, N>> extends Matrix<M, N> {

    int SIZE = 9;
    int M00 = 0, M01 = 3, M02 = 6;
    int M10 = 1, M11 = 4, M12 = 7;
    int M20 = 2, M21 = 5, M22 = 8;

    @Override
    default int size() {
        return SIZE;
    }

    @Override
    default int rows() {
        return 3;
    }

    @Override
    default int columns() {
        return 3;
    }

    N m00();
    N m01();
    N m02();

    N m10();
    N m11();
    N m12();

    N m20();
    N m21();
    N m22();

    /// Transforms a 3D position vector using this matrix.
    /// @return the transformed vector.
    V3 transform(V3 vector);

    /// Transforms a 2D position vector using the affine part of this matrix.
    /// @return the transformed vector.
    V2 transform(V2 vector);

    /// Applies a 3D rotation to this matrix using a quaternion.
    M rotate(Quaternion quaternion);

    /// Applies the inverse rotation of this matrix to a 3D vector.
    V3 unrotate(V3 vector);

    /// Optimized multiplication for affine matrices.
    /// @apiNote Both this and the {@code other} matrix are assumed to be affine.
    M affineMul(M other);

    /// @return the determinant of an affine matrix.
    N affineDeterminant();

    /// Inverts this matrix given that the determinant is != 0.
    /// @return This matrix for the purpose of chaining operations.
    /// @throws ArithmeticException if the matrix cannot be inverted because it is singular.
    M affineInvert();

    /// @return true if the affine matrix is a singular matrix.
    boolean isAffineSingular();

    /// @return a new affine matrix with the translation applied.
    M affineTranslate(V2 translation);

    /// @return a new affine matrix with the given rotation applied.
    M affineRotate(Radians angle);

    /// @return a new affine matrix with the given shearing applied.
    M affineShear(V2 shear);

    /// @return a new affine matrix with the given scaling applied.
    M affineScale(V2 scale);

    /// @return the scaling component from this affine matrix.
    V2 affineScale();

    /// @return the rotation component from this affine matrix as an angle.
    Radians rotation();

    /// @return the translation component from this affine matrix.
    V2 translation();
}
