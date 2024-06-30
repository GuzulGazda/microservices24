package kiv.tut.microservices24.ecommerce.model;

import java.math.BigDecimal;

public record ProductResponse(
        Integer id,
        String name,
        String description,
        Double quantity,
        BigDecimal price,
        Integer categoryId,

        String categoryName,
        String categoryDescription

        ) {
}
