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

package org.lidiuma.math;

import rife.bld.NamedFile;
import rife.bld.dependencies.Version;
import rife.bld.operations.JarOperation;
import java.nio.file.Path;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.jar.Attributes;

public final class Util {

    public static String nowUTC() {
        final var format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return ZonedDateTime.now(Clock.systemUTC()).format(format);
    }

    /// Adds LICENSE and a few attributes.
    public static void addAttributesToJar(JarOperation op, Version version) {

        // I add the LICENSE inside META-INF when creating a new jar file.
        final var license = Path.of("LICENSE").toFile();
        op.sourceFiles(new NamedFile("META-INF/LICENSE", license));

        final Map<Attributes.Name, Object> attributes = Map.of(
                new Attributes.Name("Built-By"), "Xasmedy",
                new Attributes.Name("Built-Date"), nowUTC(),
                new Attributes.Name("Version"), version.toString()
        );
        op.manifestAttributes(attributes);
    }
}
