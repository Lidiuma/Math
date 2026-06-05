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

import org.lidiuma.math.api.rotation.*;
import org.lidiuma.math.numerics.FloatNumeric;
import org.lidiuma.math.vector.Vec3F32;
import static org.lidiuma.math.FloatingUtil.EPSILON_F32;

public value class QuaternionFOps implements QuaternionOps<QuaternionF, Vec3F32, AngleF, Float> {

    @Override
    public QuaternionF of(Float x, Float y, Float z, Float w) {
        return new QuaternionF(x, y, z, w);
    }

    @Override
    public QuaternionF fromAxisAngle(Vec3F32 axis, AngleF angle) {
        final float half = angle.radian() * .5f;
        final float sin = (float) Math.sin(half);
        final float cos = (float) Math.cos(half);
        return of(
                (axis.x() * sin),
                (axis.y() * sin),
                (axis.z() * sin),
                cos
        );
    }

    @Override
    public QuaternionF fromEulerAngle(AngleF yaw, AngleF pitch, AngleF roll) {
        final float hr = roll.radian() * 0.5f;
        final float shr = (float) Math.sin(hr);
        final float chr = (float) Math.cos(hr);

        final float hp = pitch.radian() * 0.5f;
        final float shp = (float) Math.sin(hp);
        final float chp = (float) Math.cos(hp);

        final float hy = yaw.radian() * 0.5f;
        final float shy = (float) Math.sin(hy);
        final float chy = (float) Math.cos(hy);

        final float chyShp = chy * shp;
        final float shyChp = shy * chp;
        final float chyChp = chy * chp;
        final float shyShp = shy * shp;

        final float newX = (chyShp * chr) + (shyChp * shr); // cos(yaw/2) * sin(pitch/2) * cos(roll/2) + sin(yaw/2) * cos(pitch/2) * sin(roll/2)
        final float newY = (shyChp * chr) - (chyShp * shr); // sin(yaw/2) * cos(pitch/2) * cos(roll/2) - cos(yaw/2) * sin(pitch/2) * sin(roll/2)
        final float newZ = (chyChp * shr) - (shyShp * chr); // cos(yaw/2) * cos(pitch/2) * sin(roll/2) - sin(yaw/2) * sin(pitch/2) * cos(roll/2)
        final float newW = (chyChp * chr) + (shyShp * shr); // cos(yaw/2) * cos(pitch/2) * cos(roll/2) + sin(yaw/2) * sin(pitch/2) * sin(roll/2)
        return new QuaternionF(newX, newY, newZ, newW);
    }

    @Override
    public QuaternionF exp(QuaternionF quaternion) {

        final var witness = Vec3F32.WITNESS;
        final var vectorQuat = new Vec3F32(quaternion.x(), quaternion.y(), quaternion.z());
        final float angle = witness.length(vectorQuat); // The math is the same.

        if (angle < EPSILON_F32) return identity();

        final float sin = (float) Math.sin(angle);
        final float cos = (float) Math.cos(angle);

        final float k = sin / angle;
        return of(
                quaternion.x() * k,
                quaternion.y() * k,
                quaternion.z() * k,
                cos
        );
    }

    @Override
    public QuaternionF log(QuaternionF quaternion) {

        final float w = Math.clamp(quaternion.w(), -1f, 1f);
        final float angle = (float) Math.acos(w);
        final float sin = (float) Math.sqrt(Math.max(0f, 1f - w * w));

        if (sin < EPSILON_F32) return of(quaternion.x(), quaternion.y(), quaternion.z(), 0f);

        final float k = angle / sin;
        return of(
                quaternion.x() * k,
                quaternion.y() * k,
                quaternion.z() * k,
                0f
        );
    }

    @Override
    public QuaternionF pow(QuaternionF quaternion, Float exponent) {
        return exp(multiply(log(quaternion), exponent));
    }

    @Override
    public QuaternionF slerp(QuaternionF start, QuaternionF end, Float alpha) {

        final float dot = dot(start, end);
        final float absDot = Math.abs(dot);
        final float sign = dot < 0f ? -1f : 1f;

        // To avoid numerical instability at low angles, I use nlerp.
        if (absDot > 0.9995f) return normalize(lerp(start, multiply(end, sign), alpha));

        final float angle = (float) Math.acos(absDot);
        final float invSinTheta = (float) (1f / Math.sin(angle));

        final float scale0 = (float) (Math.sin((1f - alpha) * angle) * invSinTheta);
        final float scale1 = (float) (Math.sin((alpha * angle)) * invSinTheta);
        return add(multiply(start, scale0), multiply(end, sign * scale1));
    }

    @Override
    public Vec3F32 rotate(QuaternionF quaternion, Vec3F32 vector) {
        final var conjugate = conjugate(quaternion);
        final var vectorQuat = of(vector.x(), vector.y(), vector.z(), 0f);
        final var rotated = multiply(multiply(quaternion, vectorQuat), conjugate);
        return new Vec3F32(rotated.x(), rotated.y(), rotated.z());
    }

    @Override
    public Vec3F32 unrotate(QuaternionF quaternion, Vec3F32 vector) {
        final var conjugate = conjugate(quaternion);
        final var vectorQuat = of(vector.x(), vector.y(), vector.z(), 0f);
        final var unrotated = multiply(multiply(conjugate, vectorQuat), quaternion);
        return new Vec3F32(unrotated.x(), unrotated.y(), unrotated.z());
    }

    @Override
    public AxisAngleF axisAngle(QuaternionF quaternion, Float epsilon) {

        final float sqrt = (float) Math.sqrt(1f - quaternion.w() * quaternion.w());
        final var angle = angle(quaternion);

        // I avoid dividing by 0 if the sqrt is small enough.
        if (sqrt < epsilon) {
            // I re-normalize because without w the length might no longer be 1.
            final var ws = Vec3F32.WITNESS;
            final var axis = ws.normalize(new Vec3F32(quaternion.x(), quaternion.y(), quaternion.z()));
            return new AxisAngleF(axis, angle);
        }

        final var axis = new Vec3F32(
                quaternion.x() / sqrt,
                quaternion.y() / sqrt,
                quaternion.z() / sqrt
        );
        return new AxisAngleF(axis, angle);
    }

    @Override
    public AngleF angle(QuaternionF quaternion) {
        final float w = Math.clamp(quaternion.w(), -1f, 1f);
        return AngleF.radians((float) (2f * Math.acos(w)));
    }

    @Override
    public SwingTwistF swingTwist(QuaternionF quaternion, Vec3F32 axis) {
        final var twist = twist(quaternion, axis);
        final var swing = multiply(quaternion, conjugate(twist));
        return new SwingTwistF(swing, twist);
    }

    @Override
    public AngleF angleAround(QuaternionF quaternion, Vec3F32 axis) {
        return angle(twist(quaternion, axis));
    }

    @Override
    public FloatNumeric scalarWitness() {
        return FloatNumeric.WITNESS;
    }

    private QuaternionF twist(QuaternionF quaternion, Vec3F32 axis) {

        final var witness = Vec3F32.WITNESS;
        final var vectorQuat = new Vec3F32(quaternion.x(), quaternion.y(), quaternion.z());
        final float dot = witness.dot(vectorQuat, axis);

        return normalize(of(
                axis.x() * dot,
                axis.y() * dot,
                axis.z() * dot,
                quaternion.w())
        );
    }
}
