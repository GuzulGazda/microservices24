package kiv.tut.microservices24.ecommerce.customer;

public record CustomerResponse
        (
                String id,
                String firstName,
                String lastName,
                String email
        ) {
}
