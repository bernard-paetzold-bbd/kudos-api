package com.bbdgrads.kudos_api.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtAuthentication extends AbstractAuthenticationToken {
    private final UserDetails principal; // Need userDetails object here, make a principal

    public JwtAuthentication(UserDetails principal) {
        super(principal.getAuthorities()); // Get authorities of user, their "permissions"
        this.principal = principal;
        setAuthenticated(true); // Set them to be authenticated
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}

