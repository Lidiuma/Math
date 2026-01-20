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
import org.lidiuma.math.vector.v3.Vector3;

/// Immutable Matrix4x4 always using post-multiplication.
/// Internal indexing is row-major, while external raw output is column-major.
@SuppressWarnings("unused")
public interface Matrix4<M extends Matrix4<M, N, V>, N, V extends Vector3<V, N>> extends Matrix<M, N> {

    int SIZE = 16;
    int M00 = 0, M01 = 4, M02 =  8, M03 = 12;
    int M10 = 1, M11 = 5, M12 =  9, M13 = 13;
    int M20 = 2, M21 = 6, M22 = 10, M23 = 14;
    int M30 = 3, M31 = 7, M32 = 11, M33 = 15;

    @Override
    default int size() {
        return SIZE;
    }

    @Override
    default int rows() {
        return 4;
    }

    @Override
    default int columns() {
        return 4;
    }

    N m00();
    N m01();
    N m02();
    N m03();

    N m10();
    N m11();
    N m12();
    N m13();

    N m20();
    N m21();
    N m22();
    N m23();

    N m30();
    N m31();
    N m32();
    N m33();

    /// Linearly interpolates between this matrix and the other matrix mixing by alpha.
    /// @param alpha the alpha value in the range `[0,1]`.
    M lerp(M other, N alpha);

    /// Averages this matrix with another, using lerp for translation/scale and slerp for rotation.
    /// @param other The other matrix.
    /// @param weight Weight for this transform (other's weight is `1 - weight`)
    M average(M other, N weight);

    /// Averages an array of matrices using the same weight.
    /// @return a new matrix representing the average transform of the input matrices.
    M average(M[] matrices);

    /// Averages an array of matrices using the provided weights.
    /// @return a new matrix representing the average transform of the input matrices.
    M average(M[] matrices, N[] weights);

    /// @return the translation part of this matrix.
    V translation();

    /// @return the rotation part of this matrix.
    Quaternion rotation();

    /// @return the scale components along each axis.
    V scale();

    /// @return the 3x3 part of this matrix as a matrix3.
    Matrix3<?, N, ?, V> asMatrix3();

    // TODO Consider SIMD versions of mulVec(), project(), and rotateVec()?

    /// Transforms a 3D position vector using the affine part of this matrix.
    /// @return the transformed vector.
    V transform(V vector);

    /// @return a new matrix with the translation applied.
    M translate(V translation);

    /// @return a new matrix with the rotation around the given axis applied.
    M rotateAround(V axis, Radians angle);

    /// @return a new matrix with the given rotation applied.
    M rotate(Quaternion rotation);

    /// @return a new matrix that rotates the direction of `v1` to align with `v2`.
    M rotateBetween(V v1, V v2);

    /// @return a new matrix that rotates to align the forward direction with `direction` and up vector with `up`.
    M rotateToDirection(V direction, V up);

    /// @return a new matrix with the given scale applied.
    M scale(V scale);

    /// Projects a 3D position vector using this matrix and performs a perspective divide.
    /// @apiNote Includes rotation, translation, scale, and perspective; output is divided by W.
    V project(V vector);

    /// Rotates a 3D direction vector using the 3×3 part of this matrix.
    /// @apiNote Translation and perspective components are ignored.
    V rotate(V vector);

    /// Applies the inverse rotation of this matrix to a 3D vector, undoing {@link #rotate(Vector3)}.
    /// @apiNote Translation and perspective components are ignored. Non-uniform scale in the 3×3 submatrix may affect results.
    V unrotate(V vector);

    /// Applies the inverse affine transformation of this matrix to a 3D vector.
    V untransform(V vector);

    /// Copies the 4x3 upper-left matrix into the array.
    /// @apiNote The layout is column-major order.
    void toMatrix4x3Array(N[] out);
}
