package org.example.workingmoney.repository.post;

import lombok.RequiredArgsConstructor;
import org.example.workingmoney.domain.entity.Post;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final PostMapper mapper;

    public Post create(String userId, String categoryCode, String title, String content) {
        PostEntity entity = new PostEntity(userId, categoryCode, title, content);
        mapper.insert(entity);
        return entity.toDomain();
    }

    public Post getById(Long id) {
        return mapper.getById(id).map(PostEntity::toDomain).orElseThrow();
    }

    public Post update(Long id, String userId, String categoryCode, String title, String content) {
        PostEntity entity = mapper.getById(id).orElseThrow();
        entity.setCategoryCode(categoryCode);
        entity.setTitle(title);
        entity.setContent(content);
        mapper.update(entity);
        return getById(id);
    }

    public void deleteById(Long id) {
        mapper.deleteById(id);
    }

    public List<Post> findPosts(String categoryCode, Long cursor, Integer size) {
        return mapper.findPosts(categoryCode, cursor, size)
                .stream()
                .map(PostEntity::toDomain)
                .toList();
    }
}
