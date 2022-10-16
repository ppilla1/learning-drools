package io.learning.graphql.api;

import io.learning.graphql.domain.Book;
import io.learning.graphql.repository.BookRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
class BookGraphQL {

    private final BookRepository bookRepository;

    BookGraphQL(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @QueryMapping(name = "books")
    Iterable<Book> all() {
        return bookRepository.findAll();
    }

    @QueryMapping(name = "bookById")
    Optional<Book> byId(@Argument(name = "id") Long bookId) {
        return bookRepository.findById(bookId);
    }
}
