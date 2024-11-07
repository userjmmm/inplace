package team7.inplace.video.presentation;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team7.inplace.video.application.VideoFacade;
import team7.inplace.video.application.VideoService;
import team7.inplace.video.presentation.dto.VideoResponse;
import team7.inplace.video.presentation.dto.VideoSearchParams;

@RestController
@RequiredArgsConstructor
@RequestMapping("/videos")
public class VideoController implements VideoControllerApiSpec {
    private final VideoService videoService;
    private final VideoFacade videoFacade;

    @GetMapping()
    public ResponseEntity<Page<VideoResponse>> readVideos(
            @ModelAttribute VideoSearchParams searchParams,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        Page<VideoResponse> videoResponses = videoService.getVideosBySurround(searchParams, pageable)
                .map(VideoResponse::from);
        return new ResponseEntity<>(videoResponses, HttpStatus.OK);
    }

    @GetMapping("/new")
    public ResponseEntity<Page<VideoResponse>> readByNew(
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        Page<VideoResponse> videoResponses = videoService.getAllVideosDesc(pageable)
                .map(VideoResponse::from);
        return new ResponseEntity<>(videoResponses, HttpStatus.OK);
    }

    // 조회수 반환 기능 개발 시 개발
    @GetMapping("/cool")
    public ResponseEntity<Page<VideoResponse>> readByCool(
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        List<VideoResponse> videoResponses = new ArrayList<>();
        return new ResponseEntity<>(new PageImpl<>(videoResponses, pageable, 0), HttpStatus.OK);
    }

    // 토큰 필요 메서드
    @GetMapping("/my")
    public ResponseEntity<Page<VideoResponse>> readByInfluencer(
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        Page<VideoResponse> videoResponses = videoFacade.getVideosByMyInfluencer(pageable)
                .map(VideoResponse::from);
        return new ResponseEntity<>(videoResponses, HttpStatus.OK);
    }

    @GetMapping("/null")
    public ResponseEntity<Page<VideoResponse>> readPlaceNullVideo(
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        Page<VideoResponse> videoResponses = videoService.getPlaceNullVideo(pageable)
                .map(VideoResponse::from);
        return new ResponseEntity<>(videoResponses, HttpStatus.OK);
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long videoId) {
        videoService.deleteVideo(videoId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
