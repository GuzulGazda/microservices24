package kiv.tut.microservices24.ecommerce.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessExceptionWrongPurchase extends RuntimeException {
    private final String msg;
}
