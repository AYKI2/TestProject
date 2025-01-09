package mbank.testproject.model.dto.request;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("loginRequest")
public record LoginRequest(
        String username,
        String password
) {
}
