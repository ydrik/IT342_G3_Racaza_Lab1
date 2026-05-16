package com.annimemo.security;

import com.annimemo.model.Role;
import com.annimemo.model.User;
import com.annimemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Custom OAuth2 User Service
 * FRS Feature 4.2: Google OAuth Login
 * Handles user authentication via OAuth2 providers (Google)
 */
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        // Get provider (e.g., "google")
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String oauthId = oauth2User.getAttribute("sub"); // Google's user ID
        String email = oauth2User.getAttribute("email");
        String givenName = oauth2User.getAttribute("given_name");
        String familyName = oauth2User.getAttribute("family_name");

        // Check if user exists by OAuth ID
        Optional<User> existingUser = userRepository.findByOauthProviderAndOauthId(provider, oauthId);

        User user;
        if (existingUser.isEmpty()) {
            // Create new user
            user = new User();
            user.setOauthProvider(provider);
            user.setOauthId(oauthId);
            user.setEmail(email);
            user.setUsername(email); // Use email as username for OAuth users
            user.setFirstName(givenName != null ? givenName : "");
            user.setLastName(familyName != null ? familyName : "");
            user.setPassword(""); // No password for OAuth users
            user.setRole(Role.ROLE_USER);
            user.setActive(true);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            
            userRepository.save(user);
        } else {
            user = existingUser.get();
            // Update last login time
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        }

        return new CustomOAuth2User(oauth2User, user);
    }
}
