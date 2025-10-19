package org.example.workingmoney.domain.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Post {

    @NonNull
    private final Long id;

    @NonNull
    private final String userId;

    @NonNull
    private final String categoryCode;

    @NonNull
    private final String title;

    @NonNull
    private final String content;

    @NonNull
    private final Long likeCount;
}



