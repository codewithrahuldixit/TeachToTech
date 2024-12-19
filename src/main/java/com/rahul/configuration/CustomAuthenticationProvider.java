package com.rahul.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.rahul.service.UserDetailService;


@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserDetailService userDetailService;

    public CustomAuthenticationProvider(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Get email and contact (mobile number) from the token
        String identifier = authentication.getName(); // identifier = email|contact
        String[] parts = identifier.split("\\|");
        if (parts.length != 2) {
            throw new AuthenticationException("Invalid identifier format") {};
        }

        String email = parts[0];
        String contact = parts[1];

  UserDetails userDetails = userDetailService.loadUserByUsername(identifier);

        // Return the authentication token (with roles, authorities, etc.)
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
