package kiv.tut.microservices24.ecommerce.controller;

import kiv.tut.microservices24.ecommerce.model.OrderLineResponse;
import kiv.tut.microservices24.ecommerce.service.OrderLineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@SuppressWarnings("unused")
@RequestMapping("/api/v1/order-line")
@RequiredArgsConstructor
@Slf4j
public class OrderLineController {
    private final OrderLineService service;

    @GetMapping("order/{order-id}")
    public ResponseEntity<List<OrderLineResponse>> findById(
            @PathVariable("order-id") Integer orderId) {
        return ResponseEntity.ok(service.findByOrderId(orderId));
    }
}
