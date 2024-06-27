package kiv.tut.microservices24.ecommerce.payment;

import kiv.tut.microservices24.ecommerce.customer.CustomerResponse;
import kiv.tut.microservices24.ecommerce.model.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,

        String orderReference,

        CustomerResponse customer

) {

}
