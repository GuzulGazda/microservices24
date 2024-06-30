package kiv.tut.microservices24.ecommerce.service;

import kiv.tut.microservices24.ecommerce.customer.CustomerClient;
import kiv.tut.microservices24.ecommerce.customer.CustomerResponse;
import kiv.tut.microservices24.ecommerce.dto.OrderLineRequest;
import kiv.tut.microservices24.ecommerce.dto.OrderRequest;
import kiv.tut.microservices24.ecommerce.exception.BusinessExceptionNotFound;
import kiv.tut.microservices24.ecommerce.kafka.OrderProducer;
import kiv.tut.microservices24.ecommerce.model.Order;
import kiv.tut.microservices24.ecommerce.model.OrderConfirmation;
import kiv.tut.microservices24.ecommerce.model.OrderMapper;
import kiv.tut.microservices24.ecommerce.model.OrderResponse;
import kiv.tut.microservices24.ecommerce.payment.PaymentClient;
import kiv.tut.microservices24.ecommerce.payment.PaymentRequest;
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
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    public static final String NO_CUSTOMER_EXISTS_WITH_THE_PROVIDED_ID_S =

            "Cannot create the order:: No customer exists with the provided Id:: %s !";
    public static final String NO_ORDER_EXISTS_WITH_THE_PROVIDED_ID_D =
            "Cannot find the order:: No order exists with the provided Id::  %d !";
    private final OrderRepository repository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderLineService orderLineService;
    private final OrderMapper mapper;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;

    @Transactional
    public Integer createOrder(OrderRequest request) {
        // check the customer - we use the Feign Client
        CustomerResponse customerResponse = getCustomerResponse(request.customerId());
        // purchase the product (product-microservice)
        List<ProductPurchaseResponse> productPurchaseResponses =
                productClient.purchaseProducts(request.products());

        // persist order
        Order order = repository.save(mapper.toOrder(request));

        // persist order-lines
        List<Integer> orderLineIds = persistOrderLines(request, order);

        // start the payment process
        paymentClient.requestOrderPayment(new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customerResponse
        ));

        // send the order confirmation notification (notification microservice)
        OrderConfirmation orderConfirmation = getOrderConfirmation(request, customerResponse, productPurchaseResponses);
        log.info(String.format("IHOR ORDER: Order service send OrderConfirmation: %s", orderConfirmation));

        orderProducer.sendOrderConfirmation(orderConfirmation);
        return order.getId();
    }

    private static OrderConfirmation getOrderConfirmation(
            OrderRequest request,
            CustomerResponse customerResponse,
            List<ProductPurchaseResponse> productPurchaseResponses
    ) {
        return new OrderConfirmation(
                request.reference(),
                request.amount(),
                request.paymentMethod(),
                customerResponse,
                productPurchaseResponses
        );
    }

    private CustomerResponse getCustomerResponse(String customerId) {
        log.info("IHOR:: Order Service is trying to get Customer with ID: " + customerId);
        //TODO if no customer with this id - throw Business exception!
        AtomicReference<CustomerResponse> customerResponseAtomicReference =
                new AtomicReference<>(this.customerClient.getById(customerId)
                        .orElseThrow(() -> new BusinessExceptionNotFound(
                                format(NO_CUSTOMER_EXISTS_WITH_THE_PROVIDED_ID_S, customerId)
                        )));
        log.info("IHOR:: Order Service successfully get the customer. FirstName:: " + customerResponseAtomicReference.get().firstName());
        return customerResponseAtomicReference.get();
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

    public List<OrderResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toOrderResponse)
                .collect(toList());
    }

    public OrderResponse findById(Integer orderId) {
        return repository.findById(orderId)
                .map(mapper::toOrderResponse)
                .orElseThrow(() -> new BusinessExceptionNotFound(
                        format(NO_ORDER_EXISTS_WITH_THE_PROVIDED_ID_D, orderId)
                ));
    }
}
