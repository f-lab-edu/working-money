package org.example.workingmoney.service.post;

import org.example.workingmoney.domain.entity.Comment;
import org.example.workingmoney.domain.entity.Post;
import org.example.workingmoney.domain.entity.CursorSlice;
import org.example.workingmoney.repository.post.CommentRepository;
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
import java.util.List;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserService userService;

    @Mock
    private CommentRepository commentRepository;

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
        when(postRepository.getById(id))
                .thenReturn(new Post(id, userId, categoryCode, "title1", "content1", 3L));
        when(postRepository.update(id, userId, categoryCode, title, content)).thenReturn(updated);
        setAuthenticatedUser(userId);

        // when
        Post actual = postService.update(id, userId, categoryCode, title, content);

        // then
        assertSame(updated, actual);
        verify(postRepository, times(1)).getById(id);
        verify(postRepository, times(1)).update(id, userId, categoryCode, title, content);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void post_삭제_테스트() {
        // given
        Long id = 30L;
        String userId = "test@email.com";
        when(postRepository.getById(id))
                .thenReturn(new Post(id, userId, "crypto", "t", "c", 0L));
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
        when(postRepository.getById(id))
                .thenReturn(new Post(id, ownerUserId, categoryCode, "old", "old", 0L));
        setAuthenticatedUser(otherUserId);

        // when & then
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> postService.update(id, otherUserId, categoryCode, title, content));
        assertEquals("사용자가 작성한 글이 아닙니다.", ex.getMessage());
        verify(postRepository, times(1)).getById(id);
        verify(postRepository, never()).update(anyLong(), anyString(), anyString(), anyString(), anyString());
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void post_삭제_권한_에러_테스트() {
        // given
        Long id = 50L;
        String ownerUserId = "owner@email.com";
        String otherUserId = "other@email.com";
        when(postRepository.getById(id))
                .thenReturn(new Post(id, ownerUserId, "crypto", "t", "c", 0L));
        setAuthenticatedUser(otherUserId);

        // when & then
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> postService.delete(id, otherUserId));
        assertEquals("사용자가 작성한 글이 아닙니다.", ex.getMessage());
        verify(postRepository, times(1)).getById(id);
        verify(postRepository, never()).deleteById(anyLong());
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void 커서_기반_목록조회_hasNext_true_테스트() {
        // given
        String category = "crypto";
        Long cursor = 100L;
        int size = 3;
        List<Post> fetched = List.of(
                new Post(99L, "u", category, "t1", "c1", 0L),
                new Post(98L, "u", category, "t2", "c2", 0L),
                new Post(97L, "u", category, "t3", "c3", 0L),
                new Post(96L, "u", category, "t4", "c4", 0L)
        );
        when(postRepository.findPosts(eq(category), eq(cursor), eq(size + 1))).thenReturn(fetched);

        // when
        CursorSlice<Post> slice = postService.findPostsWithCursor(category, cursor, size);

        // then
        assertEquals(size, slice.content().size());
        assertTrue(slice.hasNext());
        assertEquals(97L, slice.nextCursor()); // 마지막 콘텐츠의 id
        verify(postRepository, times(1)).findPosts(category, cursor, size + 1);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void 커서_기반_목록조회_hasNext_false_테스트() {
        // given
        String category = null; // 카테고리 미지정 케이스
        Long cursor = null;
        int size = 3;
        List<Post> fetched = List.of(
                new Post(5L, "u", "free", "t1", "c1", 0L),
                new Post(4L, "u", "free", "t2", "c2", 0L)
        );
        when(postRepository.findPosts(eq(category), eq(cursor), eq(size + 1))).thenReturn(fetched);

        // when
        CursorSlice<Post> slice = postService.findPostsWithCursor(category, cursor, size);

        // then
        assertEquals(2, slice.content().size());
        assertFalse(slice.hasNext());
        assertNull(slice.nextCursor()); // hasNext=false면 nextCursor=null
        verify(postRepository, times(1)).findPosts(category, cursor, size + 1);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void 단건조회_getPostById_테스트() {
        // given
        Long id = 10L;
        Post expected = new Post(id, "u", "crypto", "t", "c", 0L);
        when(postRepository.getById(id)).thenReturn(expected);

        // when
        Post actual = postService.getPostById(id);

        // then
        assertSame(expected, actual);
        verify(postRepository, times(1)).getById(id);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void 댓글목록조회_getCommentsByPostId_테스트() {
        // given
        Long postId = 11L;
        var comments = java.util.List.of(
                new Comment(1L, postId, "userA", "c1"),
                new Comment(2L, postId, "userB", "c2")
        );
        when(commentRepository.findByPostId(postId)).thenReturn(comments);

        // when
        var actual = postService.getCommentsByPostId(postId);

        // then
        assertEquals(2, actual.size());
        assertEquals("c1", actual.get(0).content());
        assertEquals("c2", actual.get(1).content());
        verify(commentRepository, times(1)).findByPostId(postId);
        verifyNoMoreInteractions(commentRepository);
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


