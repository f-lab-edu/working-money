package org.example.workingmoney.repository.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void user_저장시_id_자동_생성_테스트() {

        // given
        String password = "rawPassword";
        String nickname = "name";
        String email = "test@example.com";

        // when
        userRepository.create(password, nickname, email);

        // then
        UserEntity foundByEmailUserEntity = userRepository.findByEmail(email).orElseThrow();
        UserEntity foundByIdUserEntity = userRepository.findById(foundByEmailUserEntity.getId()).orElseThrow();
        assertThat(foundByEmailUserEntity.getId()).isNotNull();
        assertEquals(foundByEmailUserEntity, foundByIdUserEntity);
    }

    @Test
    void user_저장_테스트() {

        // given
        String password = "test";
        String nickname = "tester";
        String email = "test@example.com";

        // when
        userRepository.create(password, nickname, email);

        // then
        UserEntity foundByEmailUserEntity = userRepository.findByEmail(email).orElseThrow();
        UserEntity foundByIdUserEntity = userRepository.findById(foundByEmailUserEntity.getId()).orElseThrow();
        assertThat(foundByIdUserEntity).isEqualTo(foundByEmailUserEntity);
        assertThat(foundByIdUserEntity.getPasswordHash()).isEqualTo(password);
        assertThat(foundByIdUserEntity.getNickname()).isEqualTo(nickname);
        assertThat(foundByIdUserEntity.getEmail()).isEqualTo(email);
    }

    @Test
    void id_기반_user_찾기_테스트() {

        // given
        String password = "test";
        String nickname = "tester";
        String email = "test@example.com";
        userRepository.create(password, nickname, email);

        // when
        UserEntity foundByEmailUserEntity = userRepository.findByEmail(email).orElseThrow();
        UserEntity foundByIdUserEntity = userRepository.findById(foundByEmailUserEntity.getId()).orElseThrow();

        // then
        assertThat(foundByIdUserEntity).isEqualTo(foundByEmailUserEntity);
    }

    @Test
    void email_기반_user_찾기_테스트() {

        // given
        String password = "test";
        String nickname = "tester";
        String email = "test@example.com";
        userRepository.create(password, nickname, email);

        // when
        UserEntity foundUserEntity = userRepository.findByEmail(email).orElseThrow();
        UserEntity foundByIdUserEntity = userRepository.findById(foundUserEntity.getId()).orElseThrow();

        // then
        assertThat(foundUserEntity).isEqualTo(foundByIdUserEntity);
        assertThat(foundUserEntity.getEmail()).isEqualTo(email);
    }

    @Test
    void nickname_기반_user_찾기_테스트() {

        // given
        String password = "test";
        String nickname = "tester";
        String email = "test@example.com";
        userRepository.create(password, nickname, email);

        // when
        UserEntity foundUserEntity = userRepository.findByNickname(nickname).orElseThrow();
        UserEntity foundByIdUserEntity = userRepository.findById(foundUserEntity.getId()).orElseThrow();

        // then
        assertThat(foundUserEntity).isEqualTo(foundByIdUserEntity);
        assertThat(foundUserEntity.getNickname()).isEqualTo(nickname);
    }

    @Test
    void email_존재여부_테스트() {
        // given
        String password = "test";
        String nickname = "tester";
        String email = "exist@example.com";
        userRepository.create(password, nickname, email);

        // when & then
        assertTrue(userRepository.existsByEmail(email));
        assertFalse(userRepository.existsByEmail("not-exist@example.com"));
    }

    @Test
    void nickname_존재여부_테스트() {
        // given
        String password = "test";
        String nickname = "tester-exists";
        String email = "tester-exists@example.com";
        userRepository.create(password, nickname, email);

        // when & then
        assertTrue(userRepository.existsByNickname(nickname));
        assertFalse(userRepository.existsByNickname("tester-not-exists"));
    }

    @Test
    void user_nickname_업데이트_테스트() {

        // given
        String password = "test";
        String nickname = "tester";
        String email = "test@example.com";
        String newNickname = "tester2";
        userRepository.create(password, nickname, email);

        // when
        Long savedUserId = userRepository.findByEmail(email).orElseThrow().getId();
        userRepository.updateNickname(savedUserId, newNickname);

        // then
        UserEntity foundUser = userRepository.findById(savedUserId).orElseThrow();
        assertThat(foundUser.getNickname()).isEqualTo(newNickname);
    }

    @Test
    void user_저장시_createdAt_updatedAt_일치_테스트() {

        // given
        String password = "test";
        String nickname = "tester";
        String email = "test@example.com";

        // when
        userRepository.create(password, nickname, email);

        // then
        Long savedUserId = userRepository.findByEmail(email).orElseThrow().getId();
        UserEntity foundUserEntity = userRepository.findById(savedUserId).orElseThrow();
        assertThat(foundUserEntity.getUpdatedAt()).isEqualTo(foundUserEntity.getCreatedAt());
    }

    @Test
    void user_업데이트시_updatedAt_업데이트_테스트() throws InterruptedException {

        // given
        String password = "test";
        String nickname = "tester";
        String email = "test@example.com";
        String newNickname = "tester2";
        userRepository.create(password, nickname, email);
        Long savedUserId = userRepository.findByEmail(email).orElseThrow().getId();
        LocalDateTime beforeUpdatedAt = userRepository.findById(savedUserId).orElseThrow().getUpdatedAt();

        // when
        Thread.sleep(1000);
        userRepository.updateNickname(savedUserId, newNickname);

        // then
        UserEntity foundUser = userRepository.findById(savedUserId).orElseThrow();

        assertThat(foundUser.getCreatedAt()).isNotEqualTo(foundUser.getUpdatedAt());
        assertThat(foundUser.getUpdatedAt()).isAfter(beforeUpdatedAt);
        assertThat(foundUser.getUpdatedAt()).isAfter(foundUser.getCreatedAt());
    }

    @Test
    void id값_기반_user_삭제_테스트() {

        // given
        String password = "test";
        String nickname = "tester";
        String email = "test@example.com";
        userRepository.create(password, nickname, email);

        // when
        Long savedUserId = userRepository.findByEmail(email).orElseThrow().getId();
        userRepository.deleteById(savedUserId);

        // then
        assertTrue(userRepository.findById(savedUserId).isEmpty());
    }
}
