package org.example.workingmoney.service.post;

import lombok.RequiredArgsConstructor;
import org.example.workingmoney.domain.entity.Post;
import org.example.workingmoney.repository.post.PostRepository;
import org.example.workingmoney.domain.entity.CursorSlice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Post create(String userId, String categoryCode, String title, String content) {
        return postRepository.create(userId, categoryCode, title, content);
    }

    @Transactional
    public Post update(
            Long id,
            String userId,
            String categoryCode,
            String title,
            String content) {
        validateOwner(id, userId);

        return postRepository.update(id, userId, categoryCode, title, content);
    }

    @Transactional
    public void delete(Long id, String userId) {
        validateOwner(id, userId);

        postRepository.deleteById(id);
    }

    @Transactional
    public CursorSlice<Post> getPostsWithCursor(String categoryCode, Long cursor, int size) {
        List<Post> posts = postRepository.findPosts(categoryCode, cursor, size + 1);
        return CursorSlice.createCursorSlice(posts, size, Post::getId);
    }

    private void validateOwner(Long postId, String userId) {
        Post existingPost = postRepository.findById(postId).orElseThrow();
        if (!existingPost.getUserId().equals(userId)) {
            throw new IllegalArgumentException("사용자가 작성한 글이 아닙니다.");
        }
    }
}
