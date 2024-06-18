package kiv.tut.microservices24.ecommerce.service;

import jakarta.validation.Valid;
import kiv.tut.microservices24.ecommerce.exception.CustomerNotFoundException;
import kiv.tut.microservices24.ecommerce.model.Customer;
import kiv.tut.microservices24.ecommerce.model.CustomerMapper;
import kiv.tut.microservices24.ecommerce.model.CustomerRequest;
import kiv.tut.microservices24.ecommerce.model.CustomerResponse;
import kiv.tut.microservices24.ecommerce.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;
    public String createCustomer(@Valid CustomerRequest request) {
        var customer = repository.save(mapper.toCustomer(request));
        return customer.getId();
    }

    public void updateCustomer(@Valid CustomerRequest request) {
        var customer = repository.findById(request.id())
                .orElseThrow(() -> new CustomerNotFoundException(
                    format("Cannot update customer: no customer found with the provided id:: %s", request.id())
                ));
        mergeCustomer(customer, request);
        repository.save(customer);
    }

    private void mergeCustomer(Customer customer, CustomerRequest request) {
        if(StringUtils.isNotBlank(request.firstName())){
            customer.setFirstName(request.firstName());
        }
        if(StringUtils.isNotBlank(request.lastName())){
            customer.setLastName(request.lastName());
        }
        if (StringUtils.isNotBlank(request.email())) {
            customer.setEmail(request.email());
        }
        if (request.address() != null) {
            customer.setAddress(request.address());
        }
    }

    public List<CustomerResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::fromCustomer)
                .collect(Collectors.toList());
    }

    public Boolean existsById(String customerId) {
        return repository.findById(customerId).isPresent();
    }

    public CustomerResponse getById(String customerId) {
        return repository.findById(customerId).map(mapper::fromCustomer)
                .orElseThrow(() -> new CustomerNotFoundException(
                        format("No customer found with the provided id:: %s", customerId)
                ));
    }

    public void deleteById(String customerId) {
        repository.deleteById(customerId);
    }
}
