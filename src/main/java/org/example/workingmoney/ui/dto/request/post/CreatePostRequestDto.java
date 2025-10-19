package org.example.workingmoney.ui.dto.request.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.workingmoney.common.validation.ValidPostContent;
import org.example.workingmoney.common.validation.ValidPostTitle;
import org.example.workingmoney.domain.entity.PostCategory;

public record CreatePostRequestDto(
        @NotNull
        PostCategory category,

        @NotBlank
        @ValidPostTitle
        String title,

        @NotBlank
        @ValidPostContent
        String content
) {}



