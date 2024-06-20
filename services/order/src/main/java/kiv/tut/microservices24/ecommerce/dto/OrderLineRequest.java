package kiv.tut.microservices24.ecommerce.dto;

public record OrderLineRequest(
        Integer orderId,
        Integer productId,
        double quantity) {
}
