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

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.dependencytrack.apiserver.model.Analysis;
import org.dependencytrack.apiserver.model.BomProcessingResponse;
import org.dependencytrack.apiserver.model.BomUploadRequest;
import org.dependencytrack.apiserver.model.ConfigProperty;
import org.dependencytrack.apiserver.model.CreateNotificationRuleRequest;
import org.dependencytrack.apiserver.model.CreateTeamRequest;
import org.dependencytrack.apiserver.model.CreateVulnerabilityRequest;
import org.dependencytrack.apiserver.model.Finding;
import org.dependencytrack.apiserver.model.NotificationPublisher;
import org.dependencytrack.apiserver.model.NotificationRule;
import org.dependencytrack.apiserver.model.Project;
import org.dependencytrack.apiserver.model.Team;
import org.dependencytrack.apiserver.model.UpdateNotificationRuleRequest;
import org.dependencytrack.apiserver.model.VulnerabilityPolicy;
import org.dependencytrack.apiserver.model.WorkflowState;
import org.dependencytrack.apiserver.model.WorkflowTokenResponse;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;

import java.util.List;
import java.util.UUID;

@Path("/api/v1")
@RegisterClientHeaders(ApiServerClientHeaderFactory.class)
public interface ApiServerClient {

    @POST
    @Path("/user/forceChangePassword")
    @Produces(MediaType.WILDCARD)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    void forcePasswordChange(@FormParam("username") final String username,
                             @FormParam("password") final String password,
                             @FormParam("newPassword") final String newPassword,
                             @FormParam("confirmPassword") final String confirmPassword);

    @POST
    @Path("/user/login")
    @Produces(MediaType.WILDCARD)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    String login(@FormParam("username") final String username,
                 @FormParam("password") final String password);

    @PUT
    @Path("/team")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Team createTeam(final CreateTeamRequest request);

    @POST
    @Path("/permission/{permission}/team/{uuid}")
    @Produces(MediaType.WILDCARD)
    @Consumes(MediaType.APPLICATION_JSON)
    Team addPermissionToTeam(@PathParam("uuid") final UUID teamUuid,
                             @PathParam("permission") final String permission);

    @PUT
    @Path("/bom")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    WorkflowTokenResponse uploadBom(final BomUploadRequest request);

    @GET
    @Path("/bom/token/{token}")
    @Produces(MediaType.WILDCARD)
    @Consumes(MediaType.WILDCARD)
    BomProcessingResponse isBomBeingProcessed(@PathParam("token") final String token);

    @PUT
    @Path("/vulnerability")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    void createVulnerability(final CreateVulnerabilityRequest request);

    @GET
    @Path("/finding/project/{uuid}")
    @Produces(MediaType.WILDCARD)
    @Consumes(MediaType.APPLICATION_JSON)
    List<Finding> getFindings(@PathParam("uuid") final UUID projectUuid, @QueryParam("suppressed") final boolean includeSuppressed);

    @GET
    @Path("/project/lookup")
    @Produces(MediaType.WILDCARD)
    @Consumes(MediaType.APPLICATION_JSON)
    Project lookupProject(@QueryParam("name") final String name, @QueryParam("version") final String version);

    @GET
    @Path("/notification/publisher")
    @Produces(MediaType.WILDCARD)
    @Consumes(MediaType.APPLICATION_JSON)
    List<NotificationPublisher> getAllNotificationPublishers();

    @PUT
    @Path("/notification/rule")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    NotificationRule createNotificationRule(final CreateNotificationRuleRequest request);

    @POST
    @Path("/notification/rule")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    NotificationRule updateNotificationRule(final UpdateNotificationRuleRequest request);

    @POST
    @Path("/configProperty")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    ConfigProperty updateConfigProperty(final ConfigProperty configProperty);

    @GET
    @Path("/policy/vulnerability")
    @Produces(MediaType.WILDCARD)
    @Consumes(MediaType.APPLICATION_JSON)
    List<VulnerabilityPolicy> getAllVulnerabilityPolicies();

    @POST
    @Path("/policy/vulnerability/bundle/sync")
    @Produces(MediaType.WILDCARD)
    @Consumes(MediaType.APPLICATION_JSON)
    WorkflowTokenResponse triggerVulnerabilityPolicyBundleSync();

    @GET
    @Path("/analysis")
    @Produces(MediaType.WILDCARD)
    @Consumes(MediaType.APPLICATION_JSON)
    Analysis getAnalysis(@QueryParam("project") final UUID projectUuid, @QueryParam("component") final UUID componentUuid,
                         @QueryParam("vulnerability") final UUID vulnUuid);

    @POST
    @Path("/finding/project/{uuid}/analyze")
    @Produces(MediaType.WILDCARD)
    @Consumes(MediaType.APPLICATION_JSON)
    WorkflowTokenResponse analyzeProject(@PathParam("uuid") final UUID projectUuid);

    @GET
    @Path("/workflow/token/{token}/status")
    List<WorkflowState> getWorkflowStatus(@PathParam("token") final String token);

}
