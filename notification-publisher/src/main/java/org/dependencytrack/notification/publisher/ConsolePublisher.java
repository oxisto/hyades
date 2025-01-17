/*
 * This file is part of Dependency-Track.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) OWASP Foundation. All Rights Reserved.
 */
package org.dependencytrack.notification.publisher;

import io.pebbletemplates.pebble.PebbleEngine;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.json.JsonObject;
import org.dependencytrack.persistence.repository.ConfigPropertyRepository;
import org.dependencytrack.proto.notification.v1.Level;
import org.dependencytrack.proto.notification.v1.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintStream;

@ApplicationScoped
@Startup // Force bean creation even though no direct injection points exist
public class ConsolePublisher implements Publisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsolePublisher.class);

    private final PebbleEngine pebbleEngine;
    private final ConfigPropertyRepository configPropertyRepository;

    @Inject
    public ConsolePublisher(@Named("pebbleEnginePlainText") final PebbleEngine pebbleEngine,
                            final ConfigPropertyRepository configPropertyRepository){
        this.pebbleEngine = pebbleEngine;
        this.configPropertyRepository = configPropertyRepository;
    }

    public void inform(final PublishContext ctx, final Notification notification, final JsonObject config) throws Exception {
        final String content;
        try {
            content = prepareTemplate(notification, getTemplate(config), configPropertyRepository, config);
        } catch (IOException | RuntimeException e) {
            LOGGER.error("Failed to prepare notification content (%s)".formatted(ctx), e);
            return;
        }
        final PrintStream ps;
        if (notification.getLevel() == Level.LEVEL_ERROR) {
            ps = System.err;
        } else {
            ps = System.out;
        }
        ps.println(content);
    }

    @Override
    public PebbleEngine getTemplateEngine() {
        return pebbleEngine;
    }
}
