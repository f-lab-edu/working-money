package org.example.workingmoney.repository.post;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.workingmoney.domain.entity.Comment;
import org.example.workingmoney.repository.common.TimeBaseEntity;

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CommentEntity extends TimeBaseEntity {

    private final long id;
    private final long postId;
    private final String userId;
    private final String content;

    public Comment toDomain() {
        return new Comment(id, postId, userId, content);
    }
}
