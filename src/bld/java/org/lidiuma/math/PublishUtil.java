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

import rife.bld.Project;
import rife.bld.publish.PublishDeveloper;
import rife.bld.publish.PublishInfo;
import rife.bld.publish.PublishLicense;
import java.util.ArrayList;
import static java.lang.String.format;
import static org.lidiuma.math.Math.GROUP_ID;
import static rife.bld.dependencies.Repository.CENTRAL_RELEASES;
import static rife.bld.dependencies.Repository.CENTRAL_SNAPSHOTS;
import static rife.bld.dependencies.Scope.compile;

public final class PublishUtil {

    public static String GITHUB_DOMAIN = "github.com";
    public static String GITHUB_URL = "https://" + GITHUB_DOMAIN;
    /* === LICENSES === */
    public static PublishLicense APACHE_V2_LICENSE = new PublishLicense()
            .name("The Apache License, Version 2.0")
            .url("https://www.apache.org/licenses/LICENSE-2.0.txt");
    /* === DEVELOPERS === */
    public static PublishDeveloper XASMEDY_DEV = makeDeveloperGithub("Xasmedy", "xasmedy@pm.me");

    public static PublishDeveloper makeDeveloperGithub(String developerName, String developerEmail) {
        return new PublishDeveloper()
                .id(developerName.toLowerCase())
                .name(developerName)
                .email(developerEmail)
                .url(format("%s/%s", GITHUB_URL, developerName));
    }

    /// Gradle does not support Maven 4 new types, so I'm forced to patch the dependencies to make them Maven 3 compatible.\
    /// This means making the dependency type `jar` instead of `modular-jar`.
    public static void patchDependencies(Project project) {
        final var dependencies = new ArrayList<>(project.scope(compile));
        project.scope(compile).clear();
        for (var dependency : dependencies) {
            final var fixed = project.dependency(dependency.groupId(), dependency.artifactId(), dependency.version(), dependency.classifier(), null);
            project.scope(compile).include(fixed);
        }
    }

    public static void publishCentralConfiguration(Project project, boolean centralSnapshot) {
        final var repository = centralSnapshot ? CENTRAL_SNAPSHOTS : CENTRAL_RELEASES;
        project.publishOperation().repositories(repository.withCredentials(
                project.property("sonatype.username"),
                project.property("sonatype.password")
        )).info(mathPublishInfo(project));
    }

    public static PublishInfo mathPublishInfo(Project project) {
        final var projectInfo = ProjectInfo.github("Lidiuma", project.name());
        return new PublishInfo()
                .groupId(GROUP_ID)
                .artifactId("math")
                .version(project.version())
                .name(project.name())
                .description("Math Library using Project Valhalla")
                .url(projectInfo.url())
                .developer(XASMEDY_DEV)
                .license(APACHE_V2_LICENSE)
                .scm(projectInfo.scm())
                .signKey(project.property("sign.key"))
                .signPassphrase(project.property("sign.passphrase"));
    }

    private PublishUtil() {}
}
