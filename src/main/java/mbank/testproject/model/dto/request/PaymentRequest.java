package mbank.testproject.model.dto.request;

import com.fasterxml.jackson.annotation.JsonRootName;

import java.math.BigDecimal;

@JsonRootName("paymentRequest")
public record PaymentRequest(
        String statusType,
        BigDecimal amount,
        String description
) {
}
