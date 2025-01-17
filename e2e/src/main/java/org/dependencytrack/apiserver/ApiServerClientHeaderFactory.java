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
package org.dependencytrack.apiserver;

import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

public class ApiServerClientHeaderFactory implements ClientHeadersFactory {

    private static String bearerToken;
    private static String apiKey;

    @Override
    public MultivaluedMap<String, String> update(final MultivaluedMap<String, String> incomingHeaders,
                                                 final MultivaluedMap<String, String> clientOutgoingHeaders) {
        final var headers = new MultivaluedHashMap<String, String>();
        if (apiKey != null) {
            headers.putSingle("X-Api-Key", apiKey);
        } else if (bearerToken != null) {
            headers.putSingle("Authorization", "Bearer " + bearerToken);
        }
        return headers;
    }

    public static void setBearerToken(final String bearerToken) {
        ApiServerClientHeaderFactory.bearerToken = bearerToken;
    }

    public static void setApiKey(final String apiKey) {
        ApiServerClientHeaderFactory.apiKey = apiKey;
    }

    public static void reset() {
        ApiServerClientHeaderFactory.bearerToken = null;
        ApiServerClientHeaderFactory.apiKey = null;
    }

}
