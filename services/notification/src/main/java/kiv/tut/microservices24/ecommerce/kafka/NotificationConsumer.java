package kiv.tut.microservices24.ecommerce.kafka;

import kiv.tut.microservices24.ecommerce.dto.OrderConfirmation;
import kiv.tut.microservices24.ecommerce.dto.PaymentConfirmation;
import kiv.tut.microservices24.ecommerce.email.EmailService;
import kiv.tut.microservices24.ecommerce.model.Notification;
import kiv.tut.microservices24.ecommerce.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static kiv.tut.microservices24.ecommerce.model.NotificationType.ORDER_CONFIRMATION;
import static kiv.tut.microservices24.ecommerce.model.NotificationType.PAYMENT_CONFIRMATION;

@Service
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("unused")
public class NotificationConsumer {

    private final NotificationRepository repository;
    private final EmailService emailService;

    @KafkaListener(topics = "payment-topic")
    public void consumePaymentSuccessNotification(PaymentConfirmation paymentConfirmation) {
        log.info(String.format("IHOR:: Consuming the message from Payment Topic %s", paymentConfirmation));
        // store notification to the mongoDB
        repository.save(
                Notification.builder()
                        .type(PAYMENT_CONFIRMATION)
                        .dateTime(LocalDateTime.now())
                        .paymentConfirmation(paymentConfirmation)
                        .build()
        );
        // send email
        emailService.sendPaymentSuccessEmail(
                paymentConfirmation.customerEmail(),
                paymentConfirmation.customerFirstName() + " " + paymentConfirmation.customerLastName(),
                paymentConfirmation.amount(),
                paymentConfirmation.orderReference()
        );
    }

    @KafkaListener(topics = "order-topic")
    // store notification to the mongoDB
    public void consumeOrderNotification(OrderConfirmation orderConfirmation) {
        log.info(String.format("IHOR:: Consuming the message from Payment Topic %s", orderConfirmation));
        repository.save(
                Notification.builder()
                        .type(ORDER_CONFIRMATION)
                        .dateTime(LocalDateTime.now())
                        .orderConfirmation(orderConfirmation)
                        .build()
        );
        // send email
        emailService.sendOrderConfirmationEmail(
                orderConfirmation.customer().email(),
                orderConfirmation.customer().firstName() + " " + orderConfirmation.customer().lastName(),
                orderConfirmation.totalAmount(),
                orderConfirmation.orderReference(),
                orderConfirmation.products()
        );
    }
}
