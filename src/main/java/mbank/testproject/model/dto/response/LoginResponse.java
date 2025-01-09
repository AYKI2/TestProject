package mbank.testproject.model.dto.response;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("loginResponse")
public record LoginResponse(
        String token,
        String username
) {
}
