package kiv.tut.microservices24.ecommerce.model;

import kiv.tut.microservices24.ecommerce.dto.PaymentNotificationRequest;
import kiv.tut.microservices24.ecommerce.dto.PaymentRequest;
import org.springframework.stereotype.Service;

@Service
public class PaymentMapper {

    public Payment toPayment(PaymentRequest request) {
        return Payment.builder()
                .id(request.id())
                .amount(request.amount())
                .paymentMethod(request.paymentMethod())
                .orderId(request.orderId())
                .build();
    }

    public PaymentNotificationRequest toPaymentNotificationRequest(PaymentRequest request) {
        return new PaymentNotificationRequest(
                request.orderReference(),
                request.amount(),
                request.paymentMethod(),
                request.customer().firstName(),
                request.customer().lastName(),
                request.customer().email()
        );
    }
}
