package io.learning.webscrapper.rest;

import io.learning.webscrapper.domain.Author;
import io.learning.webscrapper.repository.AuthorRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Log4j2
@RestController
class AuthorRestController {

    private final AuthorRepository repository;

    AuthorRestController(AuthorRepository repository) {
        this.repository = repository;
    }

    @GetMapping({"/v1/author"})
    ResponseEntity<Iterable<Author>> fetchAll() {
        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }

    @GetMapping({"/v1/author/{id}"})
    ResponseEntity<Optional<Author>> fetchById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(repository.findById(id), HttpStatus.OK);
    }
}
