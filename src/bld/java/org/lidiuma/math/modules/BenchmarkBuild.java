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
import rife.bld.operations.CompileOperation;
import rife.bld.operations.JavacOptions;
import rife.bld.operations.RunOperation;
import java.util.List;
import static org.lidiuma.math.MainBuild.*;
import static rife.bld.dependencies.Repository.MAVEN_CENTRAL;
import static rife.bld.dependencies.Repository.RIFE2_RELEASES;
import static rife.bld.dependencies.Scope.compile;

public final class BenchmarkBuild extends MathModule {

    private static final String JMH_VERSION = "1.37";

    public BenchmarkBuild() {

        name = "math-benchmark";
        pkg = GROUP_ID + "." + name();
        mainClass = GROUP_ID + "." + name.replace("-", "_") + ".BenchmarkMain";
        javaTool = javaToolPath();
        downloadSources = true;
        repositories = List.of(MAVEN_CENTRAL, RIFE2_RELEASES);
        version = version(0, 2, 0);
        assignModuleDirectories("benchmark");

        // I use the relative path because bld does not yet support full paths.
        final String mathJar = workDirectory().toPath().relativize(MATH.buildDistDirectory().toPath()).toString();
        scope(compile)
                .include(local(mathJar))
                .include(dependency("org.jspecify", "jspecify", version(1, 0, 0)))
                .include(dependency("org.openjdk.jmh", "jmh-core", JMH_VERSION))
                .include(dependency("org.openjdk.jmh", "jmh-generator-annprocess", JMH_VERSION));
    }

    @Override
    public RunOperation runOperation() {

        final var operation = super.runOperation();
        operation.javaOptions().enablePreview();

        final var options = operation.runOptions();
        options.remove("-prof=gc"); // Remove to avoid doubles.
        options.add("-prof=gc"); // I want to see how much garbage collection occurs.
        return operation;
    }

    @Override
    public CompileOperation compileOperation() {
        final var operation = super.compileOperation();
        final var options = operation.compileOptions();
        options.process(JavacOptions.Processing.FULL);
        commonBuildOption(options, MATH.module());
        return operation;
    }

    @Override
    public void compile() throws Exception {
        // I create the math jar to be imported by the benchmark, and move it under its temporary lib path.
        MATH.jar();
        super.compile();
    }
}
