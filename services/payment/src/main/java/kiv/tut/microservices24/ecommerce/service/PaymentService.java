package kiv.tut.microservices24.ecommerce.service;

import kiv.tut.microservices24.ecommerce.dto.PaymentNotificationRequest;
import kiv.tut.microservices24.ecommerce.dto.PaymentRequest;
import kiv.tut.microservices24.ecommerce.kafka.NotificationProducer;
import kiv.tut.microservices24.ecommerce.model.Payment;
import kiv.tut.microservices24.ecommerce.model.PaymentMapper;
import kiv.tut.microservices24.ecommerce.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final NotificationProducer notificationProducer;

    public Integer createPayment(PaymentRequest request) {
        Payment payment = repository.save(mapper.toPayment(request));
        PaymentNotificationRequest notificationRequest = mapper.toPaymentNotificationRequest(request);
        notificationProducer.sendNotification(notificationRequest);
        log.info("IHOR:: Notification is send to the Kafka: <{}>", notificationRequest);
        return payment.getId();
    }
}
