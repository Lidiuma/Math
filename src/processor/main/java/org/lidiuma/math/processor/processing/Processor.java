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

package org.lidiuma.math.processor.processing;

import org.lidiuma.math.processor.GenerateAlias;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;

public final class Processor extends AbstractProcessor {

    private Utility util;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(GenerateAlias.class.getName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        util = new Utility(processingEnv);
        if (roundEnv.processingOver()) return false;

        try { handleAlias(roundEnv);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private void handleAlias(RoundEnvironment roundEnv) throws IOException {

        final var elements = util.processingEnv().getElementUtils();
        final Map<String, AliasClassGenerator> aliases = new HashMap<>();
        roundEnv.getElementsAnnotatedWith(GenerateAlias.class).forEach(element -> {

            final String className = Objects.requireNonNull(element.getAnnotation(GenerateAlias.class)).className();
            if (className.isBlank()) throw new IllegalArgumentException("Class name cannot be empty. (GenerateAlias: " + className + ")");

            final String packageName = elements.getPackageOf(element).toString();
            if (!(element instanceof VariableElement ve)) throw new IllegalArgumentException("Invalid element.");

            aliases.computeIfAbsent(packageName + "." + className, _ -> AliasClassGenerator.of(util))
                    .packageName(packageName)
                    .className(className)
                    .methods(ve, util.methodsOfField(ve));
        });

        aliases.values().forEach(gen -> {
            try { gen.build();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }
}
