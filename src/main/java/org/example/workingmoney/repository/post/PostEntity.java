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
    private long id;
    private final String userId;
    @Setter
    private String categoryCode;
    @Setter
    private String title;
    @Setter
    private String content;
    private final Long likeCount;

    public PostEntity(
            String userId,
            String categoryCode,
            String title,
            String content) {

        this.userId = userId;
        this.categoryCode = categoryCode;
        this.title = title;
        this.content = content;
        this.likeCount = 0L;
    }

    public Post toDomain() {
        return new Post(id, userId, categoryCode, title, content, likeCount);
    }
}


