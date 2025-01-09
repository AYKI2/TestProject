package mbank.testproject.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private static final String VALID_USERNAME = "admin";

    public JwtRequestFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader(AUTH_HEADER);

        if (token != null && token.startsWith(BEARER_PREFIX)) {
            token = token.substring(BEARER_PREFIX.length());

            if (jwtUtils.validateToken(token, VALID_USERNAME)) {
                filterChain.doFilter(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Неправильный токен!");
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Токен отсутствует или недействителен!");
        }
    }
}
