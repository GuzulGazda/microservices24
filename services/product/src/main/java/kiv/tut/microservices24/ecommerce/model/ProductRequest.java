package kiv.tut.microservices24.ecommerce.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(
    @NotNull(message = "Product name is required")
    String name,
    @NotNull(message = "Product description is required")
    String description,
    @Positive(message = "Quantity should be positive")
    Double quantity,
    @Positive(message = "Price should be positive")
    BigDecimal price,
    @NotNull(message = "Product category is required")
    Integer categoryId
) {
}
