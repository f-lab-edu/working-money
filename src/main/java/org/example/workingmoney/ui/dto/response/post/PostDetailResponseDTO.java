package org.example.workingmoney.ui.dto.response.post;

import java.util.List;
import org.example.workingmoney.domain.entity.Comment;
import org.example.workingmoney.domain.entity.Post;

public record PostDetailResponseDTO(
        Post post,
        List<Comment> comments) {

}
