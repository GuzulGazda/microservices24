package kiv.tut.microservices24.ecommerce.model;

import kiv.tut.microservices24.ecommerce.dto.OrderLineRequest;
import org.springframework.stereotype.Service;

@Service
public class OrderLineMapper {
    public OrderLine toOrderLine(OrderLineRequest request) {
        if (request == null) {
            return null;
        }
        return OrderLine.builder()
//                .id(request.id())
                .order(
                        Order.builder()
                                .id(request.orderId())
                                .build()
                )
                .productId(request.productId())
                .quantity(request.quantity())
                .build();
    }

    public OrderLineResponse toOrderLineResponse(OrderLine orderLine) {
        return new OrderLineResponse(
                orderLine.getId(),
                orderLine.getOrder().getId(),
                orderLine.getProductId(),
                orderLine.getQuantity()
        );
    }
}
