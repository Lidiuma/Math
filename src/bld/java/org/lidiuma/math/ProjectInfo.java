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

import rife.bld.publish.PublishScm;
import static java.lang.String.format;
import static org.lidiuma.math.PublishUtil.GITHUB_DOMAIN;
import static org.lidiuma.math.PublishUtil.GITHUB_URL;

public record ProjectInfo(String organization, String projectName, PublishScm scm) {

    public static ProjectInfo github(String organization, String projectName) {

        final String project = format("%s/%s/%s", GITHUB_URL, organization, projectName);
        final var scm = new PublishScm()
                .connection(format("scm:git:%s.git", project))
                .developerConnection(format("scm:git:git@%s:%s/%s.git", GITHUB_DOMAIN, organization, projectName))
                .url(project);

        return new ProjectInfo(organization, projectName, scm);
    }

    public String url() {
        return scm().url();
    }
}
