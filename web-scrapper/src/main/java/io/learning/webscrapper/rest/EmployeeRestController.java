package io.learning.webscrapper.rest;

import io.learning.webscrapper.domain.Employee;
import io.learning.webscrapper.repository.EmployeeRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Set;

@Log4j2
@RestController
class EmployeeRestController {

    private final EmployeeRepository employeeRepository;

    EmployeeRestController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping({"/v1/employee"})
    ResponseEntity<Iterable<Employee>> fetchAll() {
        return new ResponseEntity<>(employeeRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping({"/v1/employee/{id}"})
    ResponseEntity<Optional<Employee>> fetchById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(employeeRepository.findById(id), HttpStatus.OK);
    }
}
