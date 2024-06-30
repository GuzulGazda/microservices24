package kiv.tut.microservices24.ecommerce.service;

import kiv.tut.microservices24.ecommerce.exception.ProductNotFoundException;
import kiv.tut.microservices24.ecommerce.exception.ProductPurchaseException;
import kiv.tut.microservices24.ecommerce.model.Product;
import kiv.tut.microservices24.ecommerce.model.ProductMapper;
import kiv.tut.microservices24.ecommerce.model.ProductPurchaseRequest;
import kiv.tut.microservices24.ecommerce.model.ProductPurchaseResponse;
import kiv.tut.microservices24.ecommerce.model.ProductRequest;
import kiv.tut.microservices24.ecommerce.model.ProductResponse;
import kiv.tut.microservices24.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ProductService {

    public static final String DOES_NOT_EXISTS_IN_THE_STORE = "One or more products does not exists in the store.";
    public static final String NO_PRODUCT_FOUND_WITH_THE_PROVIDED_ID_S = "No product found with the provided id:: %s";
    private final ProductRepository repository;
    private final ProductMapper mapper;

    public Integer createProduct(ProductRequest request) {
        Product product = mapper.toProduct(request);
        return repository.save(product).getId();
    }

    public List<ProductResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toProductResponse)
                .collect(toList());
    }

    @Transactional(rollbackFor = ProductPurchaseException.class)
    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> request) {
        var productIds = request
                .stream()
                .map(ProductPurchaseRequest::productId)
                .toList();
        var storeProducts = repository.findAllByIdInOrderById(productIds);

        if (productIds.size() != storeProducts.size()) {
            throw new ProductPurchaseException(DOES_NOT_EXISTS_IN_THE_STORE);
        }

        var sortedRequest = request
                .stream()
                .sorted(Comparator.comparingInt(ProductPurchaseRequest::productId))
                .toList();

        var purchaseProducts = new ArrayList<ProductPurchaseResponse>();
        for (int i = 0; i < sortedRequest.size(); i++) {
            var requestedProduct = sortedRequest.get(i);
            var storeProduct = storeProducts.get(i);
            if (requestedProduct.quantity() > storeProduct.getQuantity()) {
                throw new ProductPurchaseException(
                        String.format(
                                "Insufficient quantity of product:: %s",
                                storeProduct.getName()
                        ));
            }
            var newQuantity = storeProduct.getQuantity() - requestedProduct.quantity();
            storeProduct.setQuantity(newQuantity);
            repository.save(storeProduct);
            purchaseProducts.add(
                    mapper.toProductPurchaseResponse(storeProduct, requestedProduct.quantity())
            );
        }
        return purchaseProducts;
    }

    public ProductResponse findById(Integer productId) {
        return repository.findById(productId)
                .map(mapper::toProductResponse)
                .orElseThrow(() -> new ProductNotFoundException(
                        format(NO_PRODUCT_FOUND_WITH_THE_PROVIDED_ID_S, productId)
                ));
    }
}
