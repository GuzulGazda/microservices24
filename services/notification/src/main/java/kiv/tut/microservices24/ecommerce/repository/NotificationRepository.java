package kiv.tut.microservices24.ecommerce.repository;

import kiv.tut.microservices24.ecommerce.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
}
