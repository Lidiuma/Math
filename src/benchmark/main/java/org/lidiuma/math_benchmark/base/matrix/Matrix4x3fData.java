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

package org.lidiuma.math_benchmark.base.matrix;

public class Matrix4x3fData {
	protected float tx, ty, tz; 		//Translation Data
	protected float angle, ax, ay, az;	//Rotation Data (Axis Angle)
	protected float qx, qy, qz, qw;		//Rotation Data (Quaternion)
	protected float sx, sy, sz;			//Scale Data
	protected float px, py, pz;			//Position Data
	
	protected void setupMatrixData() {
		tx = 32F; ty = 0.5F; tz = 1F;
		angle = 0.558505361F; ax = 0F; ay = 1F; az = 0F;
		qx = 0F; qy = 0.275637356F; qz = 0F; qw = 0.961261696F;
		sx = 0.25F; sy = 2F; sz = 1F;
		px = 1F; py = 3F; pz = 6F;
	}
}