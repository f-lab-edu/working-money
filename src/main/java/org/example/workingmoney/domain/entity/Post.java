package org.example.workingmoney.domain.entity;

import lombok.NonNull;

public record Post(
        @NonNull Long id,
        @NonNull String userId,
        @NonNull String categoryCode,
        @NonNull String title,
        @NonNull String content,
        @NonNull Long likeCount
) {

}



