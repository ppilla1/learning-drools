package io.learning.graphql.api;

import io.learning.graphql.domain.Author;
import io.learning.graphql.domain.Book;
import io.learning.graphql.repository.AuthorRepository;
import io.learning.graphql.repository.BookRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
class BookGraphQL {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    BookGraphQL(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @QueryMapping(name = "books")
    Iterable<Book> all() {
        return bookRepository.findAll();
    }

    @QueryMapping(name = "bookById")
    Optional<Book> byId(@Argument(name = "id") Long bookId) {
        return bookRepository.findById(bookId);
    }

    @MutationMapping(name = "addBook")
    Book add(@Argument(name = "book") BookInput bookInput) {
        Author author = authorRepository.findById(bookInput.authorId)
                            .orElseThrow(() -> new IllegalArgumentException(" Author with id["+ bookInput.authorId + "] not found."));

        Book bk = new Book(null, bookInput.title, bookInput.publisher, author);
        return bookRepository.save(bk);
    }

    record BookInput(String title, String publisher, Long authorId){}
}
