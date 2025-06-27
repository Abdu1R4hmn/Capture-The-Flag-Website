package com.example.springboot.config;

import com.example.springboot.Role.Role;
import com.example.springboot.User.User;
import com.example.springboot.User.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepo userRepo;
    private final RestTemplate restTemplate;

    public CustomOAuth2UserService(UserRepo userRepo, RestTemplate restTemplate) {
        this.userRepo = userRepo;
        this.restTemplate = restTemplate;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("CustomOAuth2UserService.loadUser called for: {}", registrationId);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("OAuth2 attributes: {}", oAuth2User.getAttributes());

        String email = getEmail(oAuth2User, registrationId, userRequest);
        String username = getUsername(oAuth2User, registrationId);

        log.info("Processing OAuth2 user: {} | {}", email, username);

        if (email == null) {
            log.error("Email not found from OAuth2 provider: {}", registrationId);
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("missing_email", "Email not found from OAuth2 provider", null)
            );
        }

        User user = userRepo.findByEmailIgnoreCase(email)
                .orElseGet(() -> {
                    log.info("Creating new user for email: {}", email);
                    User newUser = createNewUser(email, username);
                    log.info("New user created: {}", newUser);
                    return newUser;
                });

        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );

        return new DefaultOAuth2User(
                authorities,
                oAuth2User.getAttributes(),
                userRequest.getClientRegistration()
                        .getProviderDetails()
                        .getUserInfoEndpoint()
                        .getUserNameAttributeName()
        );
    }

    private String getEmail(OAuth2User oAuth2User, String registrationId, OAuth2UserRequest request) {
        if ("github".equals(registrationId)) {
            return fetchGitHubEmail(request.getAccessToken().getTokenValue());
        }
        String email = oAuth2User.getAttribute("email");
        if (email == null) {
            List<Map<String, Object>> emails = oAuth2User.getAttribute("emails");
            if (emails != null && !emails.isEmpty()) {
                email = (String) emails.get(0).get("value");
            }
        }
        return email;
    }

    private String fetchGitHubEmail(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.set("User-Agent", "SpringBoot");
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    "https://api.github.com/user/emails",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {}
            );

            log.info("GitHub email API response: {}", response.getStatusCode());

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().stream()
                        .filter(email -> Boolean.TRUE.equals(email.get("primary")))
                        .filter(email -> Boolean.TRUE.equals(email.get("verified")))
                        .map(email -> (String) email.get("email"))
                        .findFirst()
                        .orElse(null);
            }
            log.error("Failed to fetch GitHub emails: {}", response.getStatusCode());
        } catch (Exception e) {
            log.error("Failed to fetch GitHub email", e);
            if (e instanceof HttpClientErrorException ex) {
                log.error("GitHub API error response: {}", ex.getResponseBodyAsString());
            }
        }
        return null;
    }

    private String getUsername(OAuth2User oAuth2User, String registrationId) {
        if ("github".equals(registrationId)) {
            return oAuth2User.getAttribute("login");
        }
        String name = oAuth2User.getAttribute("name");
        if (name == null) {
            name = oAuth2User.getAttribute("given_name");
        }
        return name;
    }

    private User createNewUser(String email, String username) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setUsername(username);
        newUser.setRole(Role.ROLE_USER);
        newUser.setPassword(null);
        return userRepo.save(newUser);
    }
}