package team7.inplace.search.presentation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team7.inplace.influencer.application.dto.InfluencerInfo;
import team7.inplace.influencer.presentation.dto.InfluencerResponse;
import team7.inplace.search.application.SearchService;
import team7.inplace.search.application.dto.AutoCompletionInfo;
import team7.inplace.search.application.dto.PlaceSearchInfo;
import team7.inplace.video.presentation.dto.VideoResponse;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController implements SearchControllerApiSpec {
    private final SearchService searchService;

    @Override
    @GetMapping("/complete")
    public ResponseEntity<List<AutoCompletionInfo>> searchKeywords(@RequestParam String value) {
        var response = searchService.searchAutoCompletions(value);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @GetMapping("/video")
    public ResponseEntity<List<VideoResponse>> searchVideo(@RequestParam String value) {
        var videos = searchService.searchVideo(value);

        var response = videos.stream()
                .map(VideoResponse::from)
                .toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @GetMapping("/influencer")
    public ResponseEntity<List<InfluencerResponse>> searchInfluencer(@RequestParam String value) {
        var influencers = searchService.searchInfluencer(value);

        var response = influencers.stream()
                .map(InfluencerResponse::from)
                .toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @GetMapping("/page/influencer")
    public ResponseEntity<Page<InfluencerResponse>> searchInfluencer(
            @RequestParam String value,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<InfluencerInfo> influencers = searchService.searchInfluencer(value, pageable);

        Page<InfluencerResponse> response = influencers.map(InfluencerResponse::from);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @GetMapping("/place")
    public ResponseEntity<List<PlaceSearchInfo>> searchPlace(@RequestParam String value) {
        var places = searchService.searchPlace(value);

        return new ResponseEntity<>(places, HttpStatus.OK);
    }
}
