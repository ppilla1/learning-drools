package io.learning.graphql.api;

import io.learning.graphql.domain.Author;
import io.learning.graphql.repository.AuthorRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
class AuthorGraphQL {
    private final AuthorRepository authorRepository;

    AuthorGraphQL(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @QueryMapping(name = "authors")
    Iterable<Author> all() {
        return authorRepository.findAll();
    }

    @QueryMapping(name = "authorById")
    Optional<Author> byId(@Argument(name = "id") Long authorId) {
        return authorRepository.findById(authorId);
    }
}
