package org.example.workingmoney.repository.post;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    List<CommentEntity> findByPostId(Long postId);
}


