package kiv.tut.microservices24.ecommerce.controller;

import jakarta.validation.Valid;
import kiv.tut.microservices24.ecommerce.dto.OrderRequest;
import kiv.tut.microservices24.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SuppressWarnings("unused")
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService service;

    @PostMapping
    public ResponseEntity<Integer> createOrder(
            @RequestBody @Valid OrderRequest request) {
        log.info("IHOR:: CreateOrder: " + request.paymentMethod());
        return ResponseEntity.ok(service.createOrder(request));
    }

}
