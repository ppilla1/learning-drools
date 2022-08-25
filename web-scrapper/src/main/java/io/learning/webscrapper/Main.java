package io.learning.webscrapper;

import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
}
