package team7.inplace.search.persistence;

import java.util.List;
import team7.inplace.search.persistence.dto.SearchResult;

public interface SearchRepository<T> {
    Integer SEARCH_LIMIT = 10;

    default List<SearchResult<T>> searchEntityByKeywords(String keyword) {
        return List.of();
    }
}