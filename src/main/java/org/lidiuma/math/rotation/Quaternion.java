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

package org.lidiuma.math.rotation;

import org.lidiuma.math.matrix.Matrix3F64;
import org.lidiuma.math.matrix.Matrix4F64;
import org.lidiuma.math.vector.v3.Vector3F64;
import org.lidiuma.math.vector.v4.Vector4F64;
import jdk.internal.vm.annotation.LooselyConsistentValue;
import static org.lidiuma.math.FloatingUtil.EPSILON_F32;

@LooselyConsistentValue
public value record Quaternion(double x, double y, double z, double w) {

    public Quaternion(Vector4F64 v4) {
        this(v4.x(), v4.y(), v4.z(), v4.w());
    }

    /// @return a new identity quaternion representing no rotation.
    public static Quaternion identity() {
        return new Quaternion(0, 0, 0, 1);
    }

    /// Creates a quaternion from an axis and a rotation angle in radians.
    /// @param axis the rotation axis.
    /// @param angle the rotation angle in radians.
    /// @return a new quaternion representing the rotation.
    /// @apiNote The axis is normalized automatically.
    public static Quaternion fromAxisAngle(Vector3F64 axis, Radians angle) {
        final var nor = axis.normalize();
        final double half = angle.value() * .5f;
        final double sin = Math.sin(half);
        final double cos = Math.cos(half);
        return new Quaternion(
                (nor.x() * sin),
                (nor.y() * sin),
                (nor.z() * sin),
                cos
        );
    }

    /// Creates a new quaternion from the given Euler angles in radians.
    ///
    /// @param yaw   rotation around the y-axis in radians.
    /// @param pitch rotation around the x-axis in radians.
    /// @param roll  rotation around the z-axis in radians.
    /// @return a new quaternion representing the rotation.
    /// @apiNote Euler angles are applied in y (yaw), x (pitch), z (roll) order.
    public static Quaternion fromEulerAngles(Radians yaw, Radians pitch, Radians roll) {

        final double hr = roll.value() * 0.5f;
        final double shr = Math.sin(hr);
        final double chr = Math.cos(hr);

        final double hp = pitch.value() * 0.5f;
        final double shp = Math.sin(hp);
        final double chp = Math.cos(hp);

        final double hy = yaw.value() * 0.5f;
        final double shy = Math.sin(hy);
        final double chy = Math.cos(hy);

        final double chyShp = chy * shp;
        final double shyChp = shy * chp;
        final double chyChp = chy * chp;
        final double shyShp = shy * shp;

        final double newX = (chyShp * chr) + (shyChp * shr); // cos(yaw/2) * sin(pitch/2) * cos(roll/2) + sin(yaw/2) * cos(pitch/2) * sin(roll/2)
        final double newY = (shyChp * chr) - (chyShp * shr); // sin(yaw/2) * cos(pitch/2) * cos(roll/2) - cos(yaw/2) * sin(pitch/2) * sin(roll/2)
        final double newZ = (chyChp * shr) - (shyShp * chr); // cos(yaw/2) * cos(pitch/2) * sin(roll/2) - sin(yaw/2) * sin(pitch/2) * cos(roll/2)
        final double newW = (chyChp * chr) + (shyShp * shr); // cos(yaw/2) * cos(pitch/2) * cos(roll/2) + sin(yaw/2) * sin(pitch/2) * sin(roll/2)
        return new Quaternion(newX, newY, newZ, newW);
    }

    /// Creates a quaternion representing the rotation from one vector to another.
    ///
    /// @param v1 the starting normalized vector.
    /// @param v2 the target normalized vector.
    /// @return a new quaternion that rotates v1 onto v2.
    /// @apiNote If the vectors are already parallel, the identity quaternion is returned.
    ///          If the vectors are antiparallel, the rotation is 180° around any axis
    ///          perpendicular to the vectors.
    public static Quaternion fromRotationBetween(Vector3F64 v1, Vector3F64 v2) {

        final double dot = Math.clamp(v1.dot(v2), -1f, 1f);
        final var cross = v1.cross(v2);

        // I check if the vectors are not parallel.
        if (cross.length2() >= EPSILON_F32) {
            final Radians angle = Radians.radians(Math.acos(dot));
            return fromAxisAngle(cross, angle);
        }

        // When the vectors are parallel.
        if (dot > 0f) return identity();
        // In case the vectors are antiparallel, I need to rotate 180 degrees.
        final Vector3F64 perpendicular = Math.abs(v1.x()) < 0.9d ?
                new Vector3F64(1d, 0d, 0d) :
                new Vector3F64(0d, 1d, 0d);
        return fromAxisAngle(perpendicular.cross(v1), Radians.radians(Math.PI));
    }

    /// Spherical linearly interpolates multiple quaternions by the given weights.
    /// @param quat the quaternions to interpolate.
    /// @param weights the weight used for each quaternion, the length must be the same as the quaternions array.
    /// @return the interpolated weighted quaternion.
    /// @apiNote
    /// - Both arrays are only read and not modified.
    /// - Multiplication is performed left-to-right.
    public static Quaternion weightedSlerp(Quaternion[] quat, double[] weights) {

        if (quat.length == 0) throw new IllegalArgumentException("Provided empty quaternion array.");
        if (quat.length != weights.length) throw new IllegalArgumentException("The quaternion and weights arrays do not have the same length.");

        Quaternion result = quat[0].pow(weights[0]);

        for (int i = 1; i < quat.length; i++) {
            final Quaternion exp = quat[i].pow(weights[i]);
            result = result.mul(exp);
        }
        return result.normalize();
    }

    /// Spherical linearly interpolates multiple quaternions.
    /// @param quat the quaternions to interpolate.
    /// @return the interpolated weighted quaternion.
    /// @apiNote
    /// - The array is only read and not modified.
    /// - All quaternions have the same weight `(1 / array length)`.
    /// - Multiplication is performed left-to-right.
    public static Quaternion weightedSlerp(Quaternion[] quat) {

        if (quat.length == 0) throw new IllegalArgumentException("Provided empty quaternion array.");

        final double weight = 1f / quat.length;
        Quaternion result = quat[0].pow(weight);

        for (int i = 1; i < quat.length; i++) {
            final Quaternion exp = quat[i].pow(weight);
            result = result.mul(exp);
        }
        return result.normalize();
    }

    /// Creates a quaternion representing a rotation from three orthogonal axes.
    ///
    /// The method computes the quaternion using the matrix trace to select the largest component first,
    /// which ensures numerical stability and avoids division by very small numbers.
    /// @return a quaternion representing the same rotation as the given axes.
    /// @apiNote The axes are normalized internally.
    public static Quaternion fromAxes(Vector3F64 xAxis, Vector3F64 yAxis, Vector3F64 zAxis) {

        final var x = xAxis.normalize();
        final var y = yAxis.normalize();
        final var z = zAxis.normalize();

        final double trace = x.x() + y.y() + z.z();
        final double qw, qx, qy, qz;

        // We make the division safe by ensuring that s is always bigger or equal than 1.
        if (trace >= 0f) {
            final double s = Math.sqrt(trace + 1f);
            final double ss = .5f / s;
            qw = .5f * s; // |w| >= .5
            qx = (z.y() - y.z()) * ss;
            qy = (x.z() - z.x()) * ss;
            qz = (y.x() - x.y()) * ss;
        } else if ((x.x() > y.y()) && (x.x() > z.z())) {
            final double s = Math.sqrt(1f + x.x() - y.y() - z.z());
            final double ss = .5f / s;
            qx = s * .5f; // |x| >= .5
            qy = (y.x() + x.y()) * ss;
            qz = (x.z() + z.x()) * ss;
            qw = (z.y() - y.z()) * ss;
        } else if (y.y() > z.z()) {
            final double s = Math.sqrt(1f + y.y() - x.x() - z.z());
            final double ss = .5f / s;
            qy = s * .5f; // |y| >= .5
            qx = (y.x() + x.y()) * ss;
            qz = (z.y() + y.z()) * ss;
            qw = (x.z() - z.x()) * ss;
        } else {
            final double s = Math.sqrt(1f + z.z() - x.x() - y.y());
            final double ss = .5f / s;
            qz = s * .5f; // |z| >= .5
            qx = (x.z() + z.x()) * ss;
            qy = (z.y() + y.z()) * ss;
            qw = (y.x() - x.y()) * ss;
        }
        return new Quaternion(qx, qy, qz, qw);
    }

    /// @return A quaternion representing the rotation of the matrix.
    public static Quaternion fromMatrix4(Matrix4F64 matrix) {
        return fromMatrix3(matrix.asMatrix3());
    }

    /**
     * Sets the Quaternion from the given rotation matrix, which must not contain scaling.
     */
    public static Quaternion fromMatrix3(Matrix3F64 matrix) {
        final var x = new Vector3F64(matrix.m00(), matrix.m01(), matrix.m02());
        final var y = new Vector3F64(matrix.m10(), matrix.m11(), matrix.m12());
        final var z = new Vector3F64(matrix.m20(), matrix.m21(), matrix.m22());
        return fromAxes(x, y, z);
    }

    public Vector4F64 v4() {
        return new Vector4F64(x(), y(), z(), w());
    }

    /**
     * @return the Euclidean length of this quaternion.
     */
    public double length() {
        return v4().length();
    }

    /**
     * Gets the pole of the gimbal lock, if any.
     *
     * @return {@link GimbalPole#NORTH}, {@link GimbalPole#SOUTH}, or {@link GimbalPole#NONE}
     */
    public GimbalPole gimbalPole() {
        final double t = y * x + z * w;
        if (t > 0.499f) return GimbalPole.NORTH;
        if (t < -0.499f) return GimbalPole.SOUTH;
        return GimbalPole.NONE;
    }

    /// Returns the roll (rotation around the z-axis) in radians.
    ///
    /// @return the roll in radians, between -π and +π.
    /// @apiNote The quaternion should be normalized for correct results.
    public Radians roll() {
        final GimbalPole pole = gimbalPole();
        final double radians = switch (pole) {
            case NORTH, SOUTH -> pole.sign * 2.0 * Math.atan2(y, w);
            case NONE -> Math.atan2(2.0 * (w * z + y * x), 1.0 - 2.0 * (x * x + z * z));
        };
        return Radians.radians(radians);
    }

    /// Returns the pitch (rotation around the x-axis) in radians.
    ///
    /// @return the pitch in radians, between -(π/2) and +(π/2).
    /// @apiNote The quaternion should be normalized for correct results.
    public Radians pitch() {
        final GimbalPole pole = gimbalPole();
        final double radians = switch (pole) {
            case NORTH, SOUTH -> pole.sign * Math.PI * 0.5;
            case NONE -> Math.asin(Math.clamp(2.0 * (w * x - z * y), -1.0, 1.0));
        };
        return Radians.radians(radians);
    }

    /// Returns the yaw (rotation around the y-axis) in radians.
    ///
    /// @return the yaw in radians, between -π and +π.
    /// @apiNote The quaternion should be normalized for correct results.
    ///  When the quaternion is in a gimbal lock configuration, the yaw is set to zero by convention.
    public Radians yaw() {
        final double radians = switch (gimbalPole()) {
            case NORTH, SOUTH -> 0.0;
            case NONE -> Math.atan2(2.0 * (y * w + x * z), 1.0 - 2.0 * (y * y + x * x));
        };
        return Radians.radians(radians);
    }

    /**
     * @return the length of this quaternion without square root
     */
    public double length2() {
        return v4().length2();
    }

    /**
     * Normalizes this quaternion to unit length
     *
     * @return the quaternion for chaining
     */
    public Quaternion normalize() {
        return new Quaternion(v4().normalize());
    }

    /// @return The conjugated quaternion
    public Quaternion conjugate() {
        return new Quaternion(-x(), -y(), -z(), w());
    }

    /// Rotates the given vector using this quaternion.
    ///
    /// @param v3 the vector to rotate
    /// @return a new rotated vector.
    /// @apiNote The quaternion is normalized internally.
    public Vector3F64 rotate(Vector3F64 v3) {
        final var norm = normalize();
        final var other = new Quaternion(v3.asV4(0d));
        return norm.mul(other)
                .mul(norm.conjugate())
                .v4()
                .asV3();
    }

    /// Returns the Hamilton product of `this` quaternion and `other`.
    ///
    /// @param other the quaternion to multiply.
    /// @return a new quaternion equal to `this * other`
    /// @apiNote Order is important! `this * other != other * this`
    public Quaternion mul(Quaternion other) {
        final double newX = w() * other.x() + x() * other.w() + y() * other.z() - z() * other.y();
        final double newY = w() * other.y() + y() * other.w() + z() * other.x() - x() * other.z();
        final double newZ = w() * other.z() + z() * other.w() + x() * other.y() - y() * other.x();
        final double newW = w() * other.w() - x() * other.x() - y() * other.y() - z() * other.z();
        return new Quaternion(newX, newY, newZ, newW);
    }

    /// Returns the Hamilton product of `other` and `this` quaternion.
    ///
    /// @param other the quaternion to multiply.
    /// @return a new quaternion equal to `other * this`
    /// @apiNote Order is important! `other * this != this * other`
    public Quaternion preMul(Quaternion other) {
        return other.mul(this);
    }
    /**
     * Add the x,y,z,w components of the passed in quaternion to the ones of this quaternion
     */
    public Quaternion add(Quaternion other) {
        return new Quaternion(v4().add(other.v4()));
    }

    /**
     * @return If this quaternion is an identity Quaternion
     */
    public boolean isIdentity() {
        return isIdentity(EPSILON_F32);
    }

    /**
     * @param tolerance allowed deviation from exact identity
     * @return true if this quaternion is approximately identity
     */
    public boolean isIdentity(double tolerance) {
        return Math.abs(x) < tolerance
                && Math.abs(y) < tolerance
                && Math.abs(z) < tolerance
                && Math.abs(w - 1f) < tolerance;
    }

    /// Spherical interpolation between this quaternion and the other quaternion.
    /// @param end the other quaternion.
    /// @param alpha value in the range of `[0,1]`.
    /// @param epsilon threshold to switch between lerp and full slerp at small angles.
    /// @return the interpolated quaternion.
    /// @apiNote The quaternions are normalized internally.
    public Quaternion slerp(Quaternion end, double alpha, double epsilon) {

        final Quaternion t = normalize();
        final Quaternion e = end.normalize();
        final double dot = t.dot(e);
        final double absDot = Math.abs(dot);

        // Set the default values in case the angle is too small to calculate the slerp.
        double scale0 = 1f - alpha;
        double scale1 = alpha;

        // To avoid numerical instability at low angles, I skip the lerp if the angle is small enough.
        if ((1 - absDot) > epsilon) {

            final double angle = Math.acos(absDot);
            final double invSinTheta = 1f / Math.sin(angle);

            scale0 = (Math.sin((1f - alpha) * angle) * invSinTheta);
            scale1 = (Math.sin((alpha * angle)) * invSinTheta);
        }

        if (dot < 0f) scale1 = -scale1;
        return new Quaternion(
                (scale0 * t.x) + (scale1 * e.x),
                (scale0 * t.y) + (scale1 * e.y),
                (scale0 * t.z) + (scale1 * e.z),
                (scale0 * t.w) + (scale1 * e.w)
        );
    }

    /// Spherical interpolation between this quaternion and the other quaternion.
    /// @param end the other quaternion
    /// @param alpha value in the range of [0,1]
    /// @return the interpolated quaternion.
    public Quaternion slerp(Quaternion end, double alpha) {
        return slerp(end, alpha, 1e-4f);
    }

    /// Returns the power of `quaternion^alpha`.
    /// @param alpha The exponent.
    public Quaternion pow(double alpha) {

        final double norm = length();
        final double normExp = Math.pow(norm, alpha); // |q|^alpha
        final double theta = Math.acos(w / norm);
        final double sinTheta = Math.sin(theta);

        // To avoid numerical instability at low angles, I approximate the coefficient.
        final double coefficient = Math.abs(theta) < EPSILON_F32 ?
                normExp * alpha / norm :
                normExp * Math.sin(alpha * theta) / (norm * sinTheta);

        final var result = new Quaternion(
                (x * coefficient),
                (y * coefficient),
                (z * coefficient),
                (normExp * Math.cos(alpha * theta))
        );
        return result.normalize(); // Fixes any possible discrepancies.
    }

    /**
     * Get the dot product between this and the other quaternion (commutative).
     *
     * @param other the other quaternion.
     * @return the dot product of this and the other quaternion.
     */
    public double dot(Quaternion other) {
        return v4().dot(other.v4());
    }

    /**
     * Multiplies the components of this quaternion with the given scalar.
     *
     * @param scalar the scalar.
     * @return this quaternion for chaining.
     */
    public Quaternion mul(double scalar) {
        return new Quaternion(v4().mul(scalar));
    }

    /// Returns the axis-angle representation of this quaternion's rotation.
    /// @return {@link AxisAngle} containing both the axis (as a unit vector) and the angle.
    /// @apiNote The quaternion is normalized internally.
    public AxisAngle axisAngle() {

        final Quaternion quat = normalize();
        final double sqrt = Math.sqrt(1d - quat.w * quat.w);

        // I avoid dividing by 0 if the sqrt is small enough.
        final Vector3F64 newAxis = sqrt < EPSILON_F32 ?
                new Vector3F64(quat.x, quat.y, quat.z).normalize() : // I re-normalize because without w the length might no longer be 1.
                new Vector3F64(quat.x / sqrt, quat.y / sqrt, quat.z / sqrt);

        final Radians angle = angle();
        return new AxisAngle(newAxis, angle);
    }

    /// @return the rotation angle of this quaternion in radians.
    /// @apiNote The quaternion is normalized internally.
    public Radians angle() {
        final double wNorm = normalize().w();
        final double angle = (2d * Math.acos(Math.clamp(wNorm, -1f, 1f)));
        return Radians.radians(angle);
    }

    /// Gets the swing rotation and twist rotation for the specified axis.
    /// - The twist rotation represents the rotation around the specified axis.
    /// - The swing rotation represents the rotation of the specified axis itself, which is the rotation around an axis perpendicular to the specified axis.
    ///
    ///  The swing and twist rotation can be used to reconstruct the original quaternion; `this = swing * twist`.
    ///
    /// @param axis of which to get the swing and twist rotation.
    /// @return the `swing` and `twist` pair.
    /// @apiNote The axis is normalized internally.
    public SwingTwist swingTwist(Vector3F64 axis) {

        final var norm = axis.normalize();
        final double dot = v4().asV3().dot(norm);

        var twist = new Quaternion(norm.x() * dot, norm.y() * dot, norm.z() * dot, w).normalize();
        if (dot < 0) twist = twist.mul(-1f);

        final var swing = twist.conjugate().preMul(normalize());
        return new SwingTwist(swing, twist);
    }

    /**
     * Get the angle in radians of the rotation around the specified axis. The axis must be normalized.
     *
     * @param axis the normalized axis for which to get the angle
     * @return the angle in radians of the rotation around the specified axis
     */
    public Radians angleAround(Vector3F64 axis) {

        final double dot = v4().asV3().dot(axis);
        final var qAxis = new Quaternion(axis.x() * dot, axis.y() * dot, axis.z() * dot, w);
        final double l2 = qAxis.length2();

        if (l2 < EPSILON_F32 * EPSILON_F32) return Radians.radians(0.0);

        final double fixedW = dot < 0 ? -w : w;
        final double clamped = Math.clamp(fixedW / Math.sqrt(l2), -1.0, 1.0);
        final double radians = 2.0 * Math.acos(clamped);

        return Radians.radians(radians);
    }
}
