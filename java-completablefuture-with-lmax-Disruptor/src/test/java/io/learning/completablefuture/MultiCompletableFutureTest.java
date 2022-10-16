package io.learning.completablefuture;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Log4j2
public class MultiCompletableFutureTest {

    static CompletableFuture<String> facebook(ExecutorService executorService, String author) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                log.info("{}", e.getMessage(), e);
            }
            return author + " - " + "My Facebook post";
        }, executorService);
    }

    static CompletableFuture<String> instagram(ExecutorService executorService, String author) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                log.info("{}", e.getMessage(), e);
            }
            return author + " - " + "My Instagram post";
        }, executorService);
    }

    static CompletableFuture<String> twitter(ExecutorService executorService, String author) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                log.info("{}", e.getMessage(), e);
            }
            return author + " - " + "My Twitter post";
        }, executorService);
    }

    static List<CompletableFuture<String>> generateTestData() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        List<CompletableFuture<String>> futures = new ArrayList<>();
        futures.add(facebook(executorService, "Abc"));
        futures.add(instagram(executorService, "Ghi"));
        futures.add(twitter(executorService, "Def"));

        return futures;
    }

    //@Test
    public void test_sync_postFetch() {
        for (int i = 0; i < 10; i++) {
            List<CompletableFuture<String>> futures = generateTestData();

            long startTm = System.currentTimeMillis();
            List<String> allPosts = futures.stream()
                                        .map(cf -> cf.join())
                                        .collect(Collectors.toList());
            log.info("[{}] Total processing time - {}\n{}", i + 1, System.currentTimeMillis() - startTm, allPosts);
        }
    }

    //@Test
    public void test_Async_postFetch() {
        for (int i = 0; i < 10; i++) {
            List<CompletableFuture<String>> futures = generateTestData();
            CompletableFuture<String>[] cfs = new CompletableFuture[futures.size()];
            futures.toArray(new CompletableFuture<?>[]{});

            long startTm = System.currentTimeMillis();
            List<String> allPosts = null;
                allPosts = CompletableFuture.anyOf(cfs)
                                            .thenApply(nil -> {
                                                List<String> posts = futures.stream()
                                                        .map(cf -> {
                                                            try {
                                                                return cf.get(400, TimeUnit.MILLISECONDS);
                                                            } catch (InterruptedException e) {
                                                                e.printStackTrace();
                                                            } catch (ExecutionException e) {
                                                                e.printStackTrace();
                                                            } catch (TimeoutException e) {
                                                                e.printStackTrace();
                                                            }
                                                            return null;
                                                        })
                                                        .collect(Collectors.toList());

                                                return posts;
                                            }).join();

            log.info("[{}] Total processing time - {}\n{}", i + 1, System.currentTimeMillis() - startTm, allPosts);
        }
    }

}
