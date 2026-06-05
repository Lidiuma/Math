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

package org.lidiuma.math.point;

/// A class to import statically to have a short way to initialize all the library points.
public value class Points {

    private Points() {}

    /* -- Point1D -- */

    public Point1I32 p1(int x) {
        return new Point1I32(x);
    }

    public Point1I64 p1(long x) {
        return new Point1I64(x);
    }

    public Point1F32 p1(float x) {
        return new Point1F32(x);
    }

    public Point1F64 p1(double x) {
        return new Point1F64(x);
    }

    /* -- Point2D -- */

    public Point2I32 p2(int x, int y) {
        return new Point2I32(x, y);
    }

    public Point2I64 p2(long x, long y) {
        return new Point2I64(x, y);
    }

    public Point2F32 p2(float x, float y) {
        return new Point2F32(x, y);
    }

    public Point2F64 p2(double x, double y) {
        return new Point2F64(x, y);
    }

    /* -- Point3D -- */

    public Point3I32 p3(int x, int y, int z) {
        return new Point3I32(x, y, z);
    }

    public Point3I64 p3(long x, long y, long z) {
        return new Point3I64(x, y, z);
    }

    public Point3F32 p3(float x, float y, float z) {
        return new Point3F32(x, y, z);
    }

    public Point3F64 p3(double x, double y, double z) {
        return new Point3F64(x, y, z);
    }

    /* -- Point4D -- */

    public Point4I32 p4(int x, int y, int z, int w) {
        return new Point4I32(x, y, z, w);
    }

    public Point4I64 p4(long x, long y, long z, long w) {
        return new Point4I64(x, y, z, w);
    }

    public Point4F32 p4(float x, float y, float z, float w) {
        return new Point4F32(x, y, z, w);
    }

    public Point4F64 p4(double x, double y, double z, double w) {
        return new Point4F64(x, y, z, w);
    }
}
