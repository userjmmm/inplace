package team7.inplace.search.persistence.dto;

import java.util.Objects;

public record SearchResult<T>(
        T searchResult,
        Double score
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SearchResult<?> that = (SearchResult<?>) o;
        return Objects.equals(searchResult, that.searchResult);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(searchResult);
    }
}
