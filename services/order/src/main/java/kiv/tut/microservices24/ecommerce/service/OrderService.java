package kiv.tut.microservices24.ecommerce.service;

import kiv.tut.microservices24.ecommerce.customer.CustomerClient;
import kiv.tut.microservices24.ecommerce.dto.OrderLineRequest;
import kiv.tut.microservices24.ecommerce.dto.OrderRequest;
import kiv.tut.microservices24.ecommerce.exception.BusinessException;
import kiv.tut.microservices24.ecommerce.model.Order;
import kiv.tut.microservices24.ecommerce.model.OrderMapper;
import kiv.tut.microservices24.ecommerce.product.ProductClient;
import kiv.tut.microservices24.ecommerce.product.ProductPurchaseRequest;
import kiv.tut.microservices24.ecommerce.product.ProductPurchaseResponse;
import kiv.tut.microservices24.ecommerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    public static final String NO_CUSTOMER_EXISTS_WITH_THE_PROVIDED_ID =
            "Cannot create the order:: No customer exists with the provided Id:: ";
    private final OrderRepository repository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderLineService orderLineService;
    private final OrderMapper mapper;

    @Transactional
    public Integer createOrder(OrderRequest request) {
        // check the customer - we use the Feign Client
        log.info("IHOR:: Order Service is trying to get Customer with ID: " + request.customerId());
        var customer = this.customerClient.getById(request.customerId())
                .orElseThrow(() -> new BusinessException(NO_CUSTOMER_EXISTS_WITH_THE_PROVIDED_ID + request.customerId()));
        log.info("IHOR:: Order Service successfully get the customer. FirstName:: " + customer.firstName());
        // purchase the product (product-microservice)
        List<ProductPurchaseResponse> productPurchaseResponses =
                productClient.purchaseProducts(request.products());

        // persist order
        Order order = repository.save(mapper.toOrder(request));

        // persist order-lines
        List<Integer> orderLineIds = persistOrderLines(request, order);

        // TODO: start the payment process

        // TODO: send the order confirmation notification (notification microservice)
        return null;
    }

    private List<Integer> persistOrderLines(OrderRequest request, Order order) {
        List<Integer> orderLineIds = new ArrayList<>();
        Integer orderLineId;
        for (ProductPurchaseRequest productPurchaseRequest : request.products()) {
            orderLineId = orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            order.getId(),
                            productPurchaseRequest.productId(),
                            productPurchaseRequest.quantity()
                    )
            );
            orderLineIds.add(orderLineId);
        }
        return orderLineIds;

    }
}
