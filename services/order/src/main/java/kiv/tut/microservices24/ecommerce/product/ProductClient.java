package kiv.tut.microservices24.ecommerce.product;

import kiv.tut.microservices24.ecommerce.exception.BusinessExceptionNotFound;
import kiv.tut.microservices24.ecommerce.exception.BusinessExceptionWrongPurchase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@RequiredArgsConstructor
public class ProductClient {
    public static final String AN_ERROR_OCCURRED_DURING_THE_PRODUCT_PURCHASE = "An error occurred during the product purchase";
    private final RestTemplate restTemplate;
    @Value("${application.config.product-url}")
    private String productUrl;

    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(CONTENT_TYPE, APPLICATION_JSON_VALUE);

        HttpEntity<List<ProductPurchaseRequest>> requestEntity = new HttpEntity<>(requestBody, headers);
        ParameterizedTypeReference<List<ProductPurchaseResponse>> responseType =
                new ParameterizedTypeReference<>() {
                };
        try {
            ResponseEntity<List<ProductPurchaseResponse>> responseEntity = restTemplate.exchange(
                    productUrl + "/purchase",
                    POST,
                    requestEntity,
                    responseType
            );
            if (responseEntity.getStatusCode().isError()) {
                // TODO use another exception
                throw new BusinessExceptionNotFound("An error occurred during the product purchase:: " + responseEntity.getStatusCode());
            }
            return responseEntity.getBody();
        } catch (RuntimeException ex) {
            throw new BusinessExceptionWrongPurchase(AN_ERROR_OCCURRED_DURING_THE_PRODUCT_PURCHASE);
        }
    }
}

