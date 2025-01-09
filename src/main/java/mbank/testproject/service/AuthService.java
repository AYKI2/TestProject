package mbank.testproject.service;

import mbank.testproject.model.dto.request.LoginRequest;
import mbank.testproject.model.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
