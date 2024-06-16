package kiv.tut.microservices24.ecommerce.model;

public record CustomerResponse
        (
        String id,
        String firstName,
        String lastName,
        String email,
        Address address) {
}
