package team7.inplace.search.presentation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team7.inplace.search.application.SearchService;
import team7.inplace.search.application.dto.AutoCompletionInfo;
import team7.inplace.video.presentation.dto.VideoResponse;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController implements SearchControllerApiSpec {
    private final SearchService searchService;

    @Override
    @GetMapping("/complete")
    public ResponseEntity<List<AutoCompletionInfo>> searchKeywords(String value) {
        var response = searchService.searchAutoCompletions(value);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @GetMapping("/video")
    public ResponseEntity<List<VideoResponse>> searchVideo(String value) {
        var videos = searchService.searchVideo(value);

        var response = videos.stream()
                .map(VideoResponse::from)
                .toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
