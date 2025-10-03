package org.example.workingmoney.service.post;

import lombok.RequiredArgsConstructor;
import org.example.workingmoney.domain.entity.Post;
import org.example.workingmoney.repository.post.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
        Post existing = postRepository.findById(id).orElseThrow();

        if (!existing.getUserId().equals(userId)) {
            throw new IllegalArgumentException("본인 게시글만 수정할 수 있습니다.");
        }

        return postRepository.update(id, userId, categoryCode, title, content);
    }

    @Transactional
    public void delete(Long id, String userId) {
        Post existing = postRepository.findById(id).orElseThrow();
        if (!existing.getUserId().equals(userId)) {
            throw new IllegalArgumentException("본인 게시글만 삭제할 수 있습니다.");
        }

        postRepository.deleteById(id);
    }
}
