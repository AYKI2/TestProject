package mbank.testproject.model.dto.request;

import java.math.BigDecimal;

public record PaymentRequest(
        String statusType,
        BigDecimal amount,
        String description
) {
}
