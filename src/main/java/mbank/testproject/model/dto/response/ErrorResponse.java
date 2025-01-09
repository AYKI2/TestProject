package mbank.testproject.model.dto.response;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("errorResponse")
public record ErrorResponse(
        int code,
        String message
) {
}
