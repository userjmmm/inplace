package search.dto;

import search.query.dto.SearchResult;
import search.query.dto.SearchType;

public class SearchResponse {

    public record AutoCompletion (
        String result,
        Double score,
        SearchType searchType
    ) {
        public static AutoCompletion from(SearchResult.AutoCompletion completion) {
            return new AutoCompletion(
                completion.result(),
                completion.score(),
                completion.searchType()
            );
        }
    }

    public record Place(
        Long placeId,
        String placeName,
        String placeImgUrl,
        boolean likes
    ) {

        public static Place from(SearchResult.Place placeInfo) {
            return new Place(
                placeInfo.id(),
                placeInfo.name(),
                "",
                placeInfo.likes()
            );
        }

    }
}
