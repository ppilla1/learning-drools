package io.learning.webscrapper;

import io.learning.webscrapper.domain.Employee;
import io.learning.webscrapper.repository.EmployeeRepository;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Log4j2
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
//        //Validate.isTrue(args.length == 1, "usage: Supply url to fetch.");
//        final String url = "https://www.dw.com/en/how-can-india-turn-around-the-parsi-communitys-dwindling-demographics/a-62822568?utm_source=pocket-newtab-intl-en";
//
//        Try.of(() -> Jsoup.connect(url).get())
//                .forEach(doc -> log.info("Html for {}\n{}", url, doc.outerHtml()));
//                //.get()
//                //.select("html").stream()
//                ;
        log.info("Hello, World SpringBoot");
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner run(EmployeeRepository employeeRepository) {
        return args -> {
            initializeEmployees(employeeRepository);
        };
    }

    private void initializeEmployees(EmployeeRepository repository) {
        repository.save(new Employee("Prashant", "Pillai"));
        repository.save(new Employee("Dalia","Abo Sheasha"));
        repository.save(new Employee("Trisha", "Gee"));
        repository.save(new Employee("Helen", "Scott"));
        repository.save(new Employee("Mala", "Gupta"));
    }
}
