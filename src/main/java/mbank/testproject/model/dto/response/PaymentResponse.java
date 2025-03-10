package mbank.testproject.model.dto.response;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("paymentResponse")
public record PaymentResponse(
        String status,
        String message
) {
}
