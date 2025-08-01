package search.dto;

public record AutoCompletionInfo(
        String result,
        Double score,
        SearchType searchType
) {
}
