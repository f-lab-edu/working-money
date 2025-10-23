package org.example.workingmoney.ui.dto.response.post;

import org.example.workingmoney.domain.entity.Comment;
import org.example.workingmoney.domain.entity.Post;

import java.util.List;

public record PostWithCommentsDto(
        Post post,
        List<Comment> comments
) {}


