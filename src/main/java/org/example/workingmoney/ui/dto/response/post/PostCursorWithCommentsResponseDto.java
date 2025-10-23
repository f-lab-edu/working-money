package org.example.workingmoney.ui.dto.response.post;

import org.example.workingmoney.domain.entity.CursorSlice;
import org.example.workingmoney.domain.entity.Post;
import org.example.workingmoney.domain.entity.Comment;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record PostCursorWithCommentsResponseDto(
        List<PostWithCommentsDto> content,
        Long nextCursor,
        boolean hasNext
) {

    public static PostCursorWithCommentsResponseDto from(
            CursorSlice<Post> slice,
            Map<Long, List<Comment>> commentsByPostId
    ) {
        List<PostWithCommentsDto> content = slice.content().stream()
                .map(p -> new PostWithCommentsDto(p, commentsByPostId.getOrDefault(p.id(), List.of())))
                .collect(Collectors.toList());
        return new PostCursorWithCommentsResponseDto(content, slice.nextCursor(), slice.hasNext());
    }
}


