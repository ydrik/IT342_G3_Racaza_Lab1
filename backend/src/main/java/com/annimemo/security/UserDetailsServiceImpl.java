package com.annimemo.security;

import com.annimemo.model.User;
import com.annimemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

/**
 * UserDetailsService implementation
 * Loads user-specific data for Spring Security authentication
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String normalizedIdentifier = username == null ? "" : username.trim();
        String normalizedEmail = normalizedIdentifier.toLowerCase(Locale.ROOT);

        User user = userRepository.findByUsername(normalizedIdentifier)
                .or(() -> userRepository.findByEmailIgnoreCase(normalizedEmail))
                .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));

        return UserDetailsImpl.build(user);
    }
}
