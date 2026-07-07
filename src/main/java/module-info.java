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

import org.jspecify.annotations.NullMarked;

@NullMarked // Makes the whole codebase non-null by default.
module lidiuma.math {
    requires org.jspecify;
    requires lidiuma.math.api;
    exports org.lidiuma.math.tuple;
    exports org.lidiuma.math.numerics;
    exports org.lidiuma.math.rotation;
    exports org.lidiuma.math.matrix;
    exports org.lidiuma.math.vector;
    exports org.lidiuma.math.point;
    exports org.lidiuma.math.shape.rectangle;
    exports org.lidiuma.math.shape.segment;
}