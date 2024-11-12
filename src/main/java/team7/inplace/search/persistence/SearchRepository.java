package team7.inplace.search.persistence;

import java.util.List;
import team7.inplace.search.application.dto.AutoCompletionInfo;

public interface SearchRepository {
    Integer AUTO_COMPLETION_LIMIT = 5;

    List<AutoCompletionInfo> searchSimilarKeywords(String keyword);
}
