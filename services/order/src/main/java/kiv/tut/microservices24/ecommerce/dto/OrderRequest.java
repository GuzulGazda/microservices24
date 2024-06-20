package kiv.tut.microservices24.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kiv.tut.microservices24.ecommerce.model.PaymentMethod;
import kiv.tut.microservices24.ecommerce.product.ProductPurchaseRequest;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(
        @NotNull(message = "Reference is required")
        String reference,
        @Positive(message = "Order amount should be positive")
        BigDecimal amount,
        @NotNull(message = "Payment method should be precised")
        PaymentMethod paymentMethod,
        @NotNull(message = "Customer ID should be present")
        @NotEmpty(message = "Customer ID should be present")
        @NotBlank(message = "Customer ID should be present")
        String customerId,
        @NotEmpty(message = "You should at least purchase one product")
        List<ProductPurchaseRequest> products
) {
}
