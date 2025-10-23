package org.example.workingmoney.repository.post;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.workingmoney.domain.entity.Comment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final CommentMapper mapper;

    public List<Comment> findByPostId(@NonNull Long postId) {

        return mapper.findByPostId(postId).stream().map(CommentEntity::toDomain).toList();
    }
}


