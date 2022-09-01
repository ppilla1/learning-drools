package io.learning.webscrapper;

import io.learning.webscrapper.domain.Author;
import io.learning.webscrapper.domain.Book;
import io.learning.webscrapper.domain.Employee;
import io.learning.webscrapper.repository.AuthorRepository;
import io.learning.webscrapper.repository.BookRepository;
import io.learning.webscrapper.repository.EmployeeRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@Log4j2
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        /*
        //Validate.isTrue(args.length == 1, "usage: Supply url to fetch.");
        final String url = "https://www.dw.com/en/how-can-india-turn-around-the-parsi-communitys-dwindling-demographics/a-62822568?utm_source=pocket-newtab-intl-en";

        Try.of(() -> Jsoup.connect(url).get())
                .forEach(doc -> log.info("Html for {}\n{}", url, doc.outerHtml()));
                //.get()
                //.select("html").stream()
                ;

         */
        log.info("Hello, World SpringBoot");
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner run(
            EmployeeRepository employeeRepository,
            BookRepository bookRepository,
            AuthorRepository authorRepository) {
        return args -> {
            initializeEmployees(employeeRepository);
            initializeBooksAndAuthors(bookRepository, authorRepository);
        };
    }

    private void initializeBooksAndAuthors(BookRepository bookRepository, AuthorRepository authorRepository) {
        Author josh = authorRepository.save(new Author(2l, "Josh Long"));
        Author mark = authorRepository.save(new Author(1l, "Mark Heckler"));

        bookRepository.saveAll(Arrays.asList(
                new Book("Reactive Spring", "Josh Long", josh ),
                new Book("Cloud Native Java", "O'Reilly", josh),
                new Book("Spring Boot Up & Running", "O'Reilly", mark)
        ));
    }

    private void initializeEmployees(EmployeeRepository repository) {
        repository.save(new Employee("Prashant", "Pillai"));
        repository.save(new Employee("Dalia","Abo Sheasha"));
        repository.save(new Employee("Trisha", "Gee"));
        repository.save(new Employee("Helen", "Scott"));
        repository.save(new Employee("Mala", "Gupta"));
    }
}
