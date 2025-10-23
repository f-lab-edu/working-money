package org.example.workingmoney.domain.entity;

import java.util.List;
import java.util.function.Function;

public record CursorSlice<T>(
        List<T> content,
        Long nextCursor,
        boolean hasNext
) {
    public static <T> CursorSlice<T> createCursorSlice(
            List<T> items,
            int requestedSize,
            Function<T, Long> idExtractor
    ) {
        boolean hasNext = items.size() > requestedSize;
        List<T> content = hasNext ? items.subList(0, requestedSize) : items;
        Long nextCursor = hasNext && !content.isEmpty() ? idExtractor.apply(content.get(content.size() - 1)) : null;

        return new CursorSlice<>(content, nextCursor, hasNext);
    }
}
