package com.proman.TaskAllocation.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtTokenValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String jwt = request.getHeader(JwtConstant.JWT_HEADER);

        // Check if JWT token is present and starts with "Bearer "
        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7); // Remove "Bearer " prefix

            try {
                // Ensure the secret key matches the one used in token generation
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECREATE_KEY.getBytes());

                // Parse the JWT claims using the provided secret key
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                // Retrieve email and authorities claims
                String email = claims.get("email", String.class);
                String authorities = claims.get("authorities", String.class);

                // Check if authorities are available
                List<GrantedAuthority> auths = authorities != null
                        ? AuthorityUtils.commaSeparatedStringToAuthorityList(authorities)
                        : AuthorityUtils.NO_AUTHORITIES;

                // Set the authentication context
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auths);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                // Log and throw a BadCredentialsException if token parsing fails
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is not valid");
                return; // Stop further processing
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
