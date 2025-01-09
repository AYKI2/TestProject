package mbank.testproject.controller;

import mbank.testproject.model.dto.request.PaymentRequest;
import mbank.testproject.model.dto.response.PaymentResponse;
import mbank.testproject.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping(consumes = "application/json", produces = "application/xml")
    public ResponseEntity<PaymentResponse> handlePayment(@RequestBody PaymentRequest paymentRequest) {
        return ResponseEntity.ok(paymentService.handlePayment(paymentRequest));
    }
}
