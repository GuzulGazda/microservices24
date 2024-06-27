package kiv.tut.microservices24.ecommerce.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "payment-service",
        url = "http://localhost:8060/api/v1/payment"
)
public interface PaymentClient {
    @PostMapping
    void requestOrderPayment(@RequestBody PaymentRequest request);
}
