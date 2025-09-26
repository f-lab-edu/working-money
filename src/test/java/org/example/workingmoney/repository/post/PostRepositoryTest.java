package org.example.workingmoney.repository.post;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.example.workingmoney.domain.entity.Post;
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
        assertNotNull(created.getId());
        Post found = postRepository.findById(created.getId()).orElseThrow();
        assertThat(found.getId()).isEqualTo(created.getId());
        assertThat(found.getUserId()).isEqualTo(userId);
        assertThat(found.getCategoryCode()).isEqualTo(categoryCode);
        assertThat(found.getTitle()).isEqualTo(title);
        assertThat(found.getContent()).isEqualTo(content);
        assertThat(found.getLikeCount()).isEqualTo(0L);
    }

    @Test
    void post_업데이트_테스트() throws InterruptedException {

        // given
        String userId = "test@email.com";
        String categoryCode = "crypto";
        String title = "title1";
        String content = "content1";
        Post created = postRepository.create(userId, categoryCode, title, content);
        Long id = created.getId();

        // when
        String newCategory = "american stock";
        String newTitle = "title2";
        String newContent = "content2";
        Post updated = postRepository.update(id, userId, newCategory, newTitle, newContent);

        // then
        assertThat(updated.getId()).isEqualTo(id);
        assertThat(updated.getUserId()).isEqualTo(userId);
        assertThat(updated.getCategoryCode()).isEqualTo(newCategory);
        assertThat(updated.getTitle()).isEqualTo(newTitle);
        assertThat(updated.getContent()).isEqualTo(newContent);
        assertThat(updated.getLikeCount()).isEqualTo(created.getLikeCount());
    }

    @Test
    void post_삭제_테스트() {

        // given
        String userId = "test@email.com";
        String categoryCode = "FREE";
        String title = "to be deleted";
        String content = "bye";
        Post created = postRepository.create(userId, categoryCode, title, content);
        Long id = created.getId();

        // when
        postRepository.deleteById(id);

        // then
        assertTrue(postRepository.findById(id).isEmpty());
    }
}


