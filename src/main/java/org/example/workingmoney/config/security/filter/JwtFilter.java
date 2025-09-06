package org.example.workingmoney.config.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.workingmoney.config.security.jwt.AuthTokenUtil;
import org.example.workingmoney.config.security.jwt.JwtType;
import org.example.workingmoney.domain.entity.User;
import org.example.workingmoney.repository.user.UserEntity;
import org.example.workingmoney.service.user.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final AuthTokenUtil authTokenUtil;
    private final String[] allowedPath;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        if ((authorization == null || !authorization.startsWith("Bearer ")) && Arrays.asList(allowedPath).contains(request.getRequestURI())) {

            filterChain.doFilter(request, response);
            return;
        }

        if (authorization == null || !authorization.startsWith("Bearer ")) {

            PrintWriter writer = response.getWriter();
            writer.println("wrong access");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String accessToken = authorization.split(" ")[1];

        if (authTokenUtil.isExpired(accessToken)) {

            PrintWriter writer = response.getWriter();
            writer.println("access token expired");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Optional<JwtType> jwtType = authTokenUtil.getCategory(accessToken);

        if (jwtType.isEmpty() || jwtType.get() != JwtType.ACCESS) {

            PrintWriter writer = response.getWriter();
            writer.println("invalid access token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String username = authTokenUtil.getUsername(accessToken);
        String role = authTokenUtil.getRole(accessToken);

        CustomUserDetails customUserDetails = new CustomUserDetails(username, null, role);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customUserDetails,
                null,
                customUserDetails.getAuthorities()
        );
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        filterChain.doFilter(request, response);
    }
}
