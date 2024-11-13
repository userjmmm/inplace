package team7.inplace.search.persistence.dto;

public record SearchResult<T>(
        T searchResult,
        Double score
) {
}
