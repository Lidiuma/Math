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

package org.lidiuma.math.modules;

import org.lidiuma.math.MainBuild;
import rife.bld.NamedFile;
import rife.bld.Project;
import rife.bld.operations.CompileOperation;
import rife.bld.operations.JarOperation;
import rife.bld.operations.JavadocOperation;
import rife.bld.operations.PublishOperation;
import rife.bld.publish.*;
import java.nio.file.Path;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import static org.lidiuma.math.MainBuild.GROUP_ID;
import static rife.bld.dependencies.Repository.*;
import static rife.bld.dependencies.Scope.*;

public final class MathBuild extends Project {

    private final MainBuild build;

    public MathBuild(MainBuild build) throws Exception {

        this.build = build;

        name = "math";
        pkg = GROUP_ID + "." + name();
        module = "lidiuma.math";
        version = version(0, 2, 0);

        javaTool = build.retrieveJavaTool();
        downloadSources = true;
        repositories = List.of(MAVEN_CENTRAL, RIFE2_RELEASES);

        scope(compile).include(module("org.jspecify", "jspecify", version(1, 0, 0)));

        final var junitVersion = version(6,0,1);
        scope(test)
                .include(module("org.junit.jupiter", "junit-jupiter", junitVersion))
                .include(module("org.junit.platform", "junit-platform-console-standalone", junitVersion))
                .include(module("org.junit.platform", "junit-platform-launcher", junitVersion));

        addAttributesToJar(jarOperation());
        addAttributesToJar(jarSourcesOperation());
    }

    @Override
    public PublishOperation publishOperation() {
        final var op = super.publishOperation();
        op.repositories(CENTRAL_RELEASES.withCredentials(
                property("sonatype.username"),
                property("sonatype.password")
        )).info(build.publishInfo());
        return op;
    }

    private void patchPublishJSpecify() {
        // Gradle does not support Maven 4 new types, so I'm forced to patch the type, making it `jar` instead of `modular-jar`.
        scope(compile).clear();
        scope(compile).include(dependency("org.jspecify", "jspecify", version(1, 0, 0)));
    }

    @Override
    public void publish() throws Exception {
        patchPublishJSpecify();
        super.publish();
    }

    @Override
    public void publishLocal() throws Exception {
        patchPublishJSpecify();
        super.publishLocal();
    }

    private static String nowUTC() {
        final var format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return ZonedDateTime.now(Clock.systemUTC()).format(format);
    }

    /// Adds LICENSE and a few attributes.
    private void addAttributesToJar(JarOperation op) {

        // I add the LICENSE inside META-INF when creating a new jar file.
        final var license = Path.of("LICENSE").toFile();
        op.sourceFiles(new NamedFile("META-INF/LICENSE", license));

        final Map<Attributes.Name, Object> attributes = Map.of(
                new Attributes.Name("Built-By"), "Xasmedy",
                new Attributes.Name("Built-Date"), nowUTC(),
                new Attributes.Name("Version"), version().toString()
        );
        op.manifestAttributes(attributes);
    }

    @Override
    public CompileOperation compileOperation() {
        final var operation = super.compileOperation();
        final var options = operation.compileOptions();
        build.commonBuildOption(options, module());
        return operation;
    }

    @Override
    public JavadocOperation javadocOperation() {

        final var operation = super.javadocOperation();
        final var options = operation.javadocOptions();

        options.add("--source=26");
        options.add("--enable-preview");
        options.add("--add-exports=java.base/jdk.internal.value=" + module());
        options.add("--add-exports=java.base/jdk.internal.vm.annotation=" + module());
        options.tag("apiNote", "a", "API Note:");
        options.tag("implNote", "a", "Implementation Note:");
        return operation;
    }
}