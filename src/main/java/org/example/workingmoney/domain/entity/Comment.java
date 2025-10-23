package org.example.workingmoney.domain.entity;

import lombok.NonNull;

public record Comment(
        @NonNull Long id,
        @NonNull Long postId,
        @NonNull String userId,
        @NonNull String content) {

}



