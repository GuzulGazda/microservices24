package kiv.tut.microservices24.ecommerce.service;

import kiv.tut.microservices24.ecommerce.dto.OrderLineRequest;
import kiv.tut.microservices24.ecommerce.model.OrderLine;
import kiv.tut.microservices24.ecommerce.model.OrderLineMapper;
import kiv.tut.microservices24.ecommerce.model.OrderLineResponse;
import kiv.tut.microservices24.ecommerce.repository.OrderLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class OrderLineService {

    private final OrderLineRepository repository;
    private final OrderLineMapper mapper;

    public Integer saveOrderLine(OrderLineRequest orderLineRequest) {
        OrderLine orderLine = mapper.toOrderLine(orderLineRequest);
        return repository.save(orderLine).getId();
    }

    public List<OrderLineResponse> findByOrderId(Integer orderId) {
        return repository.findAllByOrderId(orderId)
                .stream()
                .map(mapper::toOrderLineResponse)
                .collect(toList());
    }
}
