package io.learning.webscrapper.config;

import io.learning.webscrapper.domain.Employee;
import io.learning.webscrapper.repository.EmployeeRepository;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.RequestPredicates.GET;
import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.ServerResponse.ok;

@Configuration
public class RoutingConfig {

    @RouterOperations
    @Bean
    RouterFunction<ServerResponse> employeeRoutes(EmployeeRepository employeeRepository) {
        return route(GET("/v2/employee"),
                        req -> ok().body(employeeRepository.findAll(), ParameterizedTypeReference.forType(Employee.class)))
                .and(route(GET("/v2/employee/{id}"),
                        req -> ok().body(
                                employeeRepository.findById(Long.valueOf(req.pathVariable("id"))), ParameterizedTypeReference.forType(Employee.class))));
    }
}
