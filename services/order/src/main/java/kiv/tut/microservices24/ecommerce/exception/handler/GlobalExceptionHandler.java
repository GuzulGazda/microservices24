package kiv.tut.microservices24.ecommerce.exception.handler;

import kiv.tut.microservices24.ecommerce.exception.BusinessExceptionNotFound;
import kiv.tut.microservices24.ecommerce.exception.BusinessExceptionWrongPurchase;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@SuppressWarnings("unused")
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessExceptionNotFound.class)
    public ResponseEntity<String> hadle(BusinessExceptionNotFound exception) {
        return ResponseEntity
                .status(NOT_FOUND)
                .body(exception.getMsg());
    }

    @ExceptionHandler(BusinessExceptionWrongPurchase.class)
    public ResponseEntity<String> hadle(BusinessExceptionWrongPurchase exception) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(exception.getMsg());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> hadle(MethodArgumentNotValidException exception) {
        var errors = new HashMap<String, String>();
        exception.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var fieldName = ((FieldError) error).getField();
                    var errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(new ErrorResponse(errors));
    }
}
