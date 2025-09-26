package org.example.workingmoney.repository.post;

import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper {

    void insert(PostEntity postEntity);

    void update(PostEntity postEntity);

    void deleteById(Long id);

    Optional<PostEntity> findById(Long id);
}


