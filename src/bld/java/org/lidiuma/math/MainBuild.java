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
import rife.bld.Project;
import rife.bld.operations.JavacOptions;
import rife.bld.publish.PublishDeveloper;
import rife.bld.publish.PublishInfo;
import rife.bld.publish.PublishLicense;
import rife.bld.publish.PublishScm;
import java.io.IOException;
import java.nio.file.Files;
import static java.lang.String.format;

public final class MainBuild extends Project {

    public static final String JAVA_VERSION_NAME = "java-version.txt";
    public static final String GROUP_ID = "org.lidiuma";

    public final MathBuild math;
    public final BenchmarkBuild benchmark;

    public MainBuild() throws Exception {
        this.math = new MathBuild(this);
        this.benchmark = new BenchmarkBuild(this);
    }

    static void main(String... args) throws Exception {
        new MainBuild().start(args);
    }

    public String retrieveJavaTool() throws IOException {
        final var projectPath = workDirectory().toPath();
        return Files.readString(projectPath.resolve(JAVA_VERSION_NAME)) + "/bin/java";
    }

    public void commonBuildOption(JavacOptions options, String mathModule) {
        options.target(26);
        options.source(26);
        options.enablePreview();
        options.add("--add-exports=java.base/jdk.internal.value=" + mathModule);
        options.add("--add-exports=java.base/jdk.internal.vm.annotation=" + mathModule);
    }

    public PublishInfo publishInfo() {

        final String org = "lidiuma";
        final String artifactId = name();
        final String github = "https://github.com";
        final String project = format("%s/%s/%s", github, org, artifactId);

        final var license = new PublishLicense()
                .name("The Apache License, Version 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0.txt");

        final String devName = "Xasmedy";
        final var developer = new PublishDeveloper()
                .id(devName.toLowerCase())
                .name(devName)
                .email("xasmedy@pm.me")
                .url(format("%s/%s", github, devName));

        final var scm = new PublishScm()
                .connection(format("scm:git:%s.git", project))
                .developerConnection(format("scm:git:git@github.com:%s/%s.git", org, artifactId))
                .url(project);

        return new PublishInfo()
                .groupId(GROUP_ID)
                .artifactId(artifactId)
                .version(version())
                .name("Math")
                .description("Valhalla-based Math Library")
                .url(project)
                .developer(developer)
                .license(license)
                .scm(scm)
                .signKey(property("sign.key"))
                .signPassphrase(property("sign.passphrase"));
    }

    @Override
    public void download() throws Exception {
        math.download();
        benchmark.download();
    }

    @Override
    public void purge() throws Exception {
        math.purge();
        benchmark.purge();
    }

    @Override
    public void compile() throws Exception {
        math.compile();
        benchmark.compile();
    }

    @Override
    public void run() throws Exception {
        benchmark.run();
    }

    @Override
    public void publish() throws Exception {
        math.publish();
    }

    @Override
    public void publishLocal() throws Exception {
        math.publishLocal();
    }

    @Override
    public void jar() throws Exception {
        math.jar();
    }

    @Override
    public void javadoc() throws Exception {
        math.javadoc();
    }

    @Override
    public void jarSources() throws Exception {
        math.jarSources();
    }

    @Override
    public void jarJavadoc() throws Exception {
        math.jarJavadoc();
    }

    @Override
    public void dependencyTree() throws Exception {

        System.out.println("==== " + math.name() + " ====");
        math.dependencyTree();

        System.out.println("==== " + benchmark.name() + " ====");
        benchmark.dependencyTree();
    }

    @Override
    public void test() throws Exception {
        math.test();
    }
}
