package kiv.tut.microservices24.ecommerce.model;

import kiv.tut.microservices24.ecommerce.customer.CustomerResponse;
import kiv.tut.microservices24.ecommerce.product.ProductPurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<ProductPurchaseResponse> products

) {
}
