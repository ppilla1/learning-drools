package io.learning.completablefuture;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j2
public class PerformanceWithCompletableFuture {

    @Test
    public void test_jdkHttpClient_setup() throws IOException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        HttpClient client = HttpClient.newBuilder()
                                .version(HttpClient.Version.HTTP_1_1)
                                .build();

        HttpRequest request = HttpRequest.newBuilder()
                                .GET()
                                .uri(URI.create("https://www.google.com"))
                                .build();

        CompletableFuture<Void> start = new CompletableFuture<>();
        CompletableFuture<Void> future = start.thenCompose(nil -> client.sendAsync(request, HttpResponse.BodyHandlers.ofString()))
                .thenAccept(resp -> {
                    String body = resp.body();
                    int length = body.length();
                    log.info("{} Length [{}]\n{}", Thread.currentThread().getName(), length, body);
                })
                .thenRun(() -> log.info("{} Done !!", Thread.currentThread().getName()));

        start.complete(null);
        //future.join();
        executorService.shutdown();
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.error("{}", e.getMessage(), e);
        }
    }
}
