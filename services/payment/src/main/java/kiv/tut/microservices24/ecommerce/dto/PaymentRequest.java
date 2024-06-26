package kiv.tut.microservices24.ecommerce.dto;

import kiv.tut.microservices24.ecommerce.model.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        Integer id,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,

        String orderReference,

        Customer customer

) {

}
