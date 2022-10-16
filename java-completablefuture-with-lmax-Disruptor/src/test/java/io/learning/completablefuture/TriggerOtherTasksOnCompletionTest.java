package io.learning.completablefuture;

import lombok.Builder;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Log4j2
public class TriggerOtherTasksOnCompletionTest {

    @Test
    public void test_CompletableFuture_methodChaining() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        UserService userService = new UserService(executorService);

        CompletableFuture<Void> start = new CompletableFuture<>();
        CompletableFuture<List<User>> userCompletableFuture = start.thenComposeAsync(nil -> userService.fetchUserIds(), executorService)
                                                                    .thenComposeAsync(ids -> userService.fetchUsers(ids), executorService);
        start.completeAsync(() -> null, executorService);
        log.info("[{}] users {}", Thread.currentThread().getName(), userCompletableFuture.join());
        executorService.shutdown();
    }

    @Data
    @Builder
    static class User {
        private int id;
    }

    static class UserService {
        private final ExecutorService executorService;

        UserService(ExecutorService executorService) {
            this.executorService = executorService;
        }

        public CompletableFuture<List<Integer>> fetchUserIds() {
            return CompletableFuture.supplyAsync(() -> {
                List<Integer> userIds = Arrays.asList(3, 2, 1);
                log.info("[Thread-{}] UserIds {}", Thread.currentThread().getName(), userIds);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    log.info("{}\n", e.getMessage(), e);
                }

                return userIds;
            }, executorService);
        }

        public CompletableFuture<List<User>> fetchUsers(List<Integer> ids) {
            return CompletableFuture.supplyAsync(() -> {
                List<User> users = ids.stream()
                        .map(id -> User.builder().id(id).build())
                        .collect(Collectors.toList());

                log.info("[Thread-{}] Users {}", Thread.currentThread().getName(), users);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    log.info("{}\n", e.getMessage(), e);
                }

                return users;
            }, executorService);
        }
    }
}
