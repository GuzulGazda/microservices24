package kiv.tut.microservices24.ecommerce.model;

public record Customer(
        String id,
        String firstName,
        String lastName,
        String email
) {
}
