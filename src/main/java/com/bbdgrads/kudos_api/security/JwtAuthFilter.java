package com.bbdgrads.kudos_api.security;

import com.bbdgrads.kudos_api.model.User;
import com.bbdgrads.kudos_api.service.JwtService;
import com.bbdgrads.kudos_api.service.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserServiceImpl userService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain fc)
            throws ServletException, IOException {
        String token = extractToken(req); // Get the token from the request.

        if (token == null || token.isBlank()) {
            fc.doFilter(req, res);
            return;
        }

        Optional<String> verifyJwt = jwtService.verifyApiRequest(token);
        if (verifyJwt.isPresent()) {
            String googleId = verifyJwt.get();
            Optional<User> userOptional = userService.findByUserGoogleId(googleId);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                UserPrincipal principal = new UserPrincipal(user); // Create a UserPrincipal class
                JwtAuthentication authentication = new JwtAuthentication(principal); // Create an Authentication object
                SecurityContextHolder.getContext().setAuthentication(authentication); // Sets the authentication
                                                                                      // context, saying "this guy is
                                                                                      // authorized!"
            }
        }
        fc.doFilter(req, res);

    }

    public String extractToken(HttpServletRequest req) {
        String authHeader = req.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
