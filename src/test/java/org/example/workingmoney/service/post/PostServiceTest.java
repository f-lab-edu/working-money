package org.example.workingmoney.service.post;

import org.example.workingmoney.domain.entity.Post;
import org.example.workingmoney.repository.post.PostRepository;
import org.example.workingmoney.service.user.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserService userService;

    @Test
    void post_생성_테스트() {
        // given
        String userId = "test@email.com";
        String categoryCode = "crypto";
        String title = "hello";
        String content = "world";
        Post expected = new Post(10L, userId, categoryCode, title, content, 0L);

        when(postRepository.create(userId, categoryCode, title, content)).thenReturn(expected);

        // when
        Post actual = postService.create(userId, categoryCode, title, content);

        // then
        assertSame(expected, actual);
        verify(postRepository, times(1)).create(userId, categoryCode, title, content);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void post_업데이트_테스트() {
        // given
        Long id = 20L;
        String userId = "test@email.com";
        String categoryCode = "stock";
        String title = "title2";
        String content = "content2";
        Post updated = new Post(id, userId, categoryCode, title, content, 3L);
        when(postRepository.findById(id))
                .thenReturn(java.util.Optional.of(new Post(id, userId, categoryCode, "title1", "content1", 3L)));
        when(postRepository.update(id, userId, categoryCode, title, content)).thenReturn(updated);
        setAuthenticatedUser(userId);

        // when
        Post actual = postService.update(id, userId, categoryCode, title, content);

        // then
        assertSame(updated, actual);
        verify(postRepository, times(1)).findById(id);
        verify(postRepository, times(1)).update(id, userId, categoryCode, title, content);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void post_삭제_테스트() {
        // given
        Long id = 30L;
        String userId = "test@email.com";
        when(postRepository.findById(id))
                .thenReturn(java.util.Optional.of(new Post(id, userId, "crypto", "t", "c", 0L)));
        setAuthenticatedUser(userId);

        // when
        postService.delete(id, userId);

        // then
        verify(postRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void post_업데이트_권한_에러_테스트() {
        // given
        Long id = 40L;
        String ownerUserId = "owner@email.com";
        String otherUserId = "other@email.com";
        String categoryCode = "stock";
        String title = "newTitle";
        String content = "newContent";
        when(postRepository.findById(id))
                .thenReturn(java.util.Optional.of(new Post(id, ownerUserId, categoryCode, "old", "old", 0L)));
        setAuthenticatedUser(otherUserId);

        // when & then
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> postService.update(id, otherUserId, categoryCode, title, content));
        assertEquals("본인 게시글만 수정할 수 있습니다.", ex.getMessage());
        verify(postRepository, times(1)).findById(id);
        verify(postRepository, never()).update(anyLong(), anyString(), anyString(), anyString(), anyString());
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void post_삭제_권한_에러_테스트() {
        // given
        Long id = 50L;
        String ownerUserId = "owner@email.com";
        String otherUserId = "other@email.com";
        when(postRepository.findById(id))
                .thenReturn(java.util.Optional.of(new Post(id, ownerUserId, "crypto", "t", "c", 0L)));
        setAuthenticatedUser(otherUserId);

        // when & then
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> postService.delete(id, otherUserId));
        assertEquals("본인 게시글만 삭제할 수 있습니다.", ex.getMessage());
        verify(postRepository, times(1)).findById(id);
        verify(postRepository, never()).deleteById(anyLong());
        verifyNoMoreInteractions(postRepository);
    }

    private void setAuthenticatedUser(String userId) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userId,
                null,
                java.util.Collections.emptyList()
        );
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }
}


