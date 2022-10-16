package io.learning.webscrapper.rest;

import io.learning.webscrapper.domain.Book;
import io.learning.webscrapper.repository.BookRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Log4j2
@RestController
class BookRestController {

    private final BookRepository repository;

    BookRestController(BookRepository repository) {
        this.repository = repository;
    }

    @GetMapping({"/v1/book"})
    ResponseEntity<Iterable<Book>> fetchAll() {
        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }

    @GetMapping({"/v1/book/{id}"})
    ResponseEntity<Optional<Book>> fetchById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(repository.findById(id), HttpStatus.OK);
    }

}
