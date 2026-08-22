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

import org.lidiuma.math.MathModule;
import org.lidiuma.math.Util;
import rife.bld.operations.JavacOptions;
import java.util.List;
import static org.lidiuma.math.Math.*;
import static org.lidiuma.math.PublishUtil.patchDependencies;
import static org.lidiuma.math.PublishUtil.publishCentralConfiguration;
import static rife.bld.dependencies.Repository.*;
import static rife.bld.dependencies.Scope.*;

public final class MathBuild extends MathModule {

    public MathBuild() {

        name = "Math";
        pkg = GROUP_ID + "." + name();
        module = "lidiuma.math";
        version = version(0, 3, 0);
        downloadSources = true;
        repositories = List.of(MAVEN_CENTRAL, CENTRAL_SNAPSHOTS, RIFE2_RELEASES);
        assignModuleDirectories("math");

        includeDependencies();

        commonBuildOption(compileOperation().compileOptions(), module());
        addCodeGenerator();

        Util.addAttributesToJar(jarOperation(), version());
        Util.addAttributesToJar(jarSourcesOperation(), version());

        publishCentralConfiguration(this, false);
        modifyJavadocOperations();
    }

    private void includeDependencies() {

        scope(compile)
                .include(module("org.jspecify", "jspecify", version(1, 0, 0)))
                .include(module("org.lidiuma.math", "math-api", version(1, 0, 0, "rc2")))
                .include(module("org.lidiuma.math", "math-traits", version(0, 1, 1)));

        // Required for testing, otherwise the different bytecode version will make the test classes non-findable.
        testOperation().javaOptions().add("--enable-preview");
        final var jUnitVersion = version(6,1,3);
        scope(test)
                .include(module("org.junit.jupiter", "junit-jupiter", jUnitVersion))
                .include(module("org.junit.platform", "junit-platform-console-standalone", jUnitVersion));

        final var processorDir = workDirectory()
                .toPath()
                .relativize(PROCESSOR.buildDistDirectory().toPath()); // Use absolute when bld 2.3.1 releases.
        scope(provided).include(localModule(processorDir.toString()));
    }

    private void addCodeGenerator() {
        final var generatedSources = buildDirectory().toPath().resolve("annotation-source");
        compileOperation().compileOptions()
                .parameters() // I allow reading the variable names, since it makes the library easier to use and understand.
                .process(JavacOptions.Processing.FULL)
                .sourceOutput(generatedSources)
                .processorModulePath(PROCESSOR.buildDistDirectory().toPath());
        jarSourcesOperation().sourceDirectories().add(generatedSources.toFile());
    }

    private void modifyJavadocOperations() {
        final var options = javadocOperation().javadocOptions();
        options.add("--source=28");
        options.add("--enable-preview");
        options.add("--add-exports=java.base/jdk.internal.value=" + module());
        options.add("--add-exports=java.base/jdk.internal.vm.annotation=" + module());
        options.tag("apiNote", "a", "API Note:");
        options.tag("implNote", "a", "Implementation Note:");
    }

    @Override
    public void publish() throws Exception {
        patchDependencies(this);
        super.publish();
    }

    @Override
    public void publishLocal() throws Exception {
        patchDependencies(this);
        super.publishLocal();
    }

    @Override
    public void compile() throws Exception {
        PROCESSOR.jar(); // Dependency.
        super.compile();
    }
}