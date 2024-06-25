package kiv.tut.microservices24.ecommerce.model;

public record OrderLineResponse(
        Integer id,
        Integer orderId,
        Integer productId,
        double quantity
) {
}
