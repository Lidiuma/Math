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

import org.lidiuma.math.modules.BenchmarkBuild;
import org.lidiuma.math.modules.MathBuild;
import org.lidiuma.math.modules.ProcessorBuild;
import rife.bld.Project;
import rife.bld.operations.JavacOptions;
import java.io.IOException;
import java.nio.file.Files;

public final class MainBuild extends Project {

    public static final String JAVA_VERSION_NAME = "java-version.txt";
    public static final String GROUP_ID = "org.lidiuma"; // TODO Modify to org.lidiuma.math?

    public static final ProcessorBuild PROCESSOR = new ProcessorBuild();
    public static final MathBuild MATH = new MathBuild();
    public static final BenchmarkBuild BENCHMARK = new BenchmarkBuild();

    private static String javaTool;

    static void main(String... args) throws Exception {
        var project = new MainBuild();
        javaTool = retrieveJavaTool(project);
        project.start(args);
    }

    public static String javaToolPath() {
        return javaTool;
    }

    private static String retrieveJavaTool(Project project) throws IOException {
        final var projectPath = project.workDirectory().toPath();
        return Files.readString(projectPath.resolve(JAVA_VERSION_NAME)) + "/bin/java";
    }

    public static void commonBuildOption(JavacOptions options, String mathModule) {
        options.target(26);
        options.source(26);
        options.enablePreview();
        options.add("--add-exports=java.base/jdk.internal.value=" + mathModule);
        options.add("--add-exports=java.base/jdk.internal.vm.annotation=" + mathModule);
    }

    @Override
    public void download() throws Exception {
        MATH.download();
        BENCHMARK.download();
    }

    @Override
    public void purge() throws Exception {
        MATH.purge();
        BENCHMARK.purge();
    }

    @Override
    public void compile() throws Exception {
        MATH.compile();
//        benchmark.compile(); TODO Add back when fixed
        PROCESSOR.compile();
    }

    @Override
    public void run() throws Exception {
        BENCHMARK.run();
    }

    @Override
    public void publish() throws Exception {
        MATH.publish();
    }

    @Override
    public void publishLocal() throws Exception {
        MATH.publishLocal();
    }

    @Override
    public void jar() throws Exception {
        MATH.jar();
        PROCESSOR.jar();
    }

    @Override
    public void javadoc() throws Exception {
        MATH.javadoc();
    }

    @Override
    public void jarSources() throws Exception {
        MATH.jarSources();
    }

    @Override
    public void jarJavadoc() throws Exception {
        MATH.jarJavadoc();
    }

    @Override
    public void dependencyTree() throws Exception {

        System.out.println("==== " + MATH.name() + " ====");
        MATH.dependencyTree();

        System.out.println("==== " + BENCHMARK.name() + " ====");
        BENCHMARK.dependencyTree();
    }

    @Override
    public void test() throws Exception {
        MATH.test();
    }
}
