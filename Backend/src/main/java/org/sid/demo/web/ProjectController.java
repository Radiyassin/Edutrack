package org.sid.demo.web;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.sid.demo.DTO.ProjectDto;
import org.sid.demo.DTO.RegisterProjectRequest;
import org.sid.demo.DTO.UpdateProjectRequest;
import org.sid.demo.DTO.UserDto;
import org.sid.demo.Exception.DuplicateProjectException;
import org.sid.demo.Exception.MemberAlreadyExistsException;
import org.sid.demo.Exception.MemberNotFoundException;
import org.sid.demo.Exception.UserAlreadyInAProjectException;
import org.sid.demo.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/projects")
@CrossOrigin(origins = "http://localhost:8080",
        allowedHeaders = "*",
        allowCredentials = "true",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class ProjectController {
    private final ProjectService projectService;
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping
    public Iterable<ProjectDto> getAllProjects(
            @RequestParam(required = false, defaultValue = "", name = "sort") String sortBy
    ) {

        return projectService.getAllProjects(sortBy);
    }

    @GetMapping("/{id}")
    public ProjectDto getProject(@PathVariable Long id ){
        return projectService.getProject(id);

    }

    @GetMapping("/{id}/members")
    public Iterable<UserDto> getProjectMembers(
            @PathVariable long id,
            @RequestParam(required = false, defaultValue = "", name = "sort") String sortBy)
    {
        return projectService.getProjectMembers(id,sortBy);
    }


    @PostMapping()
    public ResponseEntity<?> registerProject (
            @Valid @RequestBody RegisterProjectRequest request,
            @AuthenticationPrincipal OAuth2User principal
           ) {

        System.out.println("Amine");
        String email = principal.getAttribute("email");
        ProjectDto projectDto = projectService.registerProject(request, email);

        return ResponseEntity.ok(projectDto);
    }


    @PutMapping("/{id}")
    public ProjectDto updateProject(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody UpdateProjectRequest request ) {

        return projectService.updateProject(id,request);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{projectId}/members/{userId}")
    public ResponseEntity<ProjectDto> addMemberToProject(
            @PathVariable Long projectId,
            @PathVariable String userId) {
        ProjectDto projectDto = projectService.addMemberToProject(projectId, userId);
        return ResponseEntity.ok(projectDto);
    }

    @DeleteMapping("/{projectId}/members/{userId}")
    public ResponseEntity<?> removeMemberFromProject(
            @PathVariable Long projectId,
            @PathVariable String userId) {
        projectService.removeMemberFromProject(projectId, userId);
        return ResponseEntity.noContent().build();
    }


    @ExceptionHandler(UserAlreadyInAProjectException.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyInAProject() {
        return ResponseEntity.badRequest().body(
                Map.of("user", "user is already in another project.")
        );
    }

    private String getGitHubEmail(OAuth2User oauth2User) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
                OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                        oauthToken.getAuthorizedClientRegistrationId(),
                        oauthToken.getName()
                );

                if (client != null) {
                    String accessToken = client.getAccessToken().getTokenValue();

                    RestTemplate restTemplate = new RestTemplate();
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Authorization", "token " + accessToken);
                    headers.set("Accept", "application/vnd.github.v3+json");
                    headers.set("User-Agent", "EduTrack-App");

                    try {
                        ResponseEntity<Map[]> response = restTemplate.exchange(
                                "https://api.github.com/user/emails",
                                HttpMethod.GET,
                                new HttpEntity<>(headers),
                                Map[].class
                        );

                        if (response.getBody() != null && response.getBody().length > 0) {
                            for (Map<String, Object> emailInfo : response.getBody()) {
                                Boolean primary = (Boolean) emailInfo.get("primary");
                                Boolean verified = (Boolean) emailInfo.get("verified");
                                String email = (String) emailInfo.get("email");

                                if (Boolean.TRUE.equals(primary) && Boolean.TRUE.equals(verified)) {
                                    return email;
                                }
                            }

                            for (Map<String, Object> emailInfo : response.getBody()) {
                                Boolean verified = (Boolean) emailInfo.get("verified");
                                if (Boolean.TRUE.equals(verified)) {
                                    return (String) emailInfo.get("email");
                                }
                            }

                            if (response.getBody().length > 0) {
                                return (String) response.getBody()[0].get("email");
                            }
                        }

                    } catch (Exception e) {
                        try {
                            ResponseEntity<Map> userResponse = restTemplate.exchange(
                                    "https://api.github.com/user",
                                    HttpMethod.GET,
                                    new HttpEntity<>(headers),
                                    Map.class
                            );

                            if (userResponse.getBody() != null) {
                                String email = (String) userResponse.getBody().get("email");
                                if (email != null && !email.isEmpty()) {
                                    return email;
                                }

                                String login = (String) userResponse.getBody().get("login");
                                if (login != null) {
                                    return login + "@github.user";
                                }
                            }
                        } catch (Exception userException) {

                        }
                    }
                }
            }
        } catch (Exception e) {

        }

        String username = oauth2User.getAttribute("login");
        if (username != null) {
            return username + "@github.user";
        }

        return null;
    }
    @ExceptionHandler(DuplicateProjectException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateProject() {
        return ResponseEntity.badRequest().body(
                Map.of("title", "title is already registered.")
        );
    }


    @ExceptionHandler(MemberAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateMember() {
        return ResponseEntity.badRequest().body(
                Map.of("member", "member exists already")
        );
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleMemberNotFound() {
        return ResponseEntity.badRequest().body(
                Map.of("member", "member not found")
        );
    }



}
