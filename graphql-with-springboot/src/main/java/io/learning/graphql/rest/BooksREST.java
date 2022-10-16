package io.learning.graphql.rest;

import io.learning.graphql.repository.BookRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(path = "/books")
public class BooksREST {

    private final BookRepository bookRepository;

    public BooksREST(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    ResponseEntity<Set<Map<String, Object>>> allBooks() {

        /* Below implementation was done to prevent circular reference while json deserialize */

        return ResponseEntity.ok(StreamSupport.stream(bookRepository.findAll().spliterator(), false)
                .map(bk -> {
                    Map<String, Object> book = new HashMap<>();
                    book.put("id", bk.getId());
                    book.put("title", bk.getTitle());
                    book.put("publisher", bk.getPublisher());
                    book.put("author", bk.getAuthor().getName());
                    return book;
                })
                .collect(Collectors.toSet()));
    }
}
