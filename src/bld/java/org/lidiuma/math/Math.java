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
import java.util.Arrays;

public interface Math {


    String AVAILABLE = "(Available: \"math\", \"benchmark\", \"processor\")";
    String GROUP_ID = "org.lidiuma"; // TODO Modify to org.lidiuma.math?
    // Minor code re-use.
    String JAVA_VERSION_NAME = "java-version.txt";
    // Must be declared in this order.
    ProcessorBuild PROCESSOR = new ProcessorBuild();
    MathBuild MATH = new MathBuild();
    BenchmarkBuild BENCHMARK = new BenchmarkBuild();

    static void main(String... args) {

        if (args.length == 0) {
            System.err.println("Please provide the module in the arguments. " + AVAILABLE);
            return;
        }

        final String module = args[0].toLowerCase();
        final Project project = switch (module) {
            case "math" -> MATH;
            case "benchmark" -> BENCHMARK;
            case "processor" -> PROCESSOR;
            default -> null;
        };

        if (project == null) {
            System.err.println("Unknown module name \"" + module + "\". " + AVAILABLE);
            return;
        }

        final String[] bldArgs = Arrays.copyOfRange(args, 1, args.length);
        System.out.println("== \"" + module + "\" module selected ==");
        project.start(bldArgs);
    }

    static String javaToolPath(Project project) {
        try {
            final var projectPath = project.workDirectory().toPath();
            return Files.readString(projectPath.resolve(JAVA_VERSION_NAME)) + "/bin/java";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void commonBuildOption(JavacOptions options, String mathModule) {
        options.target(28);
        options.source(28);
        options.enablePreview();
        options.add("--add-exports=java.base/jdk.internal.value=" + mathModule);
        options.add("--add-exports=java.base/jdk.internal.vm.annotation=" + mathModule);
    }
}