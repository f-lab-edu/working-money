package org.example.workingmoney.service.auth;

import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.example.workingmoney.service.auth.exception.DuplicatedEmailException;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@Slf4j
class AuthServiceConcurrencyTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RepeatedTest(100)
    void 동시에_같은_이메일로_회원가입_시도_시_동시성_테스트() throws Exception {

        AtomicInteger duplicatedEmailFailureCount = new AtomicInteger(0);

        // given
        String email = "same@example.com";
        String nickname1 = "nickname1";
        String nickname2 = "nickname2";
        String password = "password";

        ExecutorService executor = Executors.newFixedThreadPool(2);

        // when
        try {
            CompletableFuture<Void> task1 = CompletableFuture
                    .runAsync(() -> {
                        try {
                            authService.join(email, nickname1, password);
                        } catch (DuplicatedEmailException e) {
                            duplicatedEmailFailureCount.addAndGet(1);
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    }, executor);

            CompletableFuture<Void> task2 = CompletableFuture
                    .runAsync(() -> {
                        try {
                            authService.join(email, nickname2, password);
                        } catch (DuplicatedEmailException e) {
                            duplicatedEmailFailureCount.addAndGet(1);
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    }, executor);

            CompletableFuture.allOf(task1, task2).get();

            // then
            Integer count = jdbcTemplate.queryForObject(
                    "select count(*) from user where email = ?",
                    Integer.class,
                    email
            );

            assertThat(count).isEqualTo(1);
            assertThat(duplicatedEmailFailureCount.get()).isEqualTo(1);

        } finally {
            executor.shutdown();
        }
    }
}