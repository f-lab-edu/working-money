package org.example.workingmoney.repository.post;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.example.workingmoney.domain.entity.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    Long savedPostId;

    @BeforeEach
    void setUp() {
        var post = postRepository.create("cmt-user@example.com", "crypto", "post title", "post content");
        savedPostId = post.id();

        // 댓글 2건 삽입
        jdbcTemplate.update(
                "insert into comment (post_id, user_id, content, created_at, updated_at) values (?, ?, ?, now(), now())",
                savedPostId, "user1@example.com", "first comment");
        jdbcTemplate.update(
                "insert into comment (post_id, user_id, content, created_at, updated_at) values (?, ?, ?, now(), now())",
                savedPostId, "user2@example.com", "second comment");
    }

    @Test
    void postId로_댓글_목록_조회_테스트() {
        List<Comment> comments = commentRepository.findByPostId(savedPostId);

        assertThat(comments).hasSize(2);
        assertThat(comments.get(0).postId()).isEqualTo(savedPostId);
        assertThat(comments.get(1).postId()).isEqualTo(savedPostId);
        assertThat(comments.get(0).content()).isEqualTo("first comment");
        assertThat(comments.get(1).content()).isEqualTo("second comment");
    }

    @Test
    void 댓글_없는_postId는_빈목록_반환() {
        Long otherPostId = postRepository.create("other@example.com", "crypto", "title", "content").id();

        List<Comment> comments = commentRepository.findByPostId(otherPostId);
        assertThat(comments).isEmpty();
    }
}


