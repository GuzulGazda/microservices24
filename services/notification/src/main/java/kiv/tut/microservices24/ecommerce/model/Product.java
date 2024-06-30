package kiv.tut.microservices24.ecommerce.model;

import java.math.BigDecimal;

public record Product(
        Integer productId,
        String name,
        String description,
        Double quantity,
        BigDecimal price

) {
}
