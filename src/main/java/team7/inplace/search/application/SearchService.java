package team7.inplace.search.application;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team7.inplace.search.application.dto.AutoCompletionInfo;
import team7.inplace.search.application.dto.SearchType;
import team7.inplace.search.persistence.InfluencerSearchRepository;
import team7.inplace.search.persistence.PlaceSearchRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchService {
    private static final Integer MAX_COMPLETION_RESULTS = 5;

    private final InfluencerSearchRepository influencerSearchRepository;
    private final PlaceSearchRepository placeSearchRepository;

    public List<AutoCompletionInfo> searchAutoCompletions(String keyword) {
        var influencerSearchInfo = influencerSearchRepository.searchSimilarKeywords(keyword);
        var placeSearchInfo = placeSearchRepository.searchSimilarKeywords(keyword);

        var influencerAutoComplete = influencerSearchInfo.stream()
                .map(info -> new AutoCompletionInfo(info.searchResult().getName(), info.score(), SearchType.INFLUENCER))
                .toList();
        System.out.println("influencerAutoComplete: " + influencerAutoComplete);
        var placeAutoComplete = placeSearchInfo.stream()
                .map(info -> new AutoCompletionInfo(info.searchResult().getName(), info.score(), SearchType.PLACE))
                .toList();

        return Stream.concat(influencerAutoComplete.stream(), placeAutoComplete.stream())
                .sorted(Comparator.comparing(AutoCompletionInfo::score).reversed())
                .limit(MAX_COMPLETION_RESULTS)
                .toList();
    }
}
