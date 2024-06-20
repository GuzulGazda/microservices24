package kiv.tut.microservices24.ecommerce.service;

import kiv.tut.microservices24.ecommerce.dto.OrderLineRequest;
import kiv.tut.microservices24.ecommerce.model.OrderLine;
import kiv.tut.microservices24.ecommerce.model.OrderLineMapper;
import kiv.tut.microservices24.ecommerce.repository.OrderLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderLineService {

    private final OrderLineRepository repository;
    private final OrderLineMapper mapper;

    public Integer saveOrderLine(OrderLineRequest orderLineRequest) {
        OrderLine orderLine = mapper.toOrderLine(orderLineRequest);
        return repository.save(orderLine).getId();
    }
}
