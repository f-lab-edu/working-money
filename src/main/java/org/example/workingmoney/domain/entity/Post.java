package org.example.workingmoney.domain.entity;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Post {

    @Nonnull
    private final Long id;

    @Nonnull
    private final String userId;

    @Nonnull
    private final String categoryCode;

    @Nonnull
    private final String title;

    @Nonnull
    private final String content;

    @Nonnull
    private final Long likeCount;
}



