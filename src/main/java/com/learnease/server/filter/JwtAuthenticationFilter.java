package com.learnease.server.filter;

import com.learnease.server.dto.JWTDTO;
import com.learnease.server.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {
        System.out.println("HTTP METHOD = " + request.getMethod());

        String authHeader = request.getHeader("Authorization");

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            try {
                // If token is invalid or expired, an exception will be thrown
                Claims claims = jwtUtil.getClaims(token);

                // Extract standard & custom claims
                String email = claims.getSubject();
                String role = claims.get("role" , String.class);

                // Set authentication only if not already present
                // This avoids overriding an existing authentication
                if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){

                    //Extract UUID correctly (JWT stores it as String)
                    UUID userId = UUID.fromString(
                            claims.get("user_id", String.class));

                    // Create a lightweight principal object using JWT data
                    JWTDTO jwtdto = new JWTDTO(
                            userId,
                            email,
                            role
                    );

                    // Create Authentication token with authorities
                    UsernamePasswordAuthenticationToken authenticationToken
                            = new UsernamePasswordAuthenticationToken(
                            jwtdto,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + role))
                    );

                    // Step 5: Store Authentication in SecurityContext
                    // This marks the request as authenticated
                    SecurityContextHolder.getContext()
                            .setAuthentication(authenticationToken);
                }
            }catch (Exception e){

                // JWT is invalid, expired, or malformed
                // We log the error and allow the request to continue
                // Authorization rules will block access if required
                System.out.println("JWT validation failed: " + e.getMessage());
            }
        }
        //continue filter chain
        filterChain.doFilter(request , response);
    }
}














