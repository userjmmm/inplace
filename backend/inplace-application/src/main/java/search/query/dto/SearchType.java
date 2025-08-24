package search.query.dto;

import exception.InplaceException;
import exception.code.SearchErrorCode;

public enum SearchType {
    PLACE, INFLUENCER, VIDEO, ALL;

    public static SearchType from(String type) {
        if (type == null || type.isBlank()) {
            return ALL;
        }

        if (!type.equals("all") && !type.equals("place")) {
            throw InplaceException.of(SearchErrorCode.SEARCH_TYPE_INVALID);
        }

        return SearchType.valueOf(type.toUpperCase());
    }
}
