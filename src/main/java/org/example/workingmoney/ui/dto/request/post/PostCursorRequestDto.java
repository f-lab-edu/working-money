package org.example.workingmoney.ui.dto.request.post;

import org.example.workingmoney.domain.entity.PostCategory;

public record PostCursorRequestDto(
        PostCategory category, 
        Long cursor,            // 마지막 게시글 ID (첫 요청 시 null)
        Integer size
) {
    public PostCursorRequestDto {
        if (size == null || size <= 0) {
            size = 20;
        }
    }

    public String getCategoryCode() {
        return category != null ? category.getCode() : null;
    }
}

