package org.guccikray.creditcardmanagesystem.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.guccikray.creditcardmanagesystem.model.AuthUser;
import org.guccikray.creditcardmanagesystem.service.UserDetailsServiceImpl;
import org.guccikray.creditcardmanagesystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String JWT_BEARER = "Bearer ";

    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public JwtRequestFilter(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTH_HEADER);
        String jwt = null;
        Long userId = null;

        if (Objects.nonNull(authHeader) && authHeader.startsWith(JWT_BEARER)) {
            jwt = authHeader.substring(JWT_BEARER.length());
            String userIdString = JwtUtil.extractUserId(jwt);
            userId = Long.parseLong(userIdString);
        }

        if (Objects.nonNull(userId) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            AuthUser authUser = (AuthUser) userDetailsService.loadUserByUserId(userId);

            if (JwtUtil.validateToken(jwt, userId)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    authUser,
                    null,
                    authUser.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);

            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
