package org.example.workingmoney.repository.post;

import lombok.RequiredArgsConstructor;
import org.example.workingmoney.domain.entity.Post;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final PostMapper mapper;

    public Post create(String userId, String categoryCode, String title, String content) {
        PostEntity entity = new PostEntity(userId, categoryCode, title, content, 0L);
        mapper.insert(entity);
        return entity.toDomain();
    }

    public Optional<Post> findById(Long id) {
        return mapper.findById(id).map(PostEntity::toDomain);
    }

    public Post update(Long id, String userId, String categoryCode, String title, String content) {
        PostEntity entity = new PostEntity(userId, categoryCode, title, content, 0L);
        entity.setId(id);
        mapper.update(entity);
        return findById(id).orElseThrow();
    }

    public void deleteById(Long id) {
        mapper.deleteById(id);
    }
}
