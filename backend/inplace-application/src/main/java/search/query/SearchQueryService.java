package search.query;

import influencer.query.InfluencerQueryResult;
import influencer.query.InfluencerQueryResult.Simple;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import search.InfluencerSearchQueryDslRepository;
import search.PlaceSearchQueryDslRepository;
import search.SearchQueryResult;
import search.VideoSearchQueryDslRepository;
import video.query.VideoQueryResult;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchQueryService {
    private static final Integer MAX_COMPLETION_RESULTS = 5;
    private static final Integer SEARCH_LIMIT = 10;

    private final VideoSearchQueryDslRepository videoSearchRepository;
    private final InfluencerSearchQueryDslRepository influencerSearchRepository;
    private final PlaceSearchQueryDslRepository placeSearchRepository;

    public List<AutoCompletionInfo> searchAutoCompletions(SearchType type, String keyword) {
        var placeSearchInfo = placeSearchRepository.searchAutoComplete(keyword).stream()
            .map(
                info -> new AutoCompletionInfo(info.keyword(), info.score(), SearchType.PLACE))
            .toList();

        if (type.equals(SearchType.PLACE)) {
            return placeSearchInfo;
        }

        var influencerSearchInfo = influencerSearchRepository.searchAutoComplete(keyword).stream()
            .map(
                info -> new AutoCompletionInfo(info.keyword(), info.score(), SearchType.INFLUENCER))
            .toList();

        return Stream.concat(influencerSearchInfo.stream(), placeSearchInfo.stream())
            .sorted(Comparator.comparing(AutoCompletionInfo::score).reversed())
            .limit(MAX_COMPLETION_RESULTS)
            .toList();
    }

    public Page<Simple> searchInfluencer(String keyword, Pageable pageable) {
        var userId = AuthorizationUtil.getUserIdOrNull();
        var influencerInfos = influencerSearchRepository.search(keyword, pageable, userId);

        return influencerInfos;
    }

    public List<InfluencerQueryResult.Simple> searchInfluencer(String keyword) {
        var userId = AuthorizationUtil.getUserIdOrNull();
        var influencerInfos = influencerSearchRepository.search(
            keyword,
            Pageable.ofSize(SEARCH_LIMIT),
            userId
        );

        return influencerInfos.getContent();
    }

    public List<VideoQueryResult.DetailedVideo> searchVideo(String keyword) {
        var userId = AuthorizationUtil.getUserIdOrNull();
        var videoInfos = videoSearchRepository.search(
            keyword,
            Pageable.ofSize(SEARCH_LIMIT),
            userId
        );

        return videoInfos.getContent();
    }

    public List<SearchQueryResult.Place> searchPlace(String keyword) {
        var userId = AuthorizationUtil.getUserIdOrNull();
        var placeInfos = placeSearchRepository.search(
            keyword,
            Pageable.ofSize(SEARCH_LIMIT),
            userId
        );

        return placeInfos.getContent();
    }
}
