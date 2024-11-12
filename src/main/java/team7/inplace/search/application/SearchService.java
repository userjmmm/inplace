package team7.inplace.search.application;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team7.inplace.search.application.dto.AutoCompletionInfo;
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
        log.info("Searching auto completions for keyword: {}", keyword);
        var influencerAutoCompletions = influencerSearchRepository.searchSimilarKeywords(keyword);
        var placeAutoCompletions = placeSearchRepository.searchSimilarKeywords(keyword);

        return Stream.concat(influencerAutoCompletions.stream(), placeAutoCompletions.stream())
                .sorted(Comparator.comparing(AutoCompletionInfo::score).reversed())
                .limit(MAX_COMPLETION_RESULTS)
                .toList();
    }
}
