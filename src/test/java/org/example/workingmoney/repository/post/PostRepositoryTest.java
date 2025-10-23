package org.example.workingmoney.repository.post;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.example.workingmoney.domain.entity.Post;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Test
    void post_저장시_id_자동_생성_및_조회_테스트() {

        // given
        String userId = "test@email.com";
        String categoryCode = "crypto";
        String title = "hello";
        String content = "world";

        // when
        Post created = postRepository.create(userId, categoryCode, title, content);

        // then
        assertNotNull(created.id());
        Post found = postRepository.getById(created.id());
        assertThat(found.id()).isEqualTo(created.id());
        assertThat(found.userId()).isEqualTo(userId);
        assertThat(found.categoryCode()).isEqualTo(categoryCode);
        assertThat(found.title()).isEqualTo(title);
        assertThat(found.content()).isEqualTo(content);
        assertThat(found.likeCount()).isEqualTo(0L);
    }

    @Test
    void post_업데이트_테스트() throws InterruptedException {

        // given
        String userId = "test@email.com";
        String categoryCode = "crypto";
        String title = "title1";
        String content = "content1";
        Post created = postRepository.create(userId, categoryCode, title, content);
        Long id = created.id();

        // when
        String newCategory = "american stock";
        String newTitle = "title2";
        String newContent = "content2";
        Post updated = postRepository.update(id, userId, newCategory, newTitle, newContent);

        // then
        assertThat(updated.id()).isEqualTo(id);
        assertThat(updated.userId()).isEqualTo(userId);
        assertThat(updated.categoryCode()).isEqualTo(newCategory);
        assertThat(updated.title()).isEqualTo(newTitle);
        assertThat(updated.content()).isEqualTo(newContent);
        assertThat(updated.likeCount()).isEqualTo(created.likeCount());
    }

    @Test
    void post_삭제_테스트() {

        // given
        String userId = "test@email.com";
        String categoryCode = "FREE";
        String title = "to be deleted";
        String content = "bye";
        Post created = postRepository.create(userId, categoryCode, title, content);
        Long id = created.id();

        // when
        postRepository.deleteById(id);

        // then
        assertThrows(NoSuchElementException.class, () -> postRepository.getById(id));
    }
}


