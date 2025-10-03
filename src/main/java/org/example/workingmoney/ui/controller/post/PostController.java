package org.example.workingmoney.ui.controller.post;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.workingmoney.domain.entity.Post;
import org.example.workingmoney.domain.entity.PostCategory;
import org.example.workingmoney.service.common.SecurityProvider;
import org.example.workingmoney.service.post.PostService;
import org.example.workingmoney.ui.controller.common.Response;
import org.example.workingmoney.ui.dto.request.post.CreatePostRequestDto;
import org.example.workingmoney.ui.dto.request.post.UpdatePostRequestDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final SecurityProvider authenticationProvider;

    @PostMapping
    public Response<Post> create(@RequestBody @Valid CreatePostRequestDto dto) {
        PostCategory category = dto.category();
        Post post = postService.create(
                authenticationProvider.getCurrentUserId(),
                category.getCode(),
                dto.title(),
                dto.content());

        return Response.ok(post);
    }

    @PutMapping
    public Response<Post> update(@RequestBody @Valid UpdatePostRequestDto dto) {
        PostCategory category = dto.category();
        Post post = postService.update(
                dto.id(),
                authenticationProvider.getCurrentUserId(),
                category.getCode(),
                dto.title(),
                dto.content());

        return Response.ok(post);
    }

    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable Long id) {
        postService.delete(id, authenticationProvider.getCurrentUserId());

        return Response.ok(null);
    }
}


