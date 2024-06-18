package kiv.tut.microservices24.ecommerce.handler;

import java.util.Map;

public record ErrorResponse (
  Map<String, String> errors
){
}
