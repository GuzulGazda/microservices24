package kiv.tut.microservices24.ecommerce.repository;

import kiv.tut.microservices24.ecommerce.model.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Integer> {
}
