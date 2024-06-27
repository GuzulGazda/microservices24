package kiv.tut.microservices24.ecommerce.dto;

import kiv.tut.microservices24.ecommerce.model.Customer;
import kiv.tut.microservices24.ecommerce.model.PaymentMethod;
import kiv.tut.microservices24.ecommerce.model.Product;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        Customer customer,
        List<Product> products) {
}
