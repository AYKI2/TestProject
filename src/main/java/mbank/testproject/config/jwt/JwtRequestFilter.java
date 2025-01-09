package mbank.testproject.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String VALID_USERNAME = "admin";
    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    public JwtRequestFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader(AUTH_HEADER);
        if (token == null || !token.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }
        token = token.substring(BEARER_PREFIX.length());
        try{
            if (jwtUtils.validateToken(token, VALID_USERNAME)) {
                logger.info("Токен действителен для пользователя: {}",VALID_USERNAME);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(VALID_USERNAME, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } else {
                logger.warn("Неправильный токен: {}", token);
                sendXmlErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Неправильный токен!");
            }
        }catch (Exception e){
            logger.warn("Токен отсутствует или недействителен!");
            sendXmlErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Токен отсутствует или недействителен!");
        }
    }

    private void sendXmlErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/xml; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        String xmlResponse = String.format(
                "<errorResponse><code>%d</code><message>%s</message></errorResponse>",
                statusCode, message
        );
        response.getWriter().write(xmlResponse);
        response.getWriter().flush();
    }
}
