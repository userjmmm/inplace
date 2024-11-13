package team7.inplace.search.persistence;

import java.util.List;
import team7.inplace.search.persistence.dto.SearchResult;

public interface SearchRepository<T> {
    Integer AUTO_COMPLETION_LIMIT = 5;

    List<SearchResult<T>> searchSimilarKeywords(String keyword);
}
