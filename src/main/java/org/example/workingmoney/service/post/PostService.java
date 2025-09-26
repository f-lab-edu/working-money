package org.example.workingmoney.service.post;

import lombok.RequiredArgsConstructor;
import org.example.workingmoney.domain.entity.Post;
import org.example.workingmoney.repository.post.PostRepository;
import org.example.workingmoney.service.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    @Transactional
    public Post create(String userId, String categoryCode, String title, String content) {
        return postRepository.create(userId, categoryCode, title, content);
    }

    @Transactional
    public Post update(Long id, String categoryCode, String title, String content) {
        Post existing = postRepository.findById(id).orElseThrow();

        String requesterUserId = getAuthenticatedUserId();

        if (!existing.getUserId().equals(requesterUserId)) {
            throw new IllegalArgumentException("본인 게시글만 수정할 수 있습니다.");
        }

        return postRepository.update(id, requesterUserId, categoryCode, title, content);
    }

    @Transactional
    public void delete(Long id) {
        String authenticatedUserId = getAuthenticatedUserId();
        Post existing = postRepository.findById(id).orElseThrow();
        if (!existing.getUserId().equals(authenticatedUserId)) {
            throw new IllegalArgumentException("본인 게시글만 삭제할 수 있습니다.");
        }

        postRepository.deleteById(id);
    }

    private String getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new IllegalArgumentException("");
        }

        return authentication.getName();
    }
}
