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

package org.lidiuma.math_benchmark.base.vector;

public class Vector3fData {
	protected float sx, sy, sz;	//Start	
	protected float ex, ey, ez;	//End
	
	protected void setupVectorData() {
		// Two arbitrary unit vectors about 62 degrees apart.
		sx = 0.309426374F; sy = 0.928279122F; sz = 0.206284249F;
		ex = 0.843274043F; ey = 0.105409255F; ez = 0.527046277F;
	}
}