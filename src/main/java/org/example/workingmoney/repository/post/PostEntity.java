package org.example.workingmoney.repository.post;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.workingmoney.domain.entity.Post;
import org.example.workingmoney.repository.common.TimeBaseEntity;

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PostEntity extends TimeBaseEntity {

    @Setter
    private Long id;
    private final String userId;
    private final String categoryCode;
    private final String title;
    private final String content;
    private final Long likeCount;

    public Post toDomain() {
        return new Post(id, userId, categoryCode, title, content, likeCount);
    }
}


