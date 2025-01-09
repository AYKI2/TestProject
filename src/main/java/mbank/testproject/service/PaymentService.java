package mbank.testproject.service;

import mbank.testproject.model.dto.request.PaymentRequest;
import mbank.testproject.model.dto.response.PaymentResponse;

public interface PaymentService {
    PaymentResponse handlePayment(PaymentRequest request);
}
