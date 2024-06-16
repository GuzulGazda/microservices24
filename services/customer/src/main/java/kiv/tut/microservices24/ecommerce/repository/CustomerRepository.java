package kiv.tut.microservices24.ecommerce.repository;

import kiv.tut.microservices24.ecommerce.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {
}
