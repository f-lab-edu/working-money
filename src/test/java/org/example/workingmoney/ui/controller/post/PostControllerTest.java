package org.example.workingmoney.ui.controller.post;

 
import org.example.workingmoney.service.common.SecurityProvider;
import org.example.workingmoney.service.post.PostService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
 
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PostController.class)
@AutoConfigureMockMvc(addFilters = false)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @MockitoBean
    private SecurityProvider securityProvider;

    @Test
    void 게시글_생성_성공_테스트() throws Exception {
        // given
        var json = """
            {
              "category": "korean_stocks",
              "title": "제목",
              "content": "내용"
            }
            """;
        final String userId = "test@email.com";
        when(securityProvider.getCurrentUserId()).thenReturn(userId);

        // when
        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.message").value("ok"));


        // then
        verify(securityProvider, times(1))
                .getCurrentUserId();
        verify(postService, times(1))
                .create(userId, "korean_stocks", "제목", "내용");
    }

    @Test
    void 게시글_생성_유효성_검사_실패_테스트() throws Exception {
        // given
        var json = """
            {
              "userId": 1,
              "category": "korean_stocks",
              "title": "",
              "content": "내용"
            }
            """;

        // when
        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        // then
        verify(postService, never())
                .create(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void 게시글_수정_성공_테스트() throws Exception {
        // given
        var json = """
            {
              "id": 10,
              "userId": 1,
              "category": "korean_stocks",
              "title": "새 제목",
              "content": "내용"
            }
            """;
        final String userId = "test@email.com";
        when(securityProvider.getCurrentUserId()).thenReturn(userId);

        // when
        mockMvc.perform(put("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.message").value("ok"));

        // then
        verify(postService, times(1))
                .update(10L, userId, "korean_stocks", "새 제목", "내용");
    }

    @Test
    void 게시글_수정_유효성_검사_실패_테스트() throws Exception {
        // given
        var json = """
            {
              "id": 10,
              "category": "korean_stocks",
              "title": "",
              "content": "내용"
            }
            """;

        // when
        mockMvc.perform(put("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        // then
        verify(postService, never())
                .update(anyLong(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void 게시글_생성_category_잘못된_값_실패_테스트() throws Exception {
        // given
        var json = """
            {
              "userId": 1,
              "category": "invalid_category",
              "title": "제목",
              "content": "내용"
            }
            """;

        // when
        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        // then
        verify(postService, never())
                .create(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void 게시글_수정_category_잘못된_값_실패_테스트() throws Exception {
        // given
        var json = """
            {
              "id": 10,
              "userId": 1,
              "category": "invalid_category",
              "title": "새 제목",
              "content": "내용"
            }
            """;

        // when
        mockMvc.perform(put("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        // then
        verify(postService, never())
                .update(anyLong(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void 게시글_삭제_성공_테스트() throws Exception {
        // given
        final String userId = "test@email.com";
        when(securityProvider.getCurrentUserId()).thenReturn(userId);

        // when
        mockMvc.perform(delete("/api/v1/posts/{id}", 10L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.message").value("ok"));

        // then
        verify(postService, times(1)).delete(10L, userId);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }
}


