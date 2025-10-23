package org.example.workingmoney.ui.dto.response.post;

import org.example.workingmoney.domain.entity.CursorSlice;
import org.example.workingmoney.domain.entity.Post;

import java.util.List;

public record PostCursorResponseDto(
        List<Post> content,
        Long nextCursor,
        boolean hasNext
) {

    public static PostCursorResponseDto fromSlice(CursorSlice<Post> slice) {
        return new PostCursorResponseDto(slice.content(), slice.nextCursor(), slice.hasNext());
    }
}

