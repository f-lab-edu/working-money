package org.example.workingmoney.repository.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.example.workingmoney.domain.entity.UserRole;
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
        userRepository.create(password, nickname, email, UserRole.NORMAL_USER.name());

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
        userRepository.create(password, nickname, email, UserRole.NORMAL_USER.name());

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
        userRepository.create(password, nickname, email, UserRole.NORMAL_USER.name());

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
        userRepository.create(password, nickname, email, UserRole.NORMAL_USER.name());

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
        userRepository.create(password, nickname, email, UserRole.NORMAL_USER.name());

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
        userRepository.create(password, nickname, email, UserRole.NORMAL_USER.name());

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
        userRepository.create(password, nickname, email, UserRole.NORMAL_USER.name());

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
        userRepository.create(password, nickname, email, UserRole.NORMAL_USER.name());

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
        userRepository.create(password, nickname, email, UserRole.NORMAL_USER.name());

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
        userRepository.create(password, nickname, email, UserRole.NORMAL_USER.name());
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
        userRepository.create(password, nickname, email, UserRole.NORMAL_USER.name());

        // when
        Long savedUserId = userRepository.findByEmail(email).orElseThrow().getId();
        userRepository.deleteById(savedUserId);

        // then
        assertTrue(userRepository.findById(savedUserId).isEmpty());
    }

    @Test
    void id값_기반_refresh_token_업데이트_테스트() {

        // given
        String password = "test";
        String nickname = "tester";
        String email = "test@example.com";
        String refreshToken = "test-refresh-token";
        userRepository.create(password, nickname, email, UserRole.NORMAL_USER.name());
        Long savedUserId = userRepository.findByEmail(email).orElseThrow().getId();

        // when
        userRepository.updateRefreshTokenById(savedUserId, refreshToken);
        
        // then
        String foundToken = userRepository.findRefreshTokenById(savedUserId).orElseThrow();
        assertEquals(refreshToken, foundToken);
    }

    @Test
    void refresh_token_미설정시_empty_반환_테스트() {

        // given
        String password = "test";
        String nickname = "tester-no-token";
        String email = "no-token@example.com";
        userRepository.create(password, nickname, email, UserRole.NORMAL_USER.name());
        Long savedUserId = userRepository.findByEmail(email).orElseThrow().getId();

        // when
        var optionalToken = userRepository.findRefreshTokenById(savedUserId);

        // then
        assertTrue(optionalToken.isEmpty());
    }

    @Test
    void 존재하지_않는_id_refresh_token_조회시_empty_반환_테스트() {

        // when
        var optionalToken = userRepository.findRefreshTokenById(999999999L);

        // then
        assertTrue(optionalToken.isEmpty());
    }
}
