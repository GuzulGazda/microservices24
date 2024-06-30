package kiv.tut.microservices24.ecommerce.controller;

import jakarta.validation.Valid;
import kiv.tut.microservices24.ecommerce.dto.PaymentRequest;
import kiv.tut.microservices24.ecommerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SuppressWarnings("unused")
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService service;

    @PostMapping
    public ResponseEntity<Integer> createPayment(
            @RequestBody @Valid PaymentRequest request) {
        log.info("IHOR:: CreatePayment: {}", request);
        return ResponseEntity.ok(service.createPayment(request));
    }
}
