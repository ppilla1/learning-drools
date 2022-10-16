package io.learning.graphql;

import io.learning.graphql.domain.Author;
import io.learning.graphql.domain.Book;
import io.learning.graphql.repository.AuthorRepository;
import io.learning.graphql.repository.BookRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@Log4j2
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner(AuthorRepository authorRepository, BookRepository bookRepository) {
        return args -> {

            Author josh = new Author();
            josh.setName("Josh Long");

            Author mark = new Author();
            mark.setName("Mark Heckler");

            Book cloudNative = new Book(null,
                    "Cloud Native Java: Designing Resilient Systems with Spring Boot, Spring Cloud, and Cloud Foundry",
                    "O'Reilly", josh);
            josh.getBooks().add(cloudNative);

            Book reactiveSpring = new Book(null,
                    "Reactive Spring",
                    "Starbuxman", josh);
            josh.getBooks().add(reactiveSpring);

            Book springBootUp = new Book(null,
                    "Spring Boot: Up and Running: Building Cloud Native Java and Kotlin Applications",
                    "O'Reilly", mark);
            mark.getBooks().add(springBootUp);

            authorRepository.save(josh);
            authorRepository.save(mark);
        };
    }
}
