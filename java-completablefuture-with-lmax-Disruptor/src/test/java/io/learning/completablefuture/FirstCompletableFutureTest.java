package io.learning.completablefuture;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Log4j2
public class FirstCompletableFutureTest {

    @Test
    public void test_runAsync_with_CommonForkJoinThreadPool() throws InterruptedException {
        CompletableFuture.runAsync(() -> log.info("Iam running async in thread[{}]", Thread.currentThread().getName()));
        CompletableFuture.runAsync(() -> log.info("Iam running async in thread[{}]", Thread.currentThread().getName()));
        CompletableFuture.runAsync(() -> log.info("Iam running async in thread[{}]", Thread.currentThread().getName()));

        Thread.sleep(100);
    }

    @Test
    public void test_runAsync_with_ExecutorServiceThreadPool() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        Runnable task = () -> log.info("Iam running async in thread[{}]", Thread.currentThread().getName());

        CompletableFuture.runAsync(task, executorService);
        CompletableFuture.runAsync(task, executorService);
        CompletableFuture.runAsync(task, executorService);

        executorService.shutdown();
    }

    @Test
    public void test_supplyAsync_with_commonForkJoinThreadPool() {
        Supplier<String> supplier = () -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                log.error("{}\n",e.getMessage(),e);
            }

            return Thread.currentThread().getName();
        };

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(supplier);
        String name = completableFuture.join();
        log.info("Supplier thread [{}]", name);
    }

    /*
    * CompletableFuture.complete(T defaultValue) will force complete with defaultValue (if already not completed),
    * else actual value. Both values are return by CompletableFuture.join() method
    * */
    @Test
    public void test_forcedCompletableFuture_completion_with_complete() {
        Supplier<String> supplier = () -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                log.error("{}\n", e.getMessage(), e);
            }

            return Thread.currentThread().getName();
        };

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(supplier, executorService);
        completableFuture.complete("Too long");
        String result = completableFuture.join();

        log.info("Before - Result : {}", result);

        result = completableFuture.join();

        log.info("After - Result : {}", result);
        executorService.shutdown();
    }

    /*
     * CompletableFuture.obtrudeValue(T defaultValue) will force complete with defaultValue.
     * Values are return by CompletableFuture.join() method
     * */
    @Test
    public void test_forcedCompletableFuture_completion_with_obtrudeValue() {
        Supplier<String> supplier = () -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                log.error("{}\n", e.getMessage(), e);
            }

            return Thread.currentThread().getName();
        };

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(supplier, executorService);

        String result = completableFuture.join();

        log.info("Before - Result : {}", result);

        completableFuture.obtrudeValue("Too Long");
        result = completableFuture.join();

        log.info("After - Result : {}", result);
        executorService.shutdown();
    }

    @Test
    public void chaining_tasks() {

        Supplier<List<Long>> fetchIds = () -> {
            log.info("Fetching Ids [{}]", Thread.currentThread().getName());
            return Arrays.asList(3L, 2L, 1L);
        };

        Function<List<Long>, List<Map.Entry<String, Long>>> fetchUsers = (ids) -> {
            log.info("Fetching Users [{}]", Thread.currentThread().getName());
            return ids.stream()
                    .map(id -> new AbstractMap.SimpleEntry<>("id", id))
                    .collect(Collectors.toList());
        };

        Consumer<List<Map.Entry<String, Long>>> userLogger = (users) -> {
            log.info("Logging users [{}]", Thread.currentThread().getName());
            log.info("[{}] Totals users [{}]\n{}", Thread.currentThread().getName(), users.size(),users);
        };

        Consumer<List<Long>> alerter = (ids) -> {
            log.info("Alerting [{}]", Thread.currentThread().getName());
            log.info("Ids fetched! [{}]\n{}", Thread.currentThread().getName(), ids);
        };

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        CompletableFuture<List<Long>> cf0 = CompletableFuture.supplyAsync(fetchIds, executorService);

        CompletableFuture<List<Map.Entry<String, Long>>> cf1 =cf0.thenApply(fetchUsers);
        CompletableFuture<Void> cf2 = cf1.thenAcceptAsync(userLogger, executorService);
        CompletableFuture<Void> cf3 = cf0.thenAcceptAsync(alerter, executorService);

        CompletableFuture.allOf(cf2, cf3).join();
    }
}
