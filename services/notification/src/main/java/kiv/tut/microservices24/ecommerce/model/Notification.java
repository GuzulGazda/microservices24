package kiv.tut.microservices24.ecommerce.model;

import kiv.tut.microservices24.ecommerce.dto.OrderConfirmation;
import kiv.tut.microservices24.ecommerce.dto.PaymentNotificationRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document
public class Notification {
    private String id;
    private NotificationType type;
    private LocalDateTime dateTime;
    private OrderConfirmation orderConfirmation;
    private PaymentNotificationRequest paymentConfirmation;
}
