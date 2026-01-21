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
import rife.bld.BaseProject;
import rife.bld.operations.CompileOperation;
import rife.bld.operations.RunOperation;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import static org.lidiuma.math.MainBuild.GROUP_ID;
import static rife.bld.dependencies.Repository.MAVEN_CENTRAL;
import static rife.bld.dependencies.Repository.RIFE2_RELEASES;
import static rife.bld.dependencies.Scope.compile;

public final class BenchmarkBuild extends BaseProject {

    private static final String JMH_VERSION = "1.37";
    private final MainBuild build;

    public BenchmarkBuild(MainBuild build) throws Exception {

        this.build = build;

        name = "math-benchmark";
        pkg = GROUP_ID + "." + name();
        mainClass = GROUP_ID + "." + name.replace("-", "_") + ".BenchmarkMain";

        srcDirectory = new File(name, "src");
        buildMainDirectory = new File(buildDirectory(), "benchmark");

        javaTool = build.retrieveJavaTool();
        downloadSources = true;
        repositories = List.of(MAVEN_CENTRAL, RIFE2_RELEASES);
        version = version(0, 2, 0);

        scope(compile)
                .include(local( name + "/lib"))
                .include(module("org.jspecify", "jspecify", version(1, 0, 0)))
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
        options.processorPath(processorPath());
        build.commonBuildOption(options, build.module());
        return operation;
    }

    private String processorPath() {
        final var lib = libCompileDirectory().getPath();
        return String.format("%s/jmh-generator-annprocess-%s.jar:%s/jmh-core-%s.jar", lib, JMH_VERSION, lib, JMH_VERSION);
    }

    @Override
    public void compile() throws Exception {

        // I create the math jar to be imported by the benchmark, and move it under its temporary lib path.
        build.math.jar();

        final var jarName = build.math.jarFileName();
        final var jarPath = buildDistDirectory().toPath().resolve(jarName);
        final var target = workDirectory().toPath().resolve(name(), "lib");

        Files.createDirectories(target);
        Files.move(jarPath, target.resolve(jarName), StandardCopyOption.REPLACE_EXISTING);

        super.compile();
    }
}
