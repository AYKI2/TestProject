package mbank.testproject.service.impl;

import mbank.testproject.config.jwt.JwtUtils;
import mbank.testproject.model.dto.request.LoginRequest;
import mbank.testproject.model.dto.response.LoginResponse;
import mbank.testproject.service.AuthService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final JwtUtils jwtUtils;

    public AuthServiceImpl(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        if ("admin".equals(request.username()) && "admin123".equals(request.password())){
            return new LoginResponse(jwtUtils.generateToken(request.username()),request.username());
        }
        throw new AccessDeniedException("Неверный логин или пароль!");
    }
}
