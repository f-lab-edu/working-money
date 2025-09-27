package org.example.workingmoney.ui.dto.request.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.workingmoney.domain.entity.PostCategory;

public record CreatePostRequestDto(
        @NotNull String userId,
        @NotNull PostCategory category,
        @NotBlank String title,
        @NotBlank String content
) {}



