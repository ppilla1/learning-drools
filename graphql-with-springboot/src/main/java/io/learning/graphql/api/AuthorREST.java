package io.learning.graphql.api;

import io.learning.graphql.repository.AuthorRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Log4j2
@RestController
@RequestMapping(path = "/authors")
public class AuthorREST {

    private final AuthorRepository authorRepository;

    public AuthorREST(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @GetMapping
    ResponseEntity<Set<Map<String, Object>>> allAuthors() {

        /* Below implementation was done to prevent circular reference while json deserialize */

        return ResponseEntity.ok(StreamSupport.stream(authorRepository.findAll().spliterator(), false)
                .map(auth -> {
                    Set<String> bookTitles = auth.getBooks().stream()
                            .map(book -> book.getTitle())
                            .collect(Collectors.toSet());
                    Map<String, Object> author = new HashMap<>();
                    author.put("id", auth.getId());
                    author.put("name", auth.getName());
                    author.put("books", bookTitles);
                    return author;
                })
                .collect(Collectors.toSet()));
    }
}
